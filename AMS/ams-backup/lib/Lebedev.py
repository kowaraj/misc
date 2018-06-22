import re
import sys
import time
import glob # to avoid run command with shell=True argument
import datetime
import argparse
import traceback
import logging
import logging.handlers
import cx_Oracle

from datetime import timedelta

from Script import Script, InfoScriptException, WarningScriptException, ErrorScriptException
from settings import *

GPS_VECTOR_SOURCE = '/tmp_mnt/pcposp0_pocchome/lebedev/GPS_WGS84_Vectors_%d*.csv'
#GPS_VECTOR_SOURCE = '%d/*.csv'
GPS_VECTOR_DESTINATION_OFFLINE = '/afs/cern.ch/ams/Offline/AMSDataDir/altec/GPS/'
GPS_VECTOR_DESTINATION_BACKUP = '/dat0/frames_backup/GPS/%d/'
CMD_RSYNC = '/usr/bin/rsync'

class LebedevScript(Script):

    modes = ['gps']

    def __init__(self):
        super(LebedevScript, self).__init__(self.__class__.__name__.lower())

    def runMode(self):
        if self.mode == "gps":
            self.processGPS()

    def processGPS(self):
        try:
            # copy files to
            # /afs/cern.ch/ams/Offline/AMSDataDir/altec/GPS/ dir
            now = datetime.datetime.now()
            files = glob.glob(GPS_VECTOR_SOURCE % now.year)
            command = []
            command.append(CMD_RSYNC);
            command.extend(files);
            command.append(GPS_VECTOR_DESTINATION_OFFLINE);
            self.cmd.check_call(command)

            # copy files to ams-backup@/dat0/frames_backup/GPS/2016/
            command = []
            command.append(CMD_RSYNC);
            command.extend(files);
            command.append(GPS_VECTOR_DESTINATION_BACKUP % now.year);
            self.cmd.check_call(command)
            
        except(InfoScriptException) as error:
            self.db.logger.info(error)

        except(WarningScriptException) as error:
            self.logger.warning(error)

        except(Exception) as error:
            self.db.sql("ROLLBACK")
            self.logger.critical("python_error='%s' traceback='%s'", error, traceback.format_exc())
