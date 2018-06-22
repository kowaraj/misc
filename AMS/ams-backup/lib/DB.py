import re
import ConfigParser
import cx_Oracle

from settings import *

config = ConfigParser.RawConfigParser()
config.read('/etc/oracle-db.conf')
DB_NAME     = config.get(APP_NAME, 'name')
DB_USER     = config.get(APP_NAME, 'user')
DB_PASSWORD = config.get(APP_NAME, 'password')
DB_TNS      = "%s/%s@%s" % (DB_USER, DB_PASSWORD, DB_NAME)


class DB:
    connection = None
    last_insert_id = None
    def __init__(self, logger):
        self.logger = logger

    def getConnection(self):
        try:
            return cx_Oracle.connect(DB_TNS)
        except cx_Oracle.DatabaseError, info:
            self.logger.error("sql_error='%s'", str(info).replace("\n", ""))
            exit(1)


    def sql(self, sql, **kwargs):
        if self.connection == None:
            self.connection = self.getConnection()
            self.cursor = self.connection.cursor()

        last_insert_id = None
        insert = re.compile('^\s*INSERT\s*')
        if insert.match(sql):
            last_insert_id = self.cursor.var(int)
            kwargs['returning_id'] = last_insert_id
            sql = "%s %s" % (sql, " RETURNING ID INTO :returning_id")

        self.cursor.prepare(sql)
        result = self.cursor.execute(None, kwargs)

        commit = re.compile('^\s*(COMMIT|ROLLBACK)')
        if commit.match(sql):
            self.logger.info("sql='%s', args=%s, host=%s", sql, kwargs, self.connection.dsn)
        else:
            edit = re.compile('^\s*(UPDATE |DELETE |INSERT )')
            if edit.match(sql):
                self.logger.info("sql='%s', args=%s, host=%s, rows=%d", sql, kwargs, self.connection.dsn, self.cursor.rowcount)
            else:
                result = self.cursor.fetchall()
                self.logger.info("sql='%s', args=%s, host=%s, rows=%d", sql, kwargs, self.connection.dsn, self.cursor.rowcount)

        if last_insert_id is not None:
            self.last_insert_id = last_insert_id.getvalue()

        return result


    """
    def getRows(self):
        colnames = [i[0] for i in cursor.description]
        for row in cursor:
            yield dict(zip(colnames, row))
    """


