from email.mime.image import MIMEImage
from email.mime.multipart import MIMEMultipart

import smtplib

from email.mime.text import MIMEText

import subprocess
from subprocess import PIPE, STDOUT


fn="rsa_key_captured.jpeg"

def send_email():

	msg = MIMEMultipart()

        me = 'apashnin@cern.ch'
        you = 'apashnin@cern.ch' # 41754111323@mail2sms.cern.ch'
        msg['Subject'] = 'RSA Key value captured'
        msg['From'] = me
        msg['To'] = you

    	fp = open(fn, 'rb')
    	img = MIMEImage(fp.read(), _subtype="jpeg")
    	fp.close()
    	msg.attach(img)
        
        s = smtplib.SMTP('cernmx.cern.ch:25')
        s.sendmail(me, [you], msg.as_string())
        print 'sent'
        s.quit()


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


capture_image()
send_email()
