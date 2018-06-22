
class Model(object):
    def __init__(self):
        pass

class Rule(Model):
    def __init__(self, id=None, path=None, dir_count=None):
        self.id = id
        self.path = path
        self.dir_count = dir_count

class Archive(Model):
    def __init__(self, id=None, name=None, rule_id=None, is_full=None, size=None, file_count=None, adler32=None):
        self.id = id
        self.name = name
        self.rule_id = rule_id
        self.is_full = is_full
        self.size = size
        self.file_count = file_count
        self.adler32 = adler32

class Tape(Model):
    def __init__(self, id=None, name=None, order=None, status=None):
        self.id = id
        self.name = name
        self.order = order
        self.status = status

class Backup(Model):
    def __init__(self, id=None, name=None, tape_name=None, status=None, gaps=None):
        self.id = id
        self.name = name
        self.tape_name = tape_name
        self.status = status
        self.gaps = []

class BackupFile(Model):
    def __init__(self, id=None, rule_id=None, backup_id=None, name=None, file_size=None, mtime=None):
        self.id = id
        self.rule_id = rule_id
        self.backup_id = backup_id
        self.name = name
        self.file_size = file_size
        self.mtime = mtime
