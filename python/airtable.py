import json
import posixpath 
import requests

API_KEY = 'keyDk'
API_URL = 'https://api.airtable.com/v%s/'
API_VERSION = '0'

airtable_url = API_URL % API_VERSION

# input_data = {'sku' : 'VD-KIDS-A', 'qty' : '13'}

skusplit = input_data['sku'].split(',')
qtysplit = input_data['qty'].split(',')

retMap = {}

# retString = '<html><body>Records from food table<table border = "1"><tr><th>Name</th><th>Notes</th><th>Attachment</th><th>Date</th></tr>'
for sku, qty in zip(skusplit, qtysplit):
	#for FOOD base
	APPID_food = 'appIR0Tuet'
	TABLE_food = 'Mermaid Pillows'
	base_url = posixpath.join(airtable_url, APPID_food)
	fin_url = posixpath.join(base_url, TABLE_food)
	headers = {'Authorization': 'Bearer %s' % API_KEY}
	params = {
		'filterByFormula' : "{SKU} = '%s'" % sku
	}
	resp = requests.get(fin_url,  headers=headers, params = params)
	recs = resp.json()
	try:
		print recs
		print recs['records'][0]['id']

		#update qty
		RECORD_ID = recs['records'][0]['id']
		ORIG_QTY = recs['records'][0]['fields']['Quantity']
		print ORIG_QTY
		CURR_QTY = int(ORIG_QTY) - int(qty)
		udt_url = posixpath.join(fin_url, RECORD_ID)
		headerspatch = {
			'Content-type': 'application/json',
			'Authorization': 'Bearer %s' % API_KEY
		}

		data = '{"fields" : { "Quantity": %s } }' % str(CURR_QTY)

		respatch = requests.patch(udt_url,  headers=headerspatch, data = data)
		recspatch = respatch.json()
		print recspatch
		retMap[recspatch['fields']['SKU']] = recspatch['fields']['Quantity']
	except:
		retMap["Error"] = "Error pls chk zapier task history"	
return retMap	
