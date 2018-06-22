import sys
import datetime
import sh

LOCP='/dat0/frames_backup/archive/SCIBPB/RT/3/' # local path
CASP='/castor/cern.ch/user/a/ams/Data/FRAMES/SCIBPB/RT/3/' # castor path

NUM_OF_FILES_TO_KEEP = 3
USEP_LIMIT = 30 # %

class Logger:
        def __init__(self):
                pass
        def info(self, s):
                print 'log::info  : ', s
		sys.stdout.flush()
        def error(self, s):
                print 'log::error : ', s 
		sys.stdout.flush()
        def dbg(self, s):
                print 'log::dbg   : ', s
		sys.stdout.flush()


# Return a tuple (errcode, msg)
# Sh's exceptions are caught

def localDelete():
        logger =  Logger()
        logger.info("Timestamp: "+  str(datetime.datetime.now()) )


	try:
                # Check disk space '/dat0'

                logger.info("Check usage with 'df' command. Extract 'Use%'.")
		usep = int(sh.awk(sh.tail(sh.df("/dat0"), "-n1"), '{print $5}').rstrip('%\n'))
                logger.dbg(usep)
                if usep < USEP_LIMIT:
                        logger.info("Enough space on disk (threshold is "+USEP_LIMIT+"%).")
                        return (0, "Enough space on disk") # ok

                # Keep at least 3 most recent archives locally

                logger.info("Check number of archives on disk. Keep some.")
                local_archives = sh.ls(LOCP).split()
                if len(local_archives) <= NUM_OF_FILES_TO_KEEP:
                        return (-1, "Less then 3 local archives found. Running out of space? Look for the reason elsewhere.")
                

                # Delete the rest, if copied on CASTOR
                
                logger.info("Get name of oldest archive.")
                floc = sh.ls(LOCP).split()[0] # 3262-3262.tar = oldest local archive
                logger.dbg(floc)
                floc_size = int(sh.awk(sh.ls('-l', LOCP+floc), '{print $5}'))
                logger.dbg(floc_size)

                logger.info("Check this file on CASTOR.")
                fcas = sh.awk(sh.nsls('-l', CASP+floc), '{print $9}').split('/')[-1].rstrip('\n')
                logger.dbg(fcas)
                fcas_size = int(sh.awk(sh.nsls('-l', CASP+floc), '{print $5}'))
                logger.dbg(fcas_size)

                logger.info("Compare filename and size.")
                if fcas != floc:
                        return (-1, "Wrong filename on CASTOR")
                if fcas_size != floc_size:
                        return (-1, "File size doesn't match")
                
                logger.info("Copy on CASTOR exists and valid. Local one can be deleted.")
		archive_to_delete = LOCP+floc
                logger.dbg("Deleting: "+archive_to_delete+" ....")
		sh.rm(archive_to_delete)
                logger.dbg("Done.")
			
                # Check disk space '/dat0'

                logger.info("Check usage with 'df' command. Extract 'Use%'.")
		usep = int(sh.awk(sh.tail(sh.df("/dat0"), "-n1"), '{print $5}').rstrip('%\n'))
                logger.info("Free space on disk: "+str(usep)+"%")

                return (0, "Done")
                
	except sh.ErrorReturnCode_2:
		logger.error("something went wrong")
                return (-1, "sh.ErrorReturnCode_2")




(errcode, errmsg) = localDelete()
print "Exit: code = ", errcode, ", message = ", errmsg

