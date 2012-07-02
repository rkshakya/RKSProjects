import MySQLdb as mdb
import sys
from lead import *
import openerpconfig
from xmlcall import Xmlcall
        
file_object = open(openerpconfig.mappingfile)
mapping = dict()
while 1:
    line = file_object.readline( )
    if not line: break
    #split line on comma, load into dictionary with source fields as keys
    parts = line.split(',')
    if parts[1] != '':
        mapping[parts[1].strip().replace("\"", "")] = parts[0].strip().replace("\"", "")
file_object.close( )
#display diction
#print mapping

#connect to mysql tables and fetch recs
try:
    con = mdb.connect(openerpconfig.dbserver, openerpconfig.dbuser, openerpconfig.dbpass, openerpconfig.db)
    
    with con:    
        cur = con.cursor(mdb.cursors.DictCursor)
        cur.execute("SELECT CONCAT_WS(' ', `First Name`, `Last Name`) AS Name ,Email, CONCAT_WS(' ', `Lead No`, `Website`, `Description`,  `Industry` ) AS Description, `Lead Status`  , `Assigned To`, `Lead Source` , `Title`, `Street`, `PO Box`, `Postal Code`, `State`, `City`, `Country`, `Phone`, `Fax`, `Mobile`, `Company`, `Other Email` FROM `Leads`")
    
        rows = cur.fetchall()
    
        for row in rows:
            print "%s %s" % (row["Name"], row["Email"])
            #instantiate lead objs
            l = Lead()
            #populate field values - read mappings from diction
            l.setName(row["Name"])
            l.setActive()
            l.setEmailfrom(row["Email"])
            l.setDescription(row["Description"])
            l.setStage(row["Lead Status"])
            l.setUser(row["Assigned To"])
            l.setReferred(row["Lead Source"])
            l.setFunction(row["Title"])
            l.setStreet(row["Street"])
            l.setStreet2(row["PO Box"])
            l.setCity(row["City"])
            l.setEmail(row["Other Email"])
            l.setPhone(row["Phone"])
            l.setFax(row["Fax"])
            l.setMobile(row["Mobile"])
            l.setIscustomeradd()
            l.setCompanyid(row["Company"])
            
            l.setcountry_id(row["Country"])
            
           
            #create objects via xml-rpc
            model = 'crm.lead'
            call = Xmlcall(openerpconfig.rpcurl, openerpconfig.rpcuser, openerpconfig.rpcpwd, openerpconfig.rpcdb)
            leadid = call.execute_action(model, 'create', l.__dict__)

            #print obj id and name
            print leadid
            print row["Name"]
            
            
except mdb.Error, e:
    print "Error %d: %s" % (e.args[0],e.args[1])
    sys.exit(1)    
finally:            
    if con:    
        con.close()            