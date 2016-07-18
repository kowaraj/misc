import urllib.request

# send a command

#url = "http://cfo-864-as5g02/scpictrl.htm?lang=en"
url = "http://cfo-864-as5g02/scpi_response.txt?lang=en"

values = {'request': 'single', 'cmd': 'Send'}
data = urllib.parse.urlencode(values)
data = data.encode('ascii') # data should be bytes
headers = {'Content-Length': 23}

request = urllib.request.Request(url, data, headers)

response = urllib.request.urlopen(request)
print('response.status = '+str(response.status))

the_page = response.read()

