from bs4 import BeautifulSoup
import requests
import re
import MySQLdb

# Quick script to scrape startup data from startups-list.com
# Author : Ravi Kishor Shakya
# Pls specify MySQL DB details at appr location below

import sys
reload(sys)
sys.setdefaultencoding('utf-8')

db = MySQLdb.connect("localhost","root","hjkhjh","jana" )
cursor = db.cursor()

r  = requests.get("http://porto.startups-list.com")
data = r.text

soup = BeautifulSoup(data, 'html.parser')

results = soup.find_all("div", "card")
for element in results:
    print(element['data-href'])
    print(element['data-name'])
    media = ""
    for link in element.div.find_all('a'):
        media = media + "," + link.get('href')
    print media    
    res = element.find("a",{"class": "main_link"})
    print res.p.strong.contents[0].string
    print res.p.contents[2].string
    sql = "INSERT INTO startups(website, \
       name, social_media, oneliner, description) \
       VALUES ('%s', '%s', '%s', '%s', '%s' )" % \
       (element['data-href'], element['data-name'], media, res.p.strong.contents[0].string.strip(' \t\n\r'), res.p.contents[2].string.strip(' \t\n\r'))
    try:
        cursor.execute(sql)
        db.commit()
    except:
        db.rollback()   

db.close()        
    
    

    
