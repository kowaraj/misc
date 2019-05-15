import subprocess
from subprocess import PIPE, STDOUT
from time import sleep

fn="rsa_key_captured.jpeg"
def capture_image():
	cmd="v4l2-ctl -d1 -c focus_absolute=120; v4l2-ctl -d1 -l"
	p = subprocess.Popen(cmd, shell=True, stdin=PIPE, stdout=PIPE, stderr=PIPE)
	output, err = p.communicate()
	print output, err

	cmd="v4l2-ctl -d1 -c focus_absolute=100; v4l2-ctl -d1 -l"
	p = subprocess.Popen(cmd, shell=True, stdin=PIPE, stdout=PIPE, stderr=PIPE)
	output, err = p.communicate()
	print output, err

	cmd="ffmpeg -f video4linux2 -i /dev/video1 -vframes 1 " + fn
	p = subprocess.Popen(cmd, shell=True, stdin=PIPE, stdout=PIPE, stderr=PIPE)
	output, err = p.communicate()
	print output, err

while(True):
	capture_image()
	sleep(10)
	print "!captured"
