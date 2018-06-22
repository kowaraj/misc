import fcntl
import argparse
import logging
import logging.handlers

from settings import *
from Cmd import Cmd
from DB import DB

class ScriptException(Exception):
    def __init__(self, error):
        self.error = error
    def __str__(self):
        return self.error.encode("utf-8")

class ErrorScriptException(ScriptException):
    pass

class InfoScriptException(ScriptException):
    pass

class WarningScriptException(ScriptException):
    pass


class Script(object):
    log_size = 1024*1024*10
    log_count = 50
    level = logging.CRITICAL
    modes = []
    args = []
    mode = None
    _errors = []

    def __init__(self, class_name):
        # parse args
        parser = argparse.ArgumentParser()
        parser.add_argument('-d', '--debug', help='Show debug messages', action="store_true")
        parser.add_argument('mode', help='Running modes: %s' % "|".join(self.modes))
        for arg in self.args:
            parser.add_argument(arg['name'], help=arg['help'], nargs=arg['nargs'])

        args = parser.parse_args()
        if args.debug:
            self.level = logging.DEBUG

        if args.mode in self.modes:
            self.mode = args.mode
        else:
            parser.print_help()
            exit(1)

        for arg in self.args:
            if hasattr(args, arg['_name']):
                setattr(self, arg['_name'], getattr(args, arg['_name']))

        self.class_name = class_name
        self.logger = self.getLogger()
        self.cmd = Cmd(self.logger)
        self.db = DB(self.logger)


    def run(self):
        self.logger.info("Script start")
        lock_file = "%s/%s-%s-%s.lock" % (LOCK_DIR, APP_NAME, self.class_name, self.mode)
        fp = open(lock_file, 'w')
        try:
            fcntl.lockf(fp, fcntl.LOCK_EX | fcntl.LOCK_NB)
            self.runMode()
            self.logger.info("Script finish")
        except(ScriptException) as error:
            self.logger.warning(error)
            self.logger.info("Script finish")
        except IOError:
            self.logger.info("Script is locking")


    def getLogger(self):
        # setup logger
        logger = logging.getLogger(APP_NAME)
        logger.setLevel(logging.DEBUG)

        if APP_NAME == "pocc_backup_dev":
            # colorize debug, warnings, errors and criticals
            logging.addLevelName(logging.DEBUG,    "\033[33m%s\033[1;0m" % logging.getLevelName(logging.DEBUG))
            logging.addLevelName(logging.WARNING,  "\033[31m%s\033[1;0m" % logging.getLevelName(logging.WARNING))
            logging.addLevelName(logging.ERROR,    "\033[31m%s\033[1;0m" % logging.getLevelName(logging.ERROR))
            logging.addLevelName(logging.CRITICAL, "\033[31m%s\033[1;0m" % logging.getLevelName(logging.CRITICAL))

        # create file handler
        file_h = logging.handlers.RotatingFileHandler(
            "%s/%s-%s-%s.log" % (LOG_DIR, APP_NAME, self.class_name, self.mode),
            maxBytes=self.log_size,
            backupCount=self.log_count
        )
        file_h.setLevel(logging.DEBUG)

        # create console handler
        console_h = logging.StreamHandler()
        console_h.setLevel(self.level)

        # create email handler
        mail_h = logging.handlers.SMTPHandler('localhost', '%s@ams-backup.cern.ch' % APP_NAME, ['oleg.demakov@cern.ch, andrey.pashnin@cern.ch'], APP_NAME)
        mail_h.setLevel(logging.CRITICAL)

        # create formatter and add it to the handlers
        formatter = logging.Formatter('%(asctime)s -- %(name)s -- pid=%(process)s -- %(levelname)s -- %(message)s')
        file_h.setFormatter(formatter)
        console_h.setFormatter(formatter)
        mail_h.setFormatter(formatter)

        # add the handlers to logger
        logger.addHandler(file_h)
        logger.addHandler(console_h)
        logger.addHandler(mail_h)
        return logger
