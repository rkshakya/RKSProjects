import requests
import json

# input_data = {
#     "saleid" : "c265c7b1-ec4e-4194-a9ef-aeb4bf942d77"
# }

SALE_URL = 'https://inventory.dearsystems.com/ExternalApi/v2/sale'
SALE_ORDER_URL = 'https://inventory.dearsystems.com/ExternalApi/v2/sale/order'
FULFIL_URL = 'https://inventory.dearsystems.com/ExternalApi/v2/sale/fulfilment'

ACCOUNT_ID = 'yyyyyy'
APPLICATION_KEY = 'xxxxx'

headers = {
	'Content-Type':'application/json',
    'api-auth-accountid':'%s' % ACCOUNT_ID,
	'api-auth-applicationkey':'%s' % APPLICATION_KEY
}

#undo sale order
paramsDel = {
    'ID' : input_data['saleid'],
    'Void': False
}

respdel = requests.delete(SALE_URL,  headers=headers, params = paramsDel)
recsdel = respdel.json()
# print recsdel

#get sale order
params = {
    'SaleID' : input_data['saleid']
}


resp = requests.get(SALE_ORDER_URL,  headers=headers, params = params)
recs = resp.json()

# print recs

recs['Status'] = 'AUTHORISED'
recs['AutoPickPackShipMode']= 'AUTOPICK'
# print recs

#update order
r = requests.post(SALE_ORDER_URL, data=json.dumps(recs), headers=headers)
data = r.json()

print data

output = {"pickstatus": data}
