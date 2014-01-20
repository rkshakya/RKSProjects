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
        cur.execute("SELECT `id`, `nam`, `permalink` FROM `cb_companies` WHERE `is_processed` = 0  order by `id` LIMIT 0, 10000")
    
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
            except:
                #print "Error %d: %s" % (ex.args[0],ex.args[1])
                print "Fetch information failed for company : %s " % row["nam"]
                continue
                #sys.exit(1)   

            #populate data into tables  
            if funddata['funding_rounds'] and len(funddata['funding_rounds']) > 0:
                for f in funddata['funding_rounds'] :
                    id = f['id']
                    if f['round_code']:
                        code = f['round_code']
                    else:
                        code = "None"
                    
                        
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
                        year = 0
                        
                    if f['funded_month']:
                        month = f['funded_month']
                    else:
                        month = 0 
                    
                    if f['funded_day']:
                        day = f['funded_day']
                    else:
                        day = 0              
                    
                    insqry = "INSERT INTO cb_fundingrounds(company_id, id, round_code,  raised_amount, raised_currency_code, funded_year, funded_month, funded_day) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)" 
                    
                    #print type(row["id"]), type(id), type(code), type(sourcedesc), type(amount), type(currency), type(year), type(month), type(day)
                    
                    inscursor.execute(insqry, ( row["id"], id, code, amount, currency, year, month, day ))  
                   # print insqry        
                    try:            
                             
                        inscon.commit()
                    except: 
                        print "PROBLEM FUNDINGROUNDS" 
                        print f           
                        inscon.rollback()  
                        
                        
                    #get investor information
                    if f['investments'] and len(f['investments']) > 0:                      
                        for inv in f['investments']:
                            if inv['company'] :
                                invqry1 = "insert into cb_investors(companyid, roundid, category, nam, permalink) values (%s, %s, 'company', %s, %s)" 
                                #print invqry1
                                try:            
                                    inscursor1.execute(invqry1, (row["id"], id, inv['company']['name'], inv['company']['permalink']))      
                                    inscon1.commit()
                                except: 
                                    print "PROBLEM Investment Company" 
                                    print inv
                                    inscon1.rollback() 
                            if  inv['financial_org']:
                                invqry2 = "insert into cb_investors(companyid, roundid, category, nam, permalink) values (%s, %s, 'financial_org', %s, %s)" 
                                #print invqry2
                                try:            
                                    inscursor1.execute(invqry2, (row["id"], id, inv['financial_org']['name'], inv['financial_org']['permalink']))      
                                    inscon1.commit()
                                except: 
                                    print "PROBLEM Investment Financial Org" 
                                    print inv          
                                    inscon.rollback()  
                            if  inv['person'] :
                                invqry3 = "insert into cb_investors(companyid, roundid, category, fname, lname,  permalink) values (%s, %s, 'person', %s, %s, %s)" 
                                #print invqry3
                                try:            
                                    inscursor1.execute(invqry3, (row["id"], id, inv['person']['first_name'], inv['person']['last_name'], inv['person']['permalink']))      
                                    inscon1.commit()
                                except:   
                                    print "PROBLEM Investment Person"
                                    print inv
                                    inscon1.rollback() 
                                                  
            if funddata['relationships'] and len(funddata['relationships']) > 0:
                for rel in funddata['relationships'] :
                    if rel['is_past']:
                        is_past = "true"
                    else:
                        is_past = "false"    
                        
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
                    
                    insqryrel = "INSERT INTO cb_relationships(company_id, is_past, title, first_name, last_name) VALUES (%s, %s, %s, %s, %s)" 
                     
                   # print insqry        
                    try:            
                        inscursor.execute(insqryrel, (row["id"], is_past, title, first_name, last_name))      
                        inscon.commit()
                    except:  
                        print "PROBLEM Relationships"
                        print rel            
                        inscon.rollback()
                        
                                
                    
     
            if funddata['investments'] and len(funddata['investments']) > 0:
                for i in funddata['investments'] :
                    if i['funding_round']['round_code'] and len(i['funding_round']['round_code']) > 0:
                        iround_code = i['funding_round']['round_code']
                    else:
                        iround_code = "None"  
                        
                    
                        
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
                    
                    insqryinvestments = "INSERT INTO cb_investments(company_id, round_code,  raised_amount, raised_currency_code, funded_year, funded_month, funded_day, company, permalink) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)" 
                     
                   # print insqry        
                    try:            
                        inscursor.execute(insqryinvestments, (row["id"], iround_code, iraised_amount, iraised_currency_code, ifunded_year, ifunded_month, ifunded_day, icompany_name, icompany_permalink))      
                        inscon.commit()
                    except:    
                        print "PROBLEM Investments"
                        print i
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
                
                insqryacq = "INSERT INTO cb_acquisition(company_id, price_amount, price_currency_code, term_code,  acquired_year, acquired_month, acquired_day, acquiring_company) VALUES (%s, %s,  %s, %s, %s, %s, %s,%s )" 
                     
                   # print insqry        
                try:            
                   inscursor.execute(insqryacq, (row["id"], aprice_amount, aprice_currency_code, aterm_code, aacquired_year, aacquired_month, aacquired_day, aacquirer))      
                   inscon.commit()
                except:  
                   print "PROBLEM Acquisition"  
                   print funddata['acquisition']
                   inscon.rollback()                            
                
            if funddata['acquisitions'] and len(funddata['acquisitions']) > 0:
                for acq in funddata['acquisitions'] :
                    if acq['price_amount']:
                        ac_price_amount = acq['price_amount']
                    else:
                        ac_price_amount = 0.0
                        
                    if acq['price_currency_code'] and len(acq['price_currency_code']) > 0:
                        ac_price_currency_code = acq['price_currency_code']
                    else:
                        ac_price_currency_code = "None" 
                    
                   
                        
                    if acq['acquired_year']:
                        ac_acquired_year = acq['acquired_year']
                    else:
                        ac_acquired_year = 0
                        
                    if acq['acquired_month']:
                        ac_acquired_month = acq['acquired_month']
                    else:
                        ac_acquired_month = 0 
                        
                    if acq['acquired_day']:
                        ac_acquired_day = acq['acquired_day']
                    else:
                        ac_acquired_day = 0 
                        
                    if acq['company']['name'] and len(acq['company']['name']) > 0:
                        ac_company_name = acq['company']['name']
                    else:
                        ac_company_name = "None" 
                    
                    insqryacqs = "INSERT INTO cb_acquisitions(company_id, price_amount, price_currency_code,  acquired_year, acquired_month, acquired_day, company ) VALUES (%s, %s, %s, %s, %s, %s, %s)" 
                     
                   # print insqry        
                    try:            
                        inscursor.execute(insqryacqs, (row["id"], ac_price_amount, ac_price_currency_code,  ac_acquired_year, ac_acquired_month, ac_acquired_day, ac_company_name))      
                        inscon.commit()
                    except:    
                        print "PROBLEM Acquisitions" 
                        print acq
                        inscon.rollback()                                    
                    
            
            if funddata['offices'] and len(funddata['offices']) > 0:
                for off in funddata['offices'] :
                    if off['description'] and len(off['description']) > 0:
                        o_description = off['description']
                    else:
                        o_description = "None"
                        
                    if off['address1'] and len(off['address1']) > 0:
                        o_address1 = off['address1']
                    else:
                        o_address1 = "None"
                    
                    if off['address2'] and len(off['address2']) > 0:
                        o_address2 = off['address2']
                    else:
                        o_address2 = "None" 
                        
                    if off['zip_code'] and len(off['zip_code']) > 0:
                        o_zip_code = off['zip_code']
                    else:
                        o_zip_code = "None" 
                        
                    if off['city'] and len(off['city']) > 0:
                        o_city = off['city']
                    else:
                        o_city = "None"
                        
                    if off['state_code'] and len(off['state_code']) > 0:
                        o_state_code = off['state_code']
                    else:
                        o_state_code = "None" 
                        
                    if off['country_code'] and len(off['country_code']) > 0:
                        o_country_code = off['country_code']
                    else:
                        o_country_code = "None"  
                        
                    
                        
                    insqryoff = "INSERT INTO cb_offices(company_id, description, address1, address2, zip_code, city, state_code, country_code) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)" 
                     
                   # print insqry        
                    try:            
                        inscursor.execute(insqryoff, (row["id"], o_description, o_address1, o_address2, o_zip_code, o_city, o_state_code, o_country_code))      
                        inscon.commit()
                    except:  
                        print "PROBLEM Offices" 
                        print off
                        inscon.rollback()                                       
            
            
            if funddata['ipo'] and len(funddata['ipo']) > 0:
                if funddata['ipo']['valuation_amount'] :
                    ip_valuation_amount = funddata['ipo']['valuation_amount']
                else:
                    ip_valuation_amount = 0.0 
                    
                if funddata['ipo']['valuation_currency_code'] and len(funddata['ipo']['valuation_currency_code']) > 0:
                    ip_valuation_currency_code = funddata['ipo']['valuation_currency_code']
                else:
                    ip_valuation_currency_code = "None"
                
                if funddata['ipo']['pub_year'] :
                    ip_pub_year = funddata['ipo']['pub_year']
                else:
                    ip_pub_year = 0
                    
                if funddata['ipo']['pub_month'] :
                    ip_pub_month = funddata['ipo']['pub_month']
                else:
                    ip_pub_month = 0  
                    
                if funddata['ipo']['pub_day'] :
                    ip_pub_day = funddata['ipo']['pub_day']
                else:
                    ip_pub_day = 0 
                    
                if funddata['ipo']['stock_symbol'] and len(funddata['ipo']['stock_symbol']) > 0:
                    ip_stock_symbol = funddata['ipo']['stock_symbol']
                else:
                    ip_stock_symbol = "None" 
                    
                insqryipo = "INSERT INTO cb_ipo(company_id, valuation_amount, valuation_currency_code, pub_year, pub_month, pub_day, stock_symbol) VALUES (%s, %s, %s, %s, %s, %s, %s)" 
                try:            
                   inscursor.execute(insqryipo, (row["id"], ip_valuation_amount, ip_valuation_currency_code, ip_pub_year, ip_pub_month, ip_pub_day, ip_stock_symbol))      
                   inscon.commit()
                except: 
                   print "PROBLEM IPO" 
                   print funddata['ipo']
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
                
            if funddata['homepage_url'] and (len(funddata['homepage_url']) > 0):
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
                
            if funddata['description'] and (len(funddata['description']) > 0):
                description = funddata['description']
            else:
                description = "None" 
                
            if funddata['overview'] and (len(funddata['overview']) > 0):
                overview = funddata['overview']
            else:
                overview = "None"  
                
            if funddata['category_code'] and (len(funddata['category_code']) > 0):
                category_code = funddata['category_code']
            else:
                category_code = "None"
                
            udtover =  "update cb_companies set description = %s, overview = %s where id = %s" 
            try:            
                inscursor.execute(udtover, (description, overview, row["id"]))      
                inscon.commit()
            except:  
                print "PROBLEM Desc block"                 
                inscon.rollback()                       
                           
            udtqry = "update cb_companies set number_of_employees = %s, total_money_raised = %s, is_processed = 1, homepage_url = %s, founded_year = %s, founded_month = %s, founded_day = %s, deadpooled_year = %s, deadpooled_month = %s, deadpooled_day = %s,  catcode = %s where id = %s" 
            
            #print udtqry                                                                                                                   
            try:            
                inscursor.execute(udtqry, (size, totfund, homepage_url, founded_year, founded_month, founded_day, deadpooled_year, deadpooled_month, deadpooled_day, category_code, row["id"]))      
                inscon.commit()
            except:  
                print "PROBLEM Last block" 
                inscon.rollback() 
            print "Completed"                            
except mdb.Error, e:
    print "Error %d: %s" % (e.args[0],e.args[1])
    sys.exit(1)    
finally:            
    if con:    
        con.close()      
     

