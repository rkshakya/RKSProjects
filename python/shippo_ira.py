import requests
import json

# script that creates shipping label using Shippo API and copies it over to dropbox folder

input_data = {
    

}


url = 'https://api.goshippo.com/transactions/'
payload = {
        "shipment": {
            "address_from": {
                "name": input_data['sendername'],
                "company" : input_data['sendercompany'],
                "street1": input_data['senderstreet'],
                "city": input_data['sendercity'],
                "state": input_data['senderstate'],
                "zip": input_data['senderzip'],
                "country": input_data['sendercountry'],
                "phone": input_data['senderphone'],
                "email": input_data['senderemail']
            },
            "address_to": {
                "name": input_data['recipientname'],
                "company" : input_data['recipientcompany'],
                "street1": input_data['recipientstreet'],
                "city": input_data['recipientcity'],
                "state": input_data['recipientstate'],
                "zip": input_data['recipientzip'],
                "country": input_data['recipientcountry'],
                "phone": input_data['recipientphone'],
                "email": input_data['recipientemail']
            },
            "parcels": [{
                "length": input_data['parcellength'],
                "width": input_data['parcelwidth'],
                "height": input_data['parcelheight'],
                "distance_unit": input_data['parceldistanceunit'],
                "weight": input_data['parcelweight'],
                "mass_unit": input_data['parcelmassunit']
            }]
        },
        "carrier_account": input_data['CARRIER_ACCOUNT_ID'],
        "servicelevel_token": input_data['SERVICE_LEVEL_TOKEN']
        }

if 'senderstreet2'  in input_data:
    payload['shipment']['address_from']['street2'] = input_data['senderstreet2']

if 'recipientstreet2' in input_data:
    payload['shipment']['address_to']['street2'] = input_data['recipientstreet2']

if 'dryiceflag' in input_data and input_data['dryiceflag'] == 'Yes':
    payload['shipment']['extra'] = {"dry_ice": {"contains_dry_ice": True, "weight": input_data['dryiceweight']}}

headers = {'Content-type': 'application/json',
	'Authorization': 'ShippoToken ' + input_data['SHIPPO_API_TOKEN']
}
 
r = requests.post(url, data=json.dumps(payload), headers=headers)
data = r.json()

if 'label_url' in data and data['label_url'] != '':
    urldrop = input_data['DROPBOX_FOLDER'] + '/' + input_data['ordernumber'] + '.pdf'
    payloaddrop = { 'url' : data['label_url']}
    headersdrop = {
        'Authorization': 'Bearer ' + input_data['DROPBOX_ACCESS_TOKEN']
    }
 
    rdrop = requests.post(urldrop, data=payloaddrop, headers=headersdrop)
    datadrop = rdrop.json()
    output = {'jobid': datadrop}
else:
    output = {'message':'Label was not created', 'err': data}
