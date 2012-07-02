import xmlrpclib
from xmlcall import Xmlcall
import openerpconfig

class Lead(object):        
    def setName(self, value):
        #print value
        self.name = value

    def getName(self):
        return self.name
    
    #indicates if partner is active and displayable
    def setActive(self, value = True):
        self.active = value
        
    def getActive(self):        
        return self.active
    
    #lead email
    def setEmailfrom(self, value):
        self.email_from = value

    def getEmailfrom(self):
        return self.email_from
    
    #description field
    def setDescription(self, value):
        self.description = value

    def getDescription(self):
        return self.description
    
    #stage(status) of the lead
    def setStage(self, value):
        #chk if this stage already exists
        args = [('name', '=', value), ('type', '=', 'lead')]
        model = 'crm.case.stage'
        call = Xmlcall(openerpconfig.rpcurl, openerpconfig.rpcuser, openerpconfig.rpcpwd, openerpconfig.rpcdb)
        ids = call.execute_action(model, 'search', args)        
        
        #if not exist, try creating
        if len(ids) == 0:
            stage = {'name': value, 'type': 'lead'}        
            ids = call.execute_action(model, 'create', stage)
            self.stage_id = ids
        else:
            self.stage_id = ids[0]
        
        
        
        
    def getStage(self)        :
        return self.stage.id
    
    #user(salesman) the lead is assigned to
    def setUser(self, value):
        #chk if this stage already exists
        args = [('name', '=', value)]
        model = 'res.users'
        call = Xmlcall(openerpconfig.rpcurl, openerpconfig.rpcuser, openerpconfig.rpcpwd, openerpconfig.rpcdb)
        ids = call.execute_action(model, 'search', args)
        
        #do not try creating if not present, fallback to deafult
        if len(ids) > 0:
            self.user_id = ids[0]
        
    def getUser(self):
        return self.user_id
    
    #lead source (referred by)
    def setReferred(self, value):
        self.referred = value
        
    def getReferred(self)        :
        return self.referred
    
    #lead's designation(title)
    def setFunction(self, value):
        self.function = value
        
    def getFunction(self)        :
        return self.function
    
    def setStreet(self, value):
        self.street = value
    
    def getStreet(self)        :
        return self.street
    
    def setStreet2(self, value):
        self.street2 = value
        
    def getStreet2(self)        :
        return self.street2
    
    def setZip(self, value):
        self.zip = value
        
    def getZip(self)        :
        return self.zip
    
    def setCity(self, value):
        self.city = value
        
    def getCity(self)        :
        return self.city
    
    #for other emails
    def setEmail(self, value):
        self.email = value
        
    def getEmail(self)        :
        return self.email
     
    def setPhone(self, value)   :
        self.phone = value
        
    def getPhone(self)        :
        return self.phone
    
    def setFax(self, value):
        self.fax = value
        
    def getFax(self)        :
        return self.fax
    
    def setMobile(self, value):
        self.mobile = value
    
    def getMobile(self):
        return self.mobile
    
    def setIscustomeradd(self, value = True):
        self.is_customer_add = value
        
    def getIscustomeradd(self)   :
        return is_customer_add
    
    def setCompanyid(self, value)        :
        #check to see if company value exists
        args = [('name', '=', value)]            
        model = 'res.company'
        call = Xmlcall(openerpconfig.rpcurl, openerpconfig.rpcuser, openerpconfig.rpcpwd, openerpconfig.rpcdb)
        ids = call.execute_action(model, 'search', args)    
         
        if len(ids) > 0:
            self.company_id = ids[0]
        
    def getCompanyid(self):
        return self.company_id
        
    def setcountry_id(self, value):      
        #check to see if country value exists
        args = [('name', '=', value)]            
        model = 'res.country'
        call = Xmlcall(openerpconfig.rpcurl, openerpconfig.rpcuser, openerpconfig.rpcpwd, openerpconfig.rpcdb)
        ids = call.execute_action(model, 'search', args)        
         
        if len(ids) > 0:
            self.country_id = ids[0]

    def getcountry_id(self):
        return self.country_id
    