import requests
import json
import MySQLdb as mdb

#api key
apkey = ''
#mysql db information
host = ''
port = 3306
db = ''
uname = ''
pwd = ''

payload = {'api_key' : apkey}

#get company info from MySQL table
#populate into MySQL table 
try:
    con = mdb.connect(host, uname, pwd, db)
    
    with con:    
        cur = con.cursor(mdb.cursors.DictCursor)
        cur.execute("SELECT `id`, `nam`, `permalink` FROM `cb_companies` WHERE `is_processed` = 0 order by `id` LIMIT 0, 100")
    
        rows = cur.fetchall()
        
        inscon = mdb.connect(host, uname, pwd, db)
        inscursor = inscon.cursor()
        
        inscon1 = mdb.connect(host, uname, pwd, db)
        inscursor1 = inscon1.cursor()
    
        for row in rows:
            #make api call to get fund round info
            #http://api.crunchbase.com/v/1/company/ando-media.js?api_key=kx4j4jd98z5jvvrbw7t4b5x9
            url = "http://api.crunchbase.com/v/1/company/" + row["permalink"] + ".js"
            print "Getting data for company : %s " % row["nam"]
            #print url
            try:
                resp = requests.get(url, params=payload)
                funddata = json.loads(resp.content)
            except requests.exceptions.RequestException, ex:
                print "Error %d: %s" % (ex.args[0],ex.args[1])
                print "Fetch information failed for company : %s " % row["nam"]
                sys.exit(1)   

            #populate data into tables  
            if funddata['funding_rounds'] and len(funddata['funding_rounds']) > 0:
                for f in funddata['funding_rounds'] :
                    id = f['id']
                    if f['round_code']:
                        code = f['round_code']
                    else:
                        code = "None"
                        
                    if f['source_description']:
                        sourcedesc = f['source_description']
                    else:
                        sourcedesc = "None"    
                        
                    if f['raised_amount']:
                        amount = f['raised_amount']
                    else:
                        amount = 0.0     
                        
                    if f['raised_currency_code']:
                        currency = f['raised_currency_code']
                    else:
                        currency = "None"  
                        
                    if f['funded_year']:
                        year = f['funded_year']
                    else:
                        year = 0000
                        
                    if f['funded_month']:
                        month = f['funded_month']
                    else:
                        month = 0 
                    
                    if f['funded_day']:
                        day = f['funded_day']
                    else:
                        day = 0              
                    
                    insqry = "INSERT INTO cb_fundrounds(companyid, id, code, source_description, amount, currency, year, month, day) VALUES ('%d', '%d', '%s', '%s', '%s', '%s', '%d', '%d', '%d')" % (row["id"], id, code, sourcedesc, amount, currency, year, month, day)
                     
                   # print insqry        
                    try:            
                        inscursor.execute(insqry)      
                        inscon.commit()
                    except:              
                        inscon.rollback()  
                        
                    #get investor information
                    if f['investments'] and len(f['investments']) > 0:                      
                        for inv in f['investments']:
                            if inv['company'] :
                                invqry1 = "insert into cb_investors(companyid, roundid, category, nam, permalink) values ('%d', '%d', 'company', '%s', '%s')" % (row["id"], id, inv['company']['name'], inv['company']['permalink'])
                                #print invqry1
                                try:            
                                    inscursor1.execute(invqry1)      
                                    inscon1.commit()
                                except:              
                                    inscon1.rollback() 
                            if  inv['financial_org']:
                                invqry2 = "insert into cb_investors(companyid, roundid, category, nam, permalink) values ('%d', '%d', 'financial_org', '%s', '%s')" % (row["id"], id, inv['financial_org']['name'], inv['financial_org']['permalink'])
                                #print invqry2
                                try:            
                                    inscursor1.execute(invqry2)      
                                    inscon1.commit()
                                except:              
                                    inscon.rollback()  
                            if  inv['person'] :
                                invqry3 = "insert into cb_investors(companyid, roundid, category, fname, lname,  permalink) values ('%d', '%d', 'person', '%s', '%s', '%s')" % (row["id"], id, inv['person']['first_name'], inv['person']['last_name'], inv['person']['permalink'])
                                #print invqry3
                                try:            
                                    inscursor1.execute(invqry3)      
                                    inscon1.commit()
                                except:              
                                    inscon1.rollback() 
                                                  
     #update misc info
            if (funddata['number_of_employees'] > 0):
                size = funddata['number_of_employees']
            else:
                size = 0
                         
            if (len(funddata['total_money_raised']) > 0):
                totfund = funddata['total_money_raised']
            else:
                totfund = "None"       
            udtqry = "update cb_companies set size = %d, total_funding = '%s', is_processed = 1 where id = %d" % (size, totfund, row["id"])
            try:            
                inscursor.execute(udtqry)      
                inscon.commit()
            except:              
                inscon.rollback() 
            print "Completed"                            
except mdb.Error, e:
    print "Error %d: %s" % (e.args[0],e.args[1])
    sys.exit(1)    
finally:            
    if con:    
        con.close()      
     

