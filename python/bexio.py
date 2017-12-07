# coding=utf-8

import requests
import json
import hashlib

PUBLIC_KEY = "XXXX"
SIGNATURE_KEY = "XXXX"
KEYNAME = "MyKey"
COMPANY_ID = "XXXX"
USER_ID = "1"

url = "https://office.bexio.com/api2.php/" + COMPANY_ID + "/" + USER_ID + "/" + PUBLIC_KEY + "/contact" 
payload = {
        'contact_type_id' : input_data['contactTypeId'],
        'name_1' : input_data['contactName'],
        'owner_id' : input_data['contactOwnerId'],
        'user_id' : input_data['contactUserId'],
        'address' : input_data['contactAddress'],
        'city' : input_data['contactCity'],
        'country_id': input_data['contactCountryId'],
        'mail' : input_data['contactMail'],
        'postcode' : input_data['contactPostcode']
        }

#form signature
strSig = "post" + url + json.dumps(payload) + SIGNATURE_KEY

strFin =  hashlib.md5(strSig).hexdigest()

headers = {'Accept': 'application/json',
    'Signature' : strFin
}
 
r = requests.post(url, data=json.dumps(payload), headers=headers)
data = r.json()

if 'id' in data and data['id'] != '':
    output = {'contactid': data['id']}
else:
    output = {'message':'Contact could not be created', 'err': data}
