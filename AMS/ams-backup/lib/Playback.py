import re
import sys
import time
import datetime
import argparse
import traceback
import logging
import logging.handlers
import cx_Oracle

from datetime import timedelta

from Script import Script, InfoScriptException, WarningScriptException, ErrorScriptException
from settings import *

CMD_FIND = '/usr/bin/find'
CMD_CHD_DISP = '/tmp_mnt/pcposp0_pocchome/common/bin/chd_disp'
#CMD_CHD_DISP = './chd_disp'
CMD_ANSIFILTER = '/usr/bin/ansifilter'
CMD_TAIL = '/usr/bin/tail'
CMD_HEAD = '/usr/bin/head'
CMD_SED = '/usr/bin/sed'

class PlaybackScript(Script):

    modes = ['process_interval']
    args = [
        {
            'name':'--gmt-day',
            'help':"GMT day in format 2016-017",
            'nargs':1,
            '_name':'gmt_day',
        }
    ]
    gmt_day = None

    def __init__(self):
        super(PlaybackScript, self).__init__(self.__class__.__name__.lower())

    def runMode(self):
        if self.mode == "process_interval":
            self.processInterval()

    def processInterval(self):
        try:
            repeat = False
            if getattr(self, 'gmt_day') and len(self.gmt_day):
                gmt_day = self.gmt_day[0]
                try:
                    day = time.strptime(gmt_day, '%Y-%j')
                except ValueError:
                    print "ERROR: GMT day format error"
                    return 1
                day = datetime.date.fromtimestamp(time.mktime(day))
                repeat = True
            else:
                # find last added interval
                day = self._getNextDay()

            # insert log row
            self._insertLogAndIntervals(day, repeat)

            # commit
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

    def _createCHDFile(self, start, end):
        tmp = datetime.datetime.fromtimestamp(start)
        fname = "%s/%s/%d-%d.log" % (CHD_LOG_DIR, tmp.strftime("%Y/%j"), start, end)
        start = start - 1 * 60
        end = end + 1 * 60
        dname = os.path.dirname(fname)
        # create dir, if mot exists
        if not os.path.exists(dname):
            os.mkdir(dname)

        start_time = datetime.datetime.fromtimestamp(start)
        end_time = datetime.datetime.fromtimestamp(end)
	#self.logger.info('START_TIME: %s', start_time)
	#self.logger.info('END_TIME  : %s', end_time)

        # runs are always ~23 minutes, so we should find directories
        # with this updated date and go throught it by find command
        dirs_for_find = []
        for dir in os.listdir(CHD_FILES_DIR):
            fulldir = "%s%s" % (CHD_FILES_DIR, dir)
            statinfo = os.stat(fulldir)
            mtime = datetime.datetime.fromtimestamp(statinfo.st_mtime)
            #self.logger.info('M_TIME     : %s', mtime)
            if mtime.date() >= start_time.date() and mtime.date() <= end_time.date():
                dirs_for_find.append(fulldir)

        if dirs_for_find == []:
            raise ErrorScriptException("Could not find dir for scan CHD in <%s> for <%s>" % (CHD_FILES_DIR, mtime.date()))

        dir_id = int(dirs_for_find[0].split('/')[-1])

        # add prev dir if exists
        prev_dir_name = "%s%d" % (CHD_FILES_DIR, dir_id-1)
        if os.path.exists(prev_dir_name):
            dirs_for_find.append(prev_dir_name)

        # add next dir if exists
        next_dir_name = "%s%d" % (CHD_FILES_DIR, dir_id+1)
        if os.path.exists(next_dir_name):
            dirs_for_find.append(next_dir_name)

        # find /Data/CHD/HKHR/RT/ -type f -newermt 2015-12-10 ! -newermt 2015-12-11
        out = self.cmd.check_output([CMD_FIND] + dirs_for_find + [
            '-type', 'f',
            '-newermt',
            start_time.strftime("%Y-%m-%d %H:%M:%S"),
            '!',
            '-newermt',
            end_time.strftime("%Y-%m-%d %H:%M:%S")
        ])
        if out == '':
            self.logger.warning("There are no files for CHD")
        else:
            out = out.strip().split('\n')
            s_number = 9999999
            e_number = 0
            for file in out:
                number = int(''.join(file.split('/')[-2:]))
                s_number = min(s_number, number)
                e_number = max(e_number, number)

            # also include 1 minute from the begining and 2 from the end
            # of interval for better CHD cover
            s_number -= 1
            e_number += 2
            #  /pocchome/common/bin/chd_disp --path /Data/CHD/HKHR/RT/ -f 1837743 -l 1837999
            out = self.cmd.check_output([
                CMD_CHD_DISP,
                '--path',
                CHD_FILES_DIR,
                '-f',
                str(s_number),
                '-l',
                str(e_number),
            ])

        fh = open(fname, 'w+')
        fh.write(out)
        fh.close()

        # convert file to html
        html = "%s.html" % fname
        # ansifilter -i /tmp/CHD/2015/348/1450105432-1450106776.log
        # -o /tmp/CHD/2015/348/1450105432-1450106776.log.html --html
        self.cmd.check_call([
            CMD_ANSIFILTER,
            '-i',
            fname,
            '-o',
            html,
            '--html',
        ])
        # remove first 7 lines with <html><body> shit
        # tail -n +4 t.txt
        out = self.cmd.check_output([
            CMD_TAIL,
            '-n',
            '+8',
            html,
        ])
        fh = open(html, 'w+')
        fh.write(out)
        fh.close()

        # remove last 3 lines with <html><body> shit
        # tail -n +4 t.txt
        out = self.cmd.check_output([
            CMD_HEAD,
            '-n',
            '-3',
            html,
        ])
        fh = open(html, 'w+')
        fh.write(out)
        fh.close()

        # decrease font-size with sed
        # sed -i 's/:10pt/:8pt/g'
        self.cmd.check_call([
            CMD_SED,
            '-i',
            's/:10pt/:9pt/g',
            html,
        ])

        self.logger.info("CHD file created <%s>" % fname)

    def _insertLogAndIntervals(self, day, repeat):
        
	self.logger.info("DAY: %s", day)
	if repeat == False:
            self.db.sql(
                "INSERT INTO PLAYBACK_LOG (DAY,STATUS,CREATED_AT,UPDATED_AT) VALUES (:day, 'NEW', :now, :now)",
                day=day,
                now=datetime.datetime.utcnow()
            )
            log_id = self.db.last_insert_id
        else:
            log_id = self.db.sql("SELECT ID FROM PLAYBACK_LOG WHERE DAY=:day", day=day)
            log_id = log_id[0][0]

        # open incomplete file
        intervals = self._loadIncomplete(day)
        if intervals == None:
            raise WarningScriptException("No incomplete file, let's wait")

        # insert IncompleteInterval rows from incomplete file
        for row in intervals:
            if row == '': continue
            row = row.replace('(', '( ')
            interval = row.split("\n")
            line1 = interval[0].split()
            line3 = interval[2].split()
            #self.logger.info("LINE1: %s", line1)
            #self.logger.info("LINE3: %s", line3)
            #print line1
            #print line3
            #1439555009  1439555008  1439556371  SCI       1 491036  491033       3( 0.00%)   0:04
            #1439560522  1439560522  1439560524  CAL       1 494     494               0   2:28
            if len(line1) == 10:
                lost_events = int(line1[7].replace('(', ''))
                lost_events_percent = float(line1[8].replace('%)', ''))
                time_to_next_run = self._getSecondsFromTime(line1[9])
            elif len(line1) == 9:
                lost_events = int(line1[7])
                lost_events_percent = 0
                time_to_next_run = self._getSecondsFromTime(line1[8])
            else:
                raise WarningScriptException("Can not parse row <%s>" % row)

            run = int(line1[0])
            fe_number = int(line1[4])
            if self._insert_interval(repeat, run, fe_number, lost_events_percent, time_to_next_run):
                self.db.sql(
                    "INSERT INTO PLAYBACK_INCOMPLETEINTERVAL \
                    (LOG_ID, STATUS, RUN, FE_TIME, LE_TIME, STREAM_TYPE, FE_NUMBER, LE_NUMBER,\
                    COUNT_EVENTS, LOST_EVENTS, LOST_EVENTS_PERCENT, TIME_TO_NEXT_RUN, FP_TIME, LP_TIME, \
                    CREATED_AT, UPDATED_AT) \
                    VALUES \
                    (:log_id, :status, :run, :fe_time, :le_time, :stream_type, :fe_number, :le_number, \
                    :count_events, :lost_events, :lost_events_percent, :time_to_next_run, :fp_time, :lp_time, \
                    :now, :now)",
                    log_id=log_id,
                    status='NEW',
                    run=run ,
                    fe_time=datetime.datetime.fromtimestamp(int(line1[1])),
                    le_time=datetime.datetime.fromtimestamp(int(line1[2])),
                    stream_type=line1[3],
                    fe_number=fe_number,
                    le_number=int(line1[5]),
                    count_events=int(line1[6]),
                    lost_events=lost_events,
                    lost_events_percent=lost_events_percent,
                    time_to_next_run=time_to_next_run,
                    fp_time=datetime.datetime.fromtimestamp(int(line3[1])),
                    lp_time=datetime.datetime.fromtimestamp(int(line3[3])),
                    now=datetime.datetime.utcnow()
                )

            # create chd file for interval
            if repeat == False:
                self._createCHDFile( int(line1[1]),  int(line1[2]) )

    def _getSecondsFromTime(self, s):
        s = s.split(':')
        return (int(s[0]) * 60) + int(s[1])

    def _getNextDay(self):
        log = self.db.sql(
            "SELECT ID,DAY FROM (SELECT ID,DAY FROM PLAYBACK_LOG ORDER BY DAY DESC) WHERE ROWNUM=1"
        )
        if log == []:
            day = datetime.date(2015, 12, 10)
        else:
            id, day = log[0]
            day = day + timedelta(days=1)
            day = day.date()

        self.logger.info("Process %s day", day)
        yesterday = datetime.date.today() - timedelta(days=1)
        if day > yesterday:
            raise InfoScriptException("Next day is today, skip it")

        return day
        
    def _loadIncomplete(self, day):
        # /afs/cern.ch/ams/Offline/RawFileInfo/Incomplete/incomplete.2015-12-15
        fname = day.strftime("%s")
        fname = "%s/incomplete.%s" % (RAW_INCOMPLETE_DIR, day.strftime('%Y-%m-%d'))
        self.logger.info("Read file <%s>", fname)
        if os.path.isfile(fname) == False:
            return None
        with open(fname) as f:
            content = f.read()
            return content.split("\n\n")

    def need_playback(self, fe_number, lost_events_percent, time_to_next_run):
        res = False
        if fe_number != 1:
            res = True
        if lost_events_percent >= 1.:
            res = True
        if time_to_next_run >= 60:
            res = True
        return res

    def _insert_interval(self, repeat, run, fe_number, lost_events_percent, time_to_next_run):
        # always insert row in non-repeat mode
        if repeat == False:
            return True
        # in repeat mode always insert row for played back interval, once
        playback = self.db.sql(
            "SELECT PLAYBACK FROM PLAYBACK_INCOMPLETEINTERVAL WHERE RUN=:run ORDER BY CREATED_AT",
            run=run
        )
        if len(playback) == 1 and playback[0][0] == 'YES':
            return True
        else:
            return False
