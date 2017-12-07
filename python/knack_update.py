import requests
import json
from requests.utils import quote
import datetime


KNACK_APP_ID = 'XXXXX'
KNACK_API_KEY = 'XXXX'

headers = {'X-Knack-Application-Id': KNACK_APP_ID,
	'X-Knack-REST-API-KEY': KNACK_API_KEY
}

filters = {
  'match': 'or',
  'rules': [
             {
               'field':'field_226',
               'operator':'is',
               'value': input_data['title']
             }
           ]
};
 
url = 'https://api.knack.com/v1/objects/object_2/records'
url += '?filters=' + quote(json.dumps(filters)); 

r = requests.get( url ,  headers=headers)   

if r.status_code == 200:
	data = r.json()
	put_url = 'https://api.knack.com/v1/objects/object_2/records/'

	if 'total_records' in data and data['total_records'] >= 1:
		put_url += data['records'][0]['id']

	headers['content-type']	= 'application/json'

	payload = {
		"field_32": 'Completed & Shipped'
	}

	#update label object
	rput = requests.put(put_url, data=json.dumps(payload), headers=headers)

	if rput.status_code == 200:
		dataput = rput.json()
		# print 'updated record ' + dataput['id']
		output = {'message': 'UPDATED', 'id' : dataput['id']}
		
		# print output	
	else:
		# print 'ERROR updating record ' + data['records'][0]['id']
		output = {'message': 'FAILED', 'id' : data['records'][0]['id']} 

else:
	# print 'Unable to get object for tracking number ' + input_data['title']
	output = {'message': 'FETCHFAILED', 'title' : input_data['title']}
