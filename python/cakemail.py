import json
import requests
import collections

url = 'https://api.wbsrvc.com/List/SubscribeEmail/'
payload = {'user_key': 'XXXXX', 
	'list_id': 12346, 
	'email':'tut@jutaa.com',
	'firstName' : 'Jhahj',
	'lastName' :'ashjdsj'
}

headers = {'apikey': 'XXXX'}
 
r = requests.post(url, data=payload, headers=headers)
print r.json()
