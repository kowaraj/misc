#!/usr/bin/env python

import os
import sys

sys.path.insert(0, "%s/%s" % (os.path.dirname(os.path.realpath(__file__)), "lib"))

from Backup import BackupScript

script = BackupScript()
script.run()
