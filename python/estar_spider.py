import re
from bs4 import BeautifulSoup
import requests

from scrapy.contrib.spiders.init import InitSpider
from scrapy.selector import HtmlXPathSelector
from scrapy.contrib.linkextractors.sgml import SgmlLinkExtractor
from scrapy.contrib.spiders import CrawlSpider, Rule
from estar.items import EstarItem
from scrapy.http import FormRequest
from scrapy.http import Request


class EstarSpider(InitSpider):
    name = "estarspi"
    allowed_domains = ["asdsaf.com"]
    login_page = 'https://asdasf.com/login'
    start_urls = [
        "https://asdasf.com/products"
    ]
    
    rules = (
        Rule(SgmlLinkExtractor(allow=r'products\?page=[1-2]'), callback='parse_item', follow=True),
    )
    
    def init_request(self):
        """This function is called before crawling starts."""
        return Request(url=self.login_page, callback=self.login)

    def login(self, response):
        """Generate a login request."""
        return FormRequest.from_response(response,
                    formdata={'uname': 'asdaahsk', 'password': 'uoiu'},
                    callback=self.after_login)


    def after_login(self, response):
        # check login succeed before going on
        if "authentication failed" in response.body:
            self.log("Login failed", level=log.ERROR)
            return
        else:
            self.initialized()
            for page in range(1,80):
                yield Request(url="https://asdasf.com/products?page=%s" %page,callback=self.parse_item)  

    def parse_item(self, response):
        goods = HtmlXPathSelector(response).select('//tr') 
        
        for good in goods:
            item = EstarItem()
            item['imageURL'] = " ".join (map(unicode.strip, good.select('td/div/img/@src').extract()))
            item['itemid'] = " ".join (map(unicode.strip,good.select('td/a[@class="productDetailsLink"]/@item-id').extract()))
            item['title'] = " ".join (map(unicode.strip,good.select('td/a[@class="productDetailsLink"]/text()').extract()))
            item['quantity'] = " ".join (map(unicode.strip,good.select('td[@class="quantity"]/div[@class="qtyHolder"]/text()').extract()))
            if len(item['itemid']) == 0:
                continue 
            item['date'] =  " ".join (map(unicode.strip,good.select('td[3]/text()').extract()))
            item['sku'] = " ".join (map(unicode.strip, good.select('td[4]/text()').extract() ))
            item['typ'] = " ".join (map(unicode.strip, good.select('td[5]/text()').extract() ))
            item['condition'] = " ".join (map(unicode.strip, good.select('td[6]/text()').extract() ))
            item['stock'] = " ".join (map(unicode.strip, good.select('td[7]/text()').extract() ))
            item['msrp'] = " ".join (map(unicode.strip, good.select('td[8]/text()').extract() ))
            item['price'] = " ".join (map(unicode.strip, good.select('td[9]/text()').extract() ))
            
            r  = requests.get("https://asdasf.com/ajax/getProductDetails?id=" + str(item['itemid']))
            item['link'] = "https://asdasf.com/ajax/getProductDetails?id=" + str(item['itemid'])
            print 'URL', "https://asdasf.com/ajax/getProductDetails?id=" + str(item['itemid'])
            data = r.text
            soup = BeautifulSoup(data, 'html.parser')

            results = soup.find_all("td")
            if results:
                if results[0]:
                    item['sku'] = results[0].string
                if results[1]:    
                    item['platform'] = results[1].string
                if results[2]:      
                    item['date'] = results[2].string
                if results[6]:      
                    item['upc'] = results[6].string
                if results[7]:      
                    item['publisher'] = results[7].string
                if results[8]:      
                    item['availability'] = results[8].string

            paras = soup.find_all("p")
            txt = ""
            try:
                if paras[0]:
                   txt += paras[0].text 
                if paras[1]:
                    txt += paras[1].text
                if paras[2]:
                    txt += paras[2].text 
            except IndexError:     
                print "Some issue with para"
                
            item['description'] = txt
          
            yield item
           
            

        

    
            




