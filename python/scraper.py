from bs4 import BeautifulSoup
import requests
import re
import MySQLdb
import time
import sys

# Quick script to scrape startup data from startups-list.com
# Author : Ravi Kishor Shakya
# Pls specify MySQL DB details at appr location below

reload(sys)
sys.setdefaultencoding('utf-8')

db = MySQLdb.connect("localhost","root","hjkjh","jana" )
cursor = db.cursor()

#read cities from file
cities = open('cities.txt', 'r')
for city in cities:
    print city
    
    ofile = open(city + ".txt", "w")

    r  = requests.get("http://"  + city.strip() +".startups-list.com")
    time.sleep(20)
    data = r.text

    soup = BeautifulSoup(data, 'html.parser')

    results = soup.find_all("div", "card")
    for element in results:
        ofile.write(element['data-href'])
        ofile.write(element['data-name'])
        media = ""
        for link in element.div.find_all('a'):
            media = media + "," + link.get('href')
        ofile.write(media)    
        res = element.find("a",{"class": "main_link"})

        osql = "INSERT INTO startups(city, website, \
           name, social_media, oneliner, description) \
           VALUES ('%s', '%s', '%s', '%s', '%s', '%s' )" % (MySQLdb.escape_string(city),MySQLdb.escape_string(element['data-href']), MySQLdb.escape_string(element['data-name']), MySQLdb.escape_string(media), MySQLdb.escape_string(res.p.strong.contents[0].string.strip(' \t\n\r')), MySQLdb.escape_string(res.p.contents[2].string.strip(' \t\n\r')))
        
        try:
            ofile.write(osql)
            ofile.write(res.p.strong.contents[0].string.strip(' \t\n\r'))
            ofile.write(res.p.contents[2].string.strip(' \t\n\r'))
            cursor.execute(osql)
            db.commit()
            ofile.write('POPULATED')
        except:
            db.rollback()   
            ofile.write('FAILED')
    ofile.close()       

cities.close()

db.close()        
    
    

    
