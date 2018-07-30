import re
import datetime, time
import traceback
import tarfile
import subprocess
import cx_Oracle

from Script import Script, InfoScriptException, WarningScriptException, ErrorScriptException
from Model import Rule, Archive, Backup, BackupFile
from settings import *

CMD_XRDCP            = '/usr/bin/xrdcp'
CMD_XRDCP_DESTINATION_FLAG  = '-ODsvcClass=amscdr'
CMD_XRDCP_SOURCE_FLAG       = '-OSsvcClass=amscdr'
CMD_NSLS             = '/usr/bin/nsls'
CMD_NSLS_CKSUM_FLAG  = '--checksum'
CMD_NSLS_LS_FLAG     = '-l'
CMD_NSRM             = '/usr/bin/nsrm'
CMD_CKSUM            = '/usr/bin/cksum'
CMD_XRDADLER32       = '/usr/bin/xrdadler32'
CMD_STAGER_QRY       = '/usr/bin/stager_qry'
CMD_STAGER_QRY_FLAG  = '-M'
CMD_STAGER_GET       = '/usr/bin/stager_get'
CMD_TOUCH            = '/usr/bin/touch'
CMD_FIND             = '/usr/bin/find'
CMD_RM               = '/usr/bin/rm'
#CMD_USED_SPACE       = 'df -h --output="pcent,target" | grep "pcamsj0_Data"'
CMD_USED_SPACE       = 'df -h'
#CMD_EOS              = '/afs/cern.ch/project/eos/installation/0.3.15/bin/eos.select'

STATUS_OK       = 'OK'
STATUS_EMPTY    = 'EMPTY'
STATUS_RECREATE = 'RECREATE'
STATUS_NEW      = 'NEW'

CASTOR_STATUS_EMPTY     = ''

CASTOR_STATUS_STAGEIN   = 'STAGEIN'
CASTOR_STATUS_STAGEIN_D = 'the file is being recalled from tape or being internally disk-to-disk copied from another diskpool'

CASTOR_STATUS_STAGED    = 'STAGED'
CASTOR_STATUS_STAGED_D  = 'the file has been successfully staged from tape and it is available on disk, or the file is only available on disk if its associated nbTapeCopies = 0'

CASTOR_STATUS_STAGEOUT  = 'STAGEOUT'
CASTOR_STATUS_STAGEOUT_D = 'the file is opened by the client, who is writing. Have to wait for the client to finish'

CASTOR_STATUS_CANBEMIGR = 'CANBEMIGR'
CASTOR_STATUS_CANBEMIGR_D = 'the file is on disk and any transfer from client has been completed. The migrator will take it for tape migration'

CASTOR_STATUS_INVALID   = 'INVALID'
CASTOR_STATUS_INVALID_D   = 'the file has no valid diskcopy/tapecopy, or a failure happened concerning the file'

SOC_STATUS_EMPTY = ''
SOC_STATUS_OK    = 'OK'
SOC_STATUS_NOT_NEEDED    = 'NOT_NEEDED'

TAPE_SIZE = 1400 * 1024 * 1024 * 1024 # GiB

class EndOfFilesException(WarningScriptException):
    def __init__(self):
        self.error = "End of files for rule"

class BeginOfFilesException(WarningScriptException):
    def __init__(self):
        self.error = "Begin of files for rule"

class LastDirWillNotProcessedException(WarningScriptException):
    def __init__(self, dir):
        self.error = "dir=%s is a last dir and will not be processed" % dir

class DirectoryNotExistsException(WarningScriptException):
    def __init__(self, name):
        self.error = "Directory not exists: %s" % name

class EmptyArchiveException(WarningScriptException):
    def __init__(self):
        self.error = "Empty archive"

class RuleInProcessException(WarningScriptException):
    def __init__(self, rule_path, period):
        self.error = "Rule %s is in process %d seconds (%s), skip it" % (rule_path, total_seconds(period), strfdelta(period))

class ArchiveInProcessException(WarningScriptException):
    def __init__(self, name, period):
        self.error = "Archive %s is in process %d seconds (%s), skip it" % (name, total_seconds(period), strfdelta(period))

class ArchiveWithoutRuleException(ErrorScriptException):
    def __init__(self, id, name):
        self.error = "Archive id=%d name=%s without rule" % (id, name)

class ArchiveWithoutLocalFileException(ErrorScriptException):
    def __init__(self, id, name, path):
        self.error = "Archive id=%d name=%s without local file path=%s" % (id, name, path)

class FailedCopyFileException(ErrorScriptException):
    def __init__(self, code, error):
        self.error = "Failed copy file, returncode=%d (%s)" % (code, error)

class FailedStagerQueryException(ErrorScriptException):
    def __init__(self, code, error):
        self.error = "Failed stager query, returncode=%d (%s)" % (code, error)

class FailedStagerGetException(ErrorScriptException):
    def __init__(self, code, error):
        self.error = "Failed stager get, returncode=%d (%s)" % (code, error)

class UnsupportedReturnCodeException(ErrorScriptException):
    def __init__(self, code, error):
        self.error = "Unsupported returncode=%d (%s)" % (code, error)

class CastorStatusNotChangedException(InfoScriptException):
    def __init__(self, file):
        self.error = "Castor status not changed %s" % file

