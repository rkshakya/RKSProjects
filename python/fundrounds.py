import requests
import json
import MySQLdb as mdb

#api key
apkey = ''
#mysql db information
host = 'localhost'
port = 3306
db = 'crunchbase'
uname = ''
pwd = ''

payload = {'api_key' : apkey}

#get company info from MySQL table
#populate into MySQL table 
try:
    con = mdb.connect(host, uname, pwd, db)
    
    with con:    
        cur = con.cursor(mdb.cursors.DictCursor)
        cur.execute("SELECT `id`, `nam`, `permalink` FROM `cb_companies` WHERE `is_processed` = 0 order by `id` LIMIT 0, 5")
    
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
                    
                    insqry = "INSERT INTO cb_fundingrounds(company_id, id, round_code, source_description, raised_amount, raised_currency_code, funded_year, funded_month, funded_day) VALUES ('%d', '%d', '%s', '%s', '%s', '%s', '%d', '%d', '%d')" % (row["id"], id, code, sourcedesc, amount, currency, year, month, day)
                     
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
                                                  
            if funddata['relationships'] and len(funddata['relationships']) > 0:
                for rel in funddata['relationships'] :
                    if rel['is_past']:
                        is_past = rel['is_past']
                    else:
                        is_past = "None"    
                        
                    if rel['title'] and len(rel['title']) > 0:
                        title = rel['title']
                    else:
                        title = "None"
                        
                    if rel['person']['first_name'] and len(rel['person']['first_name']) > 0:
                        first_name = rel['person']['first_name']
                    else:
                        first_name = "None"   
                        
                    if rel['person']['last_name'] and len(rel['person']['last_name']) > 0:
                        last_name = rel['person']['last_name']
                    else:
                        last_name = "None" 
                    
                    insqryrel = "INSERT INTO cb_relationships(company_id, is_past, title, first_name, last_name) VALUES ('%d', '%s', '%s', '%s', '%s')" % (row["id"], is_past, title, first_name, last_name)
                     
                   # print insqry        
                    try:            
                        inscursor.execute(insqryrel)      
                        inscon.commit()
                    except:              
                        inscon.rollback()
                        
                                
                    
     
            if funddata['investments'] and len(funddata['investments']) > 0:
                for i in funddata['investments'] :
                    if i['funding_round']['round_code'] and len(i['funding_round']['round_code']) > 0:
                        iround_code = i['funding_round']['round_code']
                    else:
                        iround_code = "None"  
                        
                    if i['funding_round']['source_url'] and len(i['funding_round']['source_url']) > 0:
                        isource_url = i['funding_round']['source_url']
                    else:
                        isource_url = "None" 
                    
                    if i['funding_round']['source_description'] and len(i['funding_round']['source_description']) > 0:
                        isource_description = i['funding_round']['source_description']
                    else:
                        isource_description = "None"  
                        
                    if i['funding_round']['raised_amount'] :
                        iraised_amount = i['funding_round']['raised_amount']
                    else:
                        iraised_amount = 0.0
                        
                    if i['funding_round']['raised_currency_code'] and len(i['funding_round']['raised_currency_code']) > 0:
                        iraised_currency_code = i['funding_round']['raised_currency_code']
                    else:
                        iraised_currency_code = "None" 
                        
                    if i['funding_round']['funded_year'] :
                        ifunded_year = i['funding_round']['funded_year']
                    else:
                        ifunded_year = 0 
                        
                    if i['funding_round']['funded_month'] :
                        ifunded_month = i['funding_round']['funded_month']
                    else:
                        ifunded_month = 0 
                        
                    if i['funding_round']['funded_day'] :
                        ifunded_day = i['funding_round']['funded_day']
                    else:
                        ifunded_day = 0 
                        
                    if i['funding_round']['company']['name'] and len(i['funding_round']['company']['name']) > 0:
                        icompany_name = i['funding_round']['company']['name']
                    else:
                        icompany_name = "None" 
                        
                    if i['funding_round']['company']['permalink'] and len(i['funding_round']['company']['permalink']) > 0:
                        icompany_permalink = i['funding_round']['company']['permalink']
                    else:
                        icompany_permalink = "None"  
                    
                    insqryinvestments = "INSERT INTO cb_investments(company_id, round_code, source_url, source_description, raised_amount, raised_currency_code, funded_year, funded_month, funded_day, company, permalink) VALUES ('%d', '%s', '%s', '%s', '%f', '%s', '%d', '%d', '%d', '%s', '%s')" % (row["id"], iround_code, isource_url, isource_description, iraised_amount, iraised_currency_code, ifunded_year, ifunded_month, ifunded_day, icompany_name, icompany_permalink)
                     
                   # print insqry        
                    try:            
                        inscursor.execute(insqryinvestments)      
                        inscon.commit()
                    except:              
                        inscon.rollback()                                
     
            if funddata['acquisition'] and len(funddata['acquisition']) > 0:
                if funddata['acquisition']['price_amount'] :
                    aprice_amount = funddata['acquisition']['price_amount']
                else:
                    aprice_amount = 0.0
                    
                if funddata['acquisition']['price_currency_code'] and len(funddata['acquisition']['price_currency_code']) > 0:
                    aprice_currency_code = funddata['acquisition']['price_currency_code']
                else:
                    aprice_currency_code = "None" 
                
                if funddata['acquisition']['term_code'] and len(funddata['acquisition']['term_code']) > 0:
                    aterm_code = funddata['acquisition']['term_code']
                else:
                    aterm_code = "None" 
                    
                if funddata['acquisition']['source_url'] and len(funddata['acquisition']['source_url']) > 0:
                    asource_url = funddata['acquisition']['source_url']
                else:
                    asource_url = "None" 
                    
                if funddata['acquisition']['source_description'] and len(funddata['acquisition']['source_description']) > 0:
                    asource_description = funddata['acquisition']['source_description']
                else:
                    asource_description = "None" 
                    
                if funddata['acquisition']['acquired_year'] :
                    aacquired_year = funddata['acquisition']['acquired_year']
                else:
                    aacquired_year = 0 
                    
                if funddata['acquisition']['acquired_month'] :
                    aacquired_month = funddata['acquisition']['acquired_month']
                else:
                    aacquired_month = 0 
                      
                if funddata['acquisition']['acquired_day'] :
                    aacquired_day = funddata['acquisition']['acquired_day']
                else:
                    aacquired_day = 0
                    
                if funddata['acquisition']['acquiring_company']['name'] and len(funddata['acquisition']['acquiring_company']['name']) > 0:
                    aacquirer = funddata['acquisition']['acquiring_company']['name']
                else:
                    aacquirer = "None"  
                
                insqryrel = "INSERT INTO cb_relationships(company_id, is_past, title, first_name, last_name) VALUES ('%d', '%s', '%s', '%s', '%s')" % (row["id"], is_past, title, first_name, last_name)
                     
                   # print insqry        
                try:            
                   inscursor.execute(insqryrel)      
                   inscon.commit()
                except:              
                   inscon.rollback()                            
                
     #update misc info
            if (funddata['number_of_employees'] > 0):
                size = funddata['number_of_employees']
            else:
                size = 0
                         
            if (len(funddata['total_money_raised']) > 0):
                totfund = funddata['total_money_raised']
            else:
                totfund = "None" 
                
            if (len(funddata['homepage_url']) > 0):
                homepage_url = funddata['homepage_url']
            else:
                homepage_url = "None"
                
            if funddata['founded_year']:
                founded_year = funddata['founded_year']
            else:
                founded_year = 0  
            
            if funddata['founded_month']:
                founded_month = funddata['founded_month']
            else:
                founded_month = 0 
                
            if funddata['founded_day']:
                founded_day = funddata['founded_day']
            else:
                founded_day = 0 
                
            if funddata['deadpooled_year']:
                deadpooled_year = funddata['deadpooled_year']
            else:
                deadpooled_year = 0  
            
            if funddata['deadpooled_month']:
                deadpooled_month = funddata['deadpooled_month']
            else:
                deadpooled_month = 0 
                
            if funddata['deadpooled_day']:
                deadpooled_day = funddata['deadpooled_day']
            else:
                deadpooled_day = 0   
                
            if (len(funddata['description']) > 0):
                description = funddata['description']
            else:
                description = "None" 
                
            if (len(funddata['overview']) > 0):
                overview = funddata['overview']
            else:
                overview = "None"  
                
            if (len(funddata['category_code']) > 0):
                category_code = funddata['category_code']
            else:
                category_code = "None"                      
                           
            udtqry = "update cb_companies set number_of_employees = %d, total_money_raised = '%s', is_processed = 1, homepage_url = '%s', founded_year = %d, founded_month = %d, founded_day = %d, deadpooled_year = %d, deadpooled_month = %d, deadpooled_day = %d, description = '%s', overview = '%s', catcode = '%s' where id = %d" % (size, totfund, homepage_url, founded_year, founded_month, founded_day, deadpooled_year, deadpooled_month, deadpooled_day, description, overview, category_code, row["id"])
                                                                                                                                
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
     

