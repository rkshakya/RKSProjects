import requests
import re
from currency import *
import openerpconfig
from xmlcall import Xmlcall
import datetime

#script to scrape currency exchange rate from NRB website and populate into openERP
#author : Ravi K Shakya

#scrape forex rates
resp = requests.get('http://www.nrb.org.np')
musd = re.search(r'USD.*?([\d]+\.[\d]+).*?([\d]+\.[\d]+)', resp.text, re.S)
rusd = float(musd.group(2))

mgbp = re.search(r'GBP.*?([\d]+\.[\d]+).*?([\d]+\.[\d]+)', resp.text, re.S)
rgbp = float(mgbp.group(2))

meur = re.search(r'EUR.*?([\d]+\.[\d]+).*?([\d]+\.[\d]+)', resp.text, re.S)
reur = float(meur.group(2))

#out rates into dict
ratemap = {}
ratemap['USD'] = 1/rusd
ratemap['GBP'] = 1/rgbp
ratemap['EUR'] = 1/reur

today = datetime.date.today()

# now populate into openERP
for key, value in ratemap.items():
    cur = Currency()
    cur.setRate(value)
    cur.setID(key)
    cur.setName(str(today))
    
    #now make rpc call
    model = 'res.currency.rate'
    call = Xmlcall(openerpconfig.rpcurl, openerpconfig.rpcuser, openerpconfig.rpcpwd, openerpconfig.rpcdb)
    cid = call.execute_action(model, 'create', cur.__dict__)

    #print obj id and name
    print cid
    print key




