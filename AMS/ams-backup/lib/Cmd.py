import subprocess

class Cmd:
    def __init__(self, logger):
        self.logger = logger

    def check_output(self, *popenargs, **kwargs):
        self.logger.info("CMD='%s'", " ".join(popenargs[0]))
        return subprocess.check_output(*popenargs, **kwargs)

    def check_call(self, *popenargs, **kwargs):
        self.logger.info("CMD='%s'", " ".join(popenargs[0]))
        return subprocess.check_call(*popenargs, **kwargs)

