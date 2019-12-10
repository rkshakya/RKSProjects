import json
import urllib2
import urllib
from urllib2 import URLError, HTTPError
from datetime import datetime, timedelta

url = 'https://api.servicemonster.net/v1/accounts'
posturl = 'https://hooks.zapier.com/hooks/catch/1878984/ajug31/'

headerspatch = {
			'Content-type': 'application/json',
			'Authorization': 'Basic bTZPcTJsQkxqOkFXe'
		}

#find todays date
yday = (datetime.now()).strftime('%m/%d/%Y')
# yday = '6/11/2018'


req = urllib2.Request(url + '?wField=timeStamp&wValue='+ yday + '&wOperator=gt', headers=headerspatch)
response = urllib2.urlopen(req)
# print response.read()
js = json.loads(response.read())

# print js	

retMap = {}
for record in js['items']:
	print record
		#collect data and post to url
	try:
		dat = record
		dat['querystring__sec'] = 'ullu'
		data = urllib.urlencode(dat)
		reqpost = urllib2.Request(posturl, data)
		respost = urllib2.urlopen(reqpost)
		jspost = json.loads(respost.read())
		retMap[record['row_number']] = jspost		
	except HTTPError as e:
		print e.read()
		retMap["Error"] = "Error pls chk zapier task history"
		
# print retMap
output ={'out': retMap}