class TooManyFilesNotCopiedException(WarningScriptException):
    def __init__(self, size):
        self.error = "Too many files not copied to CASTOR (%s)" % sizeof_fmt(size)

class WaitingForFileStageException(InfoScriptException):
    def __init__(self):
        self.error = "Waiting while file will be in STAGED status"

def total_seconds(timedelta):
    return (timedelta.microseconds + (timedelta.seconds + timedelta.days * 24 * 3600) * 10**6) / 10**6


def sizeof_fmt(num, suffix='B'):
    for unit in ['','Ki','Mi','Gi','Ti','Pi','Ei','Zi']:
        if abs(num) < 1024.0:
            return "%3.1f%s%s" % (num, unit, suffix)
        num /= 1024.0
    return "%.1f%s%s" % (num, 'Yi', suffix)

def sizeof_gb(num, suffix='B'):
    return "%3.1f%s%s" % (num / 1024 / 1024 / 1024, 'Gi', suffix)

def strfdelta(tdelta, fmt = "{days} days {hours}:{minutes}"):
    d = {"days": tdelta.days}
    d["hours"], rem = divmod(tdelta.seconds, 3600)
    d["minutes"], d["seconds"] = divmod(rem, 60)
    return fmt.format(**d)


class BackupScript(Script):

    modes = [
        'create',
        'create_down',
        'castor_copy',
        'castor_check',
        'soc_copy',
        'check',
        'stat',
        'soc_delete',
    ]

    def __init__(self):
        super(BackupScript, self).__init__(self.__class__.__name__.lower())

    def runMode(self):
        if self.mode == "create":
            self.createArchive()
        if self.mode == "create_down":
            self.createDownArchive()
        elif self.mode == "castor_copy":
            self.castorCopy()
        elif self.mode == "castor_check":
            self.castorCheck()
        elif self.mode == "soc_copy":
            self.socCopy()
        elif self.mode == "soc_delete":
            self.socDelete()
        elif self.mode == "check":
            self.check()
        elif self.mode == "stat":
            self.stat()

    def createArchive(self):
        # select rules for process
        try:
            # too many files not copied to CASTOR or not checked yet
            size = self.db.sql("SELECT SUM(FILE_SIZE) FROM FRAMES_BACKUP_ARCHIVE WHERE STATUS=:status AND CASTOR_STATUS=:castor_status", status=STATUS_OK, castor_status=CASTOR_STATUS_CANBEMIGR)
            if len(size) == 1:
                size = size[0][0]
                if (size > 1024 * 1024 * 1024 * 500):
                    raise TooManyFilesNotCopiedException(size)

            rules = self.db.sql("SELECT ID,PATH,DIR_COUNT FROM FRAMES_BACKUP_RULE WHERE IS_ACTIVE=:is_active", is_active = 1)

        except cx_Oracle.DatabaseError, info:
            self.logger.error("sql_error='%s'", str(info).replace("\n", ""))
        else:
            # loop through rules
            for RULE_ID,PATH,DIR_COUNT in rules:
                self.rule = Rule(id=RULE_ID, path=PATH, dir_count=DIR_COUNT)
                try:
                    self.logger.info("Process rule=%s, dir_count=%d", self.rule.path, self.rule.dir_count)
                    # skip rule in process
                    self.checkRuleInProcess()

                    # find next archive for this rule
                    self.findNextFolderForArchive()

                    # get list of files to be achived
                    self.findFilesForArchive(FRAMES_DIR)

                    # create tar file
                    self.generateArchive(FRAMES_DIR)

                    # insert archive and files to DB
                    file_name = self.getLocalArchivePath()
                    crc32 = self.getCrc32(file_name)
                    adler32 = self.getAdler32(file_name)
                    file_size = os.path.getsize(file_name)
                    self.insertArchiveDB(crc32, adler32, file_size)


                except cx_Oracle.DatabaseError, info:
                    self.db.sql("ROLLBACK")
                    self.logger.error("sql_error='%s'", str(info).replace("\n", ""))

                except(InfoScriptException) as error:
                    self.logger.info(error)

                except(WarningScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.warning(error)

                except(ErrorScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.error(error)

                except(Exception) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.critical("python_error='%s' traceback='%s'", error, traceback.format_exc())

    def createDownArchive(self):
        # select rules for process
        try:
            # too many files not copied to CASTOR or not checked yet
            size = self.db.sql("SELECT SUM(FILE_SIZE) FROM FRAMES_BACKUP_ARCHIVE WHERE STATUS=:status AND CASTOR_STATUS=:castor_status", status=STATUS_OK, castor_status=CASTOR_STATUS_CANBEMIGR)
            if len(size) == 1:
                size = size[0][0]
                if (size > 1024 * 1024 * 1024 * 500):
                    raise TooManyFilesNotCopiedException(size)

            rules = self.db.sql("SELECT ID,PATH,DIR_COUNT FROM FRAMES_BACKUP_RULE WHERE IS_ACTIVE=:is_active", is_active = 1)

        except cx_Oracle.DatabaseError, info:
            self.logger.error("sql_error='%s'", str(info).replace("\n", ""))
        else:
            # loop through rules
            for RULE_ID,PATH,DIR_COUNT in rules:
                self.rule = Rule(id=RULE_ID, path=PATH, dir_count=DIR_COUNT)
                try:
                    self.logger.info("Process rule=%s, dir_count=%d", self.rule.path, self.rule.dir_count)
                    # skip rule in process
                    #self.checkRuleInProcess()

                    # find next archive for this rule
                    self.findPrevFolderForArchive()

                    # get list of files to be achived
                    self.findFilesForArchive(FRAMES_DIR_OLD)

                    # create tar file
                    self.generateArchive(FRAMES_DIR_OLD)

                    file_name = self.getLocalArchivePath()
                    crc32 = self.getCrc32(file_name)
                    adler32 = self.getAdler32(file_name)
                    file_size = os.path.getsize(file_name)

                    # move file to ams-backup server
                    self.moveFileToAmsBackup()

                    # insert archive and files to DB
                    self.insertArchiveDB(crc32, adler32, file_size, soc_status='NOT_NEEDED')


                except cx_Oracle.DatabaseError, info:
                    self.db.sql("ROLLBACK")
                    self.logger.error("sql_error='%s'", str(info).replace("\n", ""))

                except(InfoScriptException) as error:
                    self.logger.info(error)

                except(WarningScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.warning(error)

                except(ErrorScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.error(error)

                except(Exception) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.critical("python_error='%s' traceback='%s'", error, traceback.format_exc())


    def moveFileToAmsBackup(self):
        local_name = self.getLocalArchivePath()
        file_name = self.getSocArchiveFile()
        file_name_bak = "%s.bak" % file_name
        file_path_bak = '%s/%s' % (FRAMES_DIR_MOUNT, file_name_bak)

        fdir = os.path.dirname(file_path_bak)
        if not os.path.isdir(fdir):
            self.cmd.check_call(['/bin/mkdir', '-p', fdir])

        self.cmd.check_call(['/bin/cp', '-f', local_name, file_path_bak])
        self.cmd.check_call(['/bin/mv', file_path_bak, '%s/%s' % (FRAMES_DIR_MOUNT, file_name)])
        self.cmd.check_call(['/bin/rm', local_name])


    def castorCopy(self):
        # select files for process
        try:
            files = self.db.sql(
                "SELECT ID,NAME,RULE_ID,FILE_SIZE,ADLER FROM FRAMES_BACKUP_ARCHIVE WHERE STATUS=:status AND CASTOR_STATUS IS NULL ORDER BY CREATED_AT",
                status=STATUS_OK
            )

        except cx_Oracle.DatabaseError, info:
            self.logger.error("sql_error='%s'", str(info).replace("\n", ""))
        else:
            # loop through archives
            for ID,NAME,RULE_ID,FILE_SIZE,ADLER in files:
                self.archive = Archive(id=ID, name=NAME, rule_id=RULE_ID, size=FILE_SIZE, adler32=ADLER)
                try:
                    self.logger.info("Process archive=%s, rule_id=%d", self.archive.name, self.archive.rule_id)

                    # get archive rule
                    self.findArchiveRule(self.archive)

                    # skip archive in process
                    self.checkArchiveInProcess()

                    # check file exists
                    if self.castorFileExists() == 1:
                        # file exists, and it correct
                        self.castor_status = CASTOR_STATUS_CANBEMIGR
                    else:
                        # copy file to CASTOR
                        self.castorCopyArchive()

                    # insert row to DB
                    self.updateCastorStatus()


                except cx_Oracle.DatabaseError, info:
                    self.db.sql("ROLLBACK")
                    self.logger.error("sql_error='%s'", str(info).replace("\n", ""))

                except(InfoScriptException) as error:
                    self.logger.info(error)

                except(WarningScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.warning(error)

                except(ErrorScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.error(error)

                except(Exception) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.critical("python_error='%s' traceback='%s'", error, traceback.format_exc())


    def castorCheck(self):
        # select files for process
        try:
            files = self.db.sql(
                "SELECT ID,NAME,RULE_ID,FILE_SIZE,ADLER,SOC_STATUS FROM FRAMES_BACKUP_ARCHIVE WHERE STATUS=:status AND CASTOR_STATUS=:castor_status ORDER BY CREATED_AT",
                status = STATUS_OK,
                castor_status = CASTOR_STATUS_CANBEMIGR
            )

        except cx_Oracle.DatabaseError, info:
            self.logger.error("sql_error='%s'", str(info).replace("\n", ""))
        else:
            # loop through archives
            for ID,NAME,RULE_ID,FILE_SIZE,ADLER,SOC_STATUS in files:
                self.archive = Archive(id=ID, name=NAME, rule_id=RULE_ID, size=FILE_SIZE, adler32=ADLER)
                try:
                    self.logger.info("Process archive=%s, rule_id=%d", self.archive.name, self.archive.rule_id)

                    # get archive rule
                    self.findArchiveRule(self.archive)

                    # skip archive in process
                    self.checkArchiveInProcess()

                    # check file via stager_qry
                    self.castorCheckStatus()

                    # remove file from local disk if file have soc_status=NOT_NEEDED
                    if (self.castor_status == 'STAGED' and SOC_STATUS == 'NOT_NEEDED'):
                        local_file = self.getLocalArchivePath()
                        if os.path.exists(local_file) == True:
                            os.remove(local_file)

                    # update DB
                    self.updateCastorStatus()


                except cx_Oracle.DatabaseError, info:
                    self.db.sql("ROLLBACK")
                    self.logger.error("sql_error='%s'", str(info).replace("\n", ""))

                except(InfoScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.info(error)

                except(WarningScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.warning(error)

                except(ErrorScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.error(error)

                except(Exception) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.critical("python_error='%s' traceback='%s'", error, traceback.format_exc())


    def socCopy(self):
        # select files for process
        try:
            sql = "SELECT ID,NAME,RULE_ID,FILE_SIZE,ADLER FROM FRAMES_BACKUP_ARCHIVE WHERE STATUS=:status AND CASTOR_STATUS=:castor_status AND SOC_STATUS IS NULL ORDER BY CREATED_AT DESC"
            files = self.db.sql(
                ("SELECT ID,NAME,RULE_ID,FILE_SIZE,ADLER FROM (%s) WHERE ROWNUM<15" % sql),
                status=STATUS_OK,
                castor_status=CASTOR_STATUS_STAGED
            )

        except cx_Oracle.DatabaseError, info:
            self.logger.error("sql_error='%s'", str(info).replace("\n", ""))
        else:
            # loop through archives
            for ID,NAME,RULE_ID,FILE_SIZE,ADLER in files:
                self.archive = Archive(id=ID, name=NAME, rule_id=RULE_ID, size=FILE_SIZE, adler32=ADLER)
                try:
                    self.logger.info("Process archive=%s, rule_id=%d", self.archive.name, self.archive.rule_id)

                    # get archive rule
                    self.findArchiveRule(self.archive)

                    # skip archive in process
                    self.checkArchiveInProcess()

                    # check file exists
                    if self.socFileExists() == 1:
                        # file exists, and it correct
                        self.soc_status = SOC_STATUS_OK
                    else:
                        # copy file to SOC
                        self.socCopyArchive()

                    # insert row to DB
                    self.updateSocStatus()


                except cx_Oracle.DatabaseError, info:
                    self.db.sql("ROLLBACK")
                    self.logger.error("sql_error='%s'", str(info).replace("\n", ""))

                except(InfoScriptException) as error:
                    self.logger.info(error)

                except(WarningScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.warning(error)

                except(ErrorScriptException) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.error(error)

                except(Exception) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.critical("python_error='%s' traceback='%s'", error, traceback.format_exc())

    def socDelete(self):
        
        # check free space, if <= 95%, then delete 10 oldest files
        spaceLimit = 95 # %
        df_output = self.cmd.check_output(CMD_USED_SPACE.split(' '));
        if '/tmp_mnt/pcamsj0_Data' not in df_output:
            self.logger.error("df: j0 mount not found")
            return 
        df_output_j0 = [x.split(' ')[-2] for x in df_output.split('\n') if '/tmp_mnt/pcamsj0_Data' in x]
        spaceTaken = int(df_output_j0[0].split('%')[0])

        # do nothing if enough space
        if spaceTaken < spaceLimit:
            self.logger.info("enough space [ %i ], do nothing", spaceTaken )
            return 

        self.logger.info("low space [ %i ], delete something", spaceTaken )

        try:
            sql = "SELECT ID,NAME,RULE_ID,FILE_SIZE,ADLER FROM FRAMES_BACKUP_ARCHIVE WHERE RULE_ID=:rule_id AND STATUS=:status AND CASTOR_STATUS=:castor_status AND SOC_STATUS=:soc_status ORDER BY NAME"
            files = self.db.sql(
                ("SELECT ID,NAME,RULE_ID,FILE_SIZE,ADLER FROM (%s) WHERE ROWNUM<=10" % sql),
                rule_id=2,
                status=STATUS_OK,
                castor_status=CASTOR_STATUS_STAGED,
                soc_status=SOC_STATUS_OK
            )

        except cx_Oracle.DatabaseError, info:
            self.logger.error("sql_error='%s'", str(info).replace("\n", ""))
        else:
            # loop through archives
            for ID,NAME,RULE_ID,FILE_SIZE,ADLER in files:
                self.archive = Archive(id=ID, name=NAME, rule_id=RULE_ID, size=FILE_SIZE, adler32=ADLER)
                try:
                    self.logger.info("Process archive=%s, rule_id=%d", self.archive.name, self.archive.rule_id)

                    self.findArchiveRule(self.archive)

                    path = self.getSocArchivePath()

                    self.logger.info("Dir for remove %s", path)
                    self.cmd.check_call([CMD_RM, path])

                    self.db.sql(
                        "UPDATE FRAMES_BACKUP_ARCHIVE SET SOC_STATUS=:soc_status WHERE ID=:archive_id",
                        soc_status=SOC_STATUS_NOT_NEEDED,
                        archive_id=self.archive.id
                    )

                    self.db.sql("COMMIT")
                    
                except cx_Oracle.DatabaseError, info:
                    self.db.sql("ROLLBACK")
                    self.logger.error("sql_error='%s'", str(info).replace("\n", ""))

                except(Exception) as error:
                    self.db.sql("ROLLBACK")
                    self.logger.critical("python_error='%s' traceback='%s'", error, traceback.format_exc())

    def check(self):
        # select rules for process
        try:
            rules = self.db.sql("SELECT ID,PATH,DIR_COUNT,IS_ACTIVE FROM FRAMES_BACKUP_RULE")
            # loop through rules
            for RULE_ID,PATH,DIR_COUNT,IS_ACTIVE in rules:
                path = "%s/%s" % (FRAMES_DIR, PATH)
                self.logger.info("Check rule %s", PATH)
                dirs = map(lambda d:int(d), self.listDir(path))
                for i in range(dirs[0], dirs[-1]+1):
                    check_dir = "%s/%s/%04d" % (FRAMES_DIR, PATH, i)
                    self.logger.info("Check dir %s", check_dir)
                    if os.path.isdir(check_dir):
                        # check files
                        """
                        files = map(lambda d:int(d), self.listFile(check_dir))
                        for j in range(0, files[-1]+1):
                            check_file = "%s/%03d" % (check_dir, j)
                            if os.path.isfile(check_file) == False:
                                self.logger.warning("Missing file %s", check_file)

                        if files[-1] != 999:
                            self.logger.warning("Last file %s", check_file)
                        """
                    else:
                        if IS_ACTIVE == 1 and DIR_COUNT == 1:
                            # create empty archive if not exists
                            if self.createEmptyArchive(RULE_ID, check_dir):
                                self.logger.warning("Missing directory %s", check_dir)
                        else:
                            self.logger.warning("Missing directory %s", check_dir)

            self.db.sql("COMMIT")

        except cx_Oracle.DatabaseError, info:
            self.db.sql("ROLLBACK")
            self.logger.error("sql_error='%s'", str(info).replace("\n", ""))

        except(InfoScriptException) as error:
            self.logger.info(error)

        except(WarningScriptException) as error:
            self.db.sql("ROLLBACK")
            self.logger.warning(error)

        except(Exception) as error:
            self.db.sql("ROLLBACK")
            self.logger.critical("python_error='%s' traceback='%s'", error, traceback.format_exc())


    def createEmptyArchive(self, rule_id, dir):
        name = dir.split('/')
        name = "%s-%s.tar" % (name[-1], name[-1])
        res = self.db.sql("SELECT 1 FROM FRAMES_BACKUP_ARCHIVE WHERE RULE_ID=:rule_id AND NAME=:name",  rule_id = rule_id, name = name)
        if res == []:
            # insert empty archive
            self.db.sql(
                "INSERT INTO FRAMES_BACKUP_ARCHIVE \
                (RULE_ID,NAME,FILE_SIZE,FILE_COUNT,IS_FULL,STATUS,CASTOR_STATUS,CRC,ADLER,CREATED_AT,UPDATED_AT) \
                 VALUES \
                (:rule_id,:name,:file_size,:file_count,:is_full,:status,:castor_status,:crc,:adler,:created_at,:updated_at)",
                rule_id = rule_id,
                name = name,
                file_size = 0,
                file_count = 0,
                is_full = 0,
                status = STATUS_EMPTY,
                castor_status = CASTOR_STATUS_EMPTY,
                crc = 0,
                adler = 0,
                created_at = datetime.datetime.utcnow(),
                updated_at = datetime.datetime.utcnow()
            )
            return True
        else:
            return False

    def checkRuleInProcess(self):
        res = self.db.sql("SELECT UPDATED_AT FROM FRAMES_BACKUP_RULE WHERE ID=:rule_id FOR UPDATE NOWAIT",  rule_id = self.rule.id)
        if res == []:
            raise RuleInProcessException(self.rule.path, datetime.datetime.utcnow() - row[0])


    def checkArchiveInProcess(self):
        res = self.db.sql("SELECT UPDATED_AT FROM FRAMES_BACKUP_ARCHIVE WHERE ID=:archive_id FOR UPDATE NOWAIT", archive_id = self.archive.id)
        if res == []:
            raise ArchiveInProcessException(self.archive.name, datetime.datetime.utcnow() - row[0])


    def findNextFolderForArchive(self):
        self.next_folder = None
        last_archive = self.db.sql(
            "SELECT NAME FROM (SELECT NAME FROM FRAMES_BACKUP_ARCHIVE WHERE RULE_ID=:rule_id ORDER BY NAME DESC) WHERE ROWNUM=1",
            rule_id = self.rule.id
        )
        if last_archive == []:
            # search first existen directory
            next_folder = int(self.listDir("%s/%s" % (FRAMES_DIR, self.rule.path))[0])
        else:
            next_folder = int(last_archive[0][0].split("-")[1].split(".")[0]) + 1

        if (next_folder > 9999):
            raise EndOfFilesException()

        self.next_folder = next_folder


    def findPrevFolderForArchive(self):
        self.next_folder = None
        last_archive = self.db.sql(
            "SELECT NAME FROM (SELECT NAME FROM FRAMES_BACKUP_ARCHIVE WHERE RULE_ID=:rule_id ORDER BY NAME) WHERE ROWNUM=1",
            rule_id = self.rule.id
        )
        if last_archive == []:
            # search last existen directory
            next_folder = int(self.listDir("%s/%s" % (FRAMES_DIR_OLD, self.rule.path))[0])
        else:
            next_folder = int(last_archive[0][0].split("-")[0].split(".")[0]) - self.rule.dir_count

        if (next_folder < 0000):
            raise BeginOfFilesException()

        self.next_folder = next_folder


    def findFilesForArchive(self, frames_dir):
        archive_name = []
        self.archive_files = []
        self.archive_files_missed = []

        if (self.rule.dir_count + self.next_folder > 9999):
            dir_count = 9999 + 1 - self.next_folder
        else:
            dir_count = self.rule.dir_count

        for i in xrange(self.next_folder, self.next_folder + dir_count):
            dir = '%s/%s/%04d' % (frames_dir, self.rule.path, i);
            self.logger.info("Process dir=%s", dir);
            if os.path.isdir(dir) == False:
                raise DirectoryNotExistsException(dir)
            elif (self.isLastDir(dir) == True):
                raise LastDirWillNotProcessedException(dir)
            else:
                archive_name.append('%04d' % i)
                files = self.listFile(dir)
                for file in files:
                    name = "%s/%s" % (dir, file);
                    if os.access(name, os.R_OK) and os.path.getsize(name) > 0:
                        self.archive_files.append(name)
                    else:
                        self._errorsBatchAdd(name.split("/")[-1])

            self._errorsBatchSend("File access or file zero-size error in <%s>" % dir)

        if len(self.archive_files) == 0:
            raise EmptyArchiveException()

        if (len(self.archive_files) < 1000 * dir_count):
            tmp = []
            for path in self.archive_files:
                file = path.split("/")
                i = int("%s%s" % (file[-2], file[-1]))
                tmp.append(i)

            missed = self.missing_elements(tmp, self.next_folder * 1000, (self.next_folder + dir_count) * 1000 - 1)
            for i in missed:
                i = "%07d" % i
                path = "%04d/%03d" % (int(i[-7:-3]), int(i[-3:]))
                self.archive_files_missed.append(path)

            is_full = 0
            self.logger.info("Archive is not full, but will be created with flag is_full=0")
        else:
            is_full = 1

        self.archive = Archive(
            name = "-".join([archive_name[0], archive_name[-1]]) + '.tar',
            rule_id = self.rule.id,
            is_full = is_full,
            file_count = len(self.archive_files)
        )

    def missing_elements(self, li, start, end):
        return sorted(set(xrange(start, end + 1)).difference(li))

    def getLocalArchivePath(self):
        return "%s/%s/%s/%s" % (ARCHIVE_DIR, self.rule.path, self.archive.name[:1], self.archive.name)

    def getCastorArchiveFile(self):
        return "%s/%s/%s/%s" % (CASTOR_ROOT_DIR, self.rule.path, self.archive.name[:1], self.archive.name)
    def getSocArchiveFile(self):
        return "%s/%s/%s" % (self.rule.path, self.archive.name[:1], self.archive.name)

    def getCastorArchivePath(self):
        return "%s/%s" % (CASTOR_HOST, self.getCastorArchiveFile())
    def getSocArchivePath(self):
        return "%s/%s" % (SOC_ROOT_DIR, self.getSocArchiveFile())

    def generateArchive(self, frames_dir):
        start = datetime.datetime.now()
        file = self.getLocalArchivePath()
        if os.path.exists(file):
            os.remove(file)
        dir = os.path.dirname(file)
        if os.path.isdir(dir) == False:
            os.makedirs(dir)

        self.logger.info("Start create archive %s", file)
        tar = tarfile.open(file, "w:")
        for name in self.archive_files:
            arcname = name[len(frames_dir)+len(self.rule.path)+2:]
            tar.add(name, arcname)

        tar.close()

        end = datetime.datetime.now()
        size = os.path.getsize(file)
        self.logger.info('Archive created, size=%d (%s), time=%d seconds', size, sizeof_fmt(size), total_seconds(end - start))


    def getCrc32(self, path):
        out = self.cmd.check_output([CMD_CKSUM, path])
        return out.split(" ")[0]


    def getAdler32(self, path):
        out = self.cmd.check_output([CMD_XRDADLER32, path])
        return out.split(" ")[0]

    def insertArchiveDB(self, crc32, adler32, file_size, soc_status=None):
        #crc32 = self.getCrc32(self.getLocalArchivePath())
        #adler32 = self.getAdler32(self.getLocalArchivePath())
        #file_size = os.path.getsize(self.getLocalArchivePath())
        self.db.sql(
            "UPDATE FRAMES_BACKUP_RULE SET UPDATED_AT=:update_at WHERE ID=:rule_id",
            update_at=datetime.datetime.utcnow(),
            rule_id=self.rule.id
        )
        self.db.sql(
            "INSERT INTO FRAMES_BACKUP_ARCHIVE \
            (RULE_ID,NAME,FILE_SIZE,FILE_COUNT,IS_FULL,STATUS,CASTOR_STATUS,SOC_STATUS,CRC,ADLER,CREATED_AT,UPDATED_AT) \
            VALUES \
            (:rule_id,:name,:file_size,:file_count,:is_full,:status,:castor_status,:soc_status,:crc,:adler,:created_at,:updated_at)",
            rule_id = self.rule.id,
            name = self.archive.name,
            file_size = file_size,
            file_count = self.archive.file_count,
            is_full = self.archive.is_full,
            status = STATUS_OK,
            castor_status = CASTOR_STATUS_EMPTY,
            soc_status = soc_status,
            crc = crc32,
            adler = adler32,
            created_at = datetime.datetime.utcnow(),
            updated_at = datetime.datetime.utcnow()
        )
        archive_id = self.db.last_insert_id

        # insert missed files
        for name in self.archive_files_missed:
            self.db.sql(
                "INSERT INTO FRAMES_BACKUP_MISSEDFILE \
                (ARCHIVE_ID,NAME,CREATED_AT,UPDATED_AT) \
                VALUES \
                (:archive_id,:name,:created_at,:updated_at)",
                archive_id = archive_id,
                name = name,
                created_at = datetime.datetime.utcnow(),
                updated_at = datetime.datetime.utcnow()
            )

        self.db.sql("COMMIT")


    def chunks(self, l, n):
        for i in xrange(0, len(l), n):
            yield l[i:i+n]

    def updateCastorStatus(self):
        self.db.sql(
            "UPDATE FRAMES_BACKUP_ARCHIVE SET CASTOR_STATUS=:castor_status, UPDATED_AT=:updated_at WHERE ID=:archive_id",
            castor_status = self.castor_status,
            updated_at = datetime.datetime.utcnow(),
            archive_id = self.archive.id
        )
        self.db.sql("COMMIT")

    def updateSocStatus(self):
        self.db.sql(
            "UPDATE FRAMES_BACKUP_ARCHIVE SET SOC_STATUS=:soc_status, UPDATED_AT=:updated_at WHERE ID=:archive_id",
            soc_status = self.soc_status,
            updated_at = datetime.datetime.utcnow(),
            archive_id = self.archive.id
        )
        self.db.sql("COMMIT")

    def castorFileExists(self):
        local_file = self.getLocalArchivePath()
        if os.path.exists(local_file) == False:
            raise ArchiveWithoutLocalFileException(self.archive.id, self.archive.name, local_file)

        self.logger.info("Start check file in CASTOR")

        # create CASTOR directory structure
        castor_file = self.getCastorArchiveFile()
        castor_path = self.getCastorArchivePath()

        # check file exists in CASTOR
        try:
            out = self.cmd.check_output([CMD_NSLS, CMD_NSLS_LS_FLAG, CMD_NSLS_CKSUM_FLAG, castor_file])
            castor_info = out.split()
            castor_size = int(castor_info[4])
            if castor_size != self.archive.size:
                self.logger.warning("File %s exists but wrong size(castor=%d, local=%d), will be removed", castor_file, castor_size, self.archive.size)
                self.cmd.check_call([CMD_NSRM, castor_file])
                return 0

            # add zeroes to begin of sum
            castor_adler32 = castor_info[9].zfill(8)
            if castor_adler32 != self.archive.adler32:
                self.logger.warning("File %s exists but wrong adler32(castor=%s, local=%s), will be removed", castor_file, castor_adler32, self.archive.adler32)
                self.cmd.check_call([CMD_NSRM, castor_file])
                return 0

        except(subprocess.CalledProcessError) as error:
            if error.returncode == 1:
                # file not exists, copy it
                return 0
            else:
                raise UnsupportedReturnCodeException(error.returncode, error)

        return 1

    def socFileExists(self):
        self.logger.info("Start check file in SOC")

        soc_file = self.getSocArchiveFile()
        soc_path = self.getSocArchivePath()

        # create SOC directory structure
        fdir = os.path.dirname(soc_path)
        self.cmd.check_call(['mkdir', '-p', fdir])
        
        # check file exists in SOC
        try:
            out = self.cmd.check_output(['ls', '-la', soc_path])
            soc_info = out.split()
            soc_size = int(soc_info[4])
            if soc_size != self.archive.size:
                self.logger.warning("File %s exists but wrong size(soc=%d, local=%d), will be removed", soc_path, soc_size, self.archive.size)
                self.cmd.check_call(['rm', soc_path])
                return 0

        except(subprocess.CalledProcessError) as error:
            if error.returncode == 2:
                # file not exists, copy it
                return 0
            else:
                raise UnsupportedReturnCodeException(error.returncode, error)

        return 1

    def castorCopyArchive(self):
        start = datetime.datetime.now()
        local_file = self.getLocalArchivePath()
        if os.path.exists(local_file) == False:
            raise ArchiveWithoutLocalFileException(self.archive.id, self.archive.name, local_file)

        self.logger.info("Start copy file to CASTOR")

        # create CASTOR directory structure
        castor_file = self.getCastorArchiveFile()
        castor_path = self.getCastorArchivePath()

        try:
            self.cmd.check_call(
                [CMD_XRDCP, CMD_XRDCP_DESTINATION_FLAG, local_file, castor_path]
            )
        except(subprocess.CalledProcessError) as error:
            raise FailedCopyFileException(error.returncode, error)

        end = datetime.datetime.now()
        self.logger.info('File copied, time=%d seconds', total_seconds(end - start))
        self.castor_status = CASTOR_STATUS_CANBEMIGR


    def socCopyArchive(self):
        start = datetime.datetime.now()
        soc_path = self.getSocArchivePath()
        local_file = self.getLocalArchivePath()
        if os.path.exists(local_file) == True:
            # do copy from local drive
            self.logger.info("Start copy file from local drive")
            try:
                self.cmd.check_call(['cp', local_file, soc_path])
            except(subprocess.CalledProcessError) as error:
                raise FailedCopyFileException(error.returncode, error)

            # remove local file
            os.remove(local_file)
        else:
            # do copy from CASTOR
            self.logger.info("Start copy file from CASTOR")
            castor_path = self.getCastorArchivePath()
            castor_file = self.getCastorArchiveFile()
            try:
                out = self.cmd.check_output([CMD_STAGER_QRY, CMD_STAGER_QRY_FLAG, castor_file])
                castor_status = out.split()[-1]
                if castor_status != CASTOR_STATUS_STAGED:
                    raise WaitingForFileStageException()
                    
                try:
                    self.cmd.check_call([CMD_XRDCP, CMD_XRDCP_SOURCE_FLAG, castor_path, soc_path])
                except(subprocess.CalledProcessError) as error:
                    raise FailedCopyFileException(error.returncode, error)
            except(subprocess.CalledProcessError) as error:
                if error.returncode == 1:
                    # run stager_get to tell to CASTOR fetch data to disk
                    self.cmd.check_call([CMD_STAGER_GET, CMD_STAGER_QRY_FLAG, castor_file])
                    raise FailedStagerGetException(error.returncode, error)
                else:
                    raise FailedStagerQueryException(error.returncode, error)

        end = datetime.datetime.now()
        self.logger.info('File copied, time=%d seconds', total_seconds(end - start))
        self.soc_status = SOC_STATUS_OK


    def castorCheckStatus(self):
        start = datetime.datetime.now()
        self.logger.info("Start check CASTOR stage")

        castor_file = self.getCastorArchiveFile()
        try:
            out = self.cmd.check_output([CMD_STAGER_QRY, CMD_STAGER_QRY_FLAG, castor_file])
            castor_status = out.split()[2]
            if castor_status != CASTOR_STATUS_CANBEMIGR:
                self.castor_status = castor_status
            else:
                raise CastorStatusNotChangedException(castor_file)

        except(subprocess.CalledProcessError) as error:
            raise FailedStagerQueryException(error.returncode, error)

        end = datetime.datetime.now()
        self.logger.info('File checked, time=%d seconds', total_seconds(end - start))


    def listDir(self, dir):
        regex = re.compile("^[0-9]{4}$", re.IGNORECASE)
        if os.path.isdir(dir) == False:
            raise DirectoryNotExistsException(dir)

        dirs = os.listdir(dir)
        res = sorted(filter(regex.search, dirs))
        if len(res) < 1:
            raise WarningScriptException("Empty rule directories <%s>" % dir)

        return res


    def listFile(self, dir):
        regex = re.compile("^[0-9]{3}$", re.IGNORECASE)
        dirs = os.listdir(dir)
        res = sorted(filter(regex.search, dirs))
        if len(res) < 1:
            raise WarningScriptException("Empty directory files <%s>" % dir)

        return res


    def isLastDir(self, fdir):
        # do not check for last dir in create_down node
        if self.mode == 'create_down':
            return False

        fpath = os.path.dirname(fdir)
        fdir = os.path.basename(fdir)
        if (fdir == '9999'):
            return False
        else:
            for i in range(int(fdir)+1, 10000):
                if os.path.isdir("%s/%04d" % (fpath, i)):
                    return False

        return True


    def findArchiveRule(self, archive):
        rules = self.db.sql("SELECT ID,PATH,DIR_COUNT FROM FRAMES_BACKUP_RULE WHERE ID=:rule_id", rule_id = archive.rule_id)
        if rules == []:
            raise ArchiveWithoutRuleException(archive.id, archive.name)

        for RULE_ID,PATH,DIR_COUNT in rules:
            self.rule = Rule(id=RULE_ID, path=PATH, dir_count=DIR_COUNT)


    def _errorsBatchAdd(self, str):
        self._errors.append(str)


    def _errorsBatchSend(self, str):
        if len(self._errors):
            self.logger.error("%s [%s]" % (str, "; ".join(self._errors)))
            self._errors = []


    def stat(self):
        try:
            rules = self.db.sql(
                "SELECT R.ID, R.PATH, SUM(A.FILE_SIZE) FROM FRAMES_BACKUP_ARCHIVE A LEFT JOIN FRAMES_BACKUP_RULE R ON A.RULE_ID=R.ID WHERE R.IS_ACTIVE=:is_active GROUP BY R.ID, R.PATH",
                is_active = 1
            )
            now = datetime.datetime.utcnow().strftime('%s')
            for RULE_ID,PATH,SIZE in rules:
                self.logger.info("Send '%s %s %d' row" % (now, PATH, SIZE))
 
        except(Exception) as error:
            self.logger.critical("python_error='%s' traceback='%s'", error, traceback.format_exc())


    def _findGaps(self, file_list):
        old_number = old_path = old_rule = None
        gaps = []
        for file in file_list:
            path = file.name.split('/')
            rule = '/'.join(path[0:2])
            number = int(''.join(path[2:]))
            if (old_number is not None) and (rule == old_rule and old_number + 1 != number):
                gaps.append([old_path, path])
                
            old_number = number
            old_path = path
            old_rule = rule

        return gaps
        
