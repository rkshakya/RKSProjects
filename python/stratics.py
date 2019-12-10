import datetime
import time
import hashlib
import requests
import pprint

input_data = {
	"phone" : "787519856",
}

def getToken():
	email = hashlib.md5(b'greg@gcbma.com').hexdigest() # stratics Email
	password = hashlib.md5(b'Gr1234').hexdigest() #stratics password
	r = requests.post('https://api.ivr-platform.com/api/token-auth', 
                  data={'email': email, 'password': password})
	token = r.json()['token']
	return token

def makeRVM(tok, phone):
	retval = ''
	url = ('https://api.ivr-platform.com/api/rvms/{}/ondemand'.format('100446'))
	headers = {
  		'Authorization': 'JWT {}'.format(tok),
  		'Accept': 'application/json',
  		'Content-Type': 'application/json',
	}
	payload = {
	  'delay': 1800, # delay 30 mins
	  'scrub_nat_dnc': True, # Scrub lead against National DNC
	  'respect_schedule': False, # Drop RVM as soon as possible
	  'lead': {
	    'lead_phone': phone
	  }
	}

	r = requests.post(url, headers=headers, json=payload)

	if r.status_code == 201:
	  retval = "RVM Created"
	elif r.status_code == 202:
	  retval = "RVM Accepted" 
	elif r.status_code == 400 or r.status_code == 403:
	  retval = "RVM Forbidden or Error"

	return retval  


tok = getToken()
rt = makeRVM (tok, input_data['phone'])

output = {'message': rt}



