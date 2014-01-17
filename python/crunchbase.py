import requests
import json
import MySQLdb as mdb

#api key
apkey = ''
#mysql db information
host = 'localhost'
port = 3306
db = ''
uname = 'root'
pwd = ''

#make api call and fetch company info
payload = {'api_key' : apkey}
try:
    resp = requests.get("http://api.crunchbase.com/v/1/companies.js", params=payload, timeout=600)
    data = json.loads(resp.content)
except requests.exceptions.RequestException, ex:
    print "Error %d: %s" % (ex.args[0],ex.args[1])
    sys.exit(1)   
     
#populate into MySQL table 
try:
    con = mdb.connect(host, uname, pwd, db)
    with con:    
        cur = con.cursor()
        
        for d in data:
            sql = "insert into cb_companies(nam, permalink, category_code) values (%s, %s, %s )"
           
            try:
               # Execute the SQL command
               cur.execute(sql, (d['name'], d['permalink'], d['category_code']))
               # Commit your changes in the database
               con.commit()
            except:
               # Rollback in case there is any error
               print 'PROBLEM'
               print d
               con.rollback()
except mdb.Error, e:
    print "Error %d: %s" % (e.args[0],e.args[1])
    sys.exit(1)    
finally:            
    if con:    
        con.close()        






