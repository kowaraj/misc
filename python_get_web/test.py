import urllib.request

response  = urllib.request.urlopen("http://cfo-864-as5g02/crt_print.png")
image = response.read()
f = open('test01.png', 'wb')
f.write(image)
f.close()

