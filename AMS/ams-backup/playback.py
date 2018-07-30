#!/usr/bin/env python

import os
import sys

sys.path.insert(0, "%s/%s" % (os.path.dirname(os.path.realpath(__file__)), "lib"))
sys.path.insert(0, os.path.dirname(os.path.realpath(__file__+'../../')))
from Playback import PlaybackScript

script = PlaybackScript()
script.run()