import xmlrpclib

class Xmlcall:
    def __init__(self, url, uname, pwd, db):
        self.url = url
        self.uname = uname
        self.pwd = pwd
        self.db = db
        self.uid = self.login()
        
    def login(self):
        try:
            sock = xmlrpclib.ServerProxy(self.url + '/common')
            uid = sock.login(self.db, self.uname, self.pwd)
        except xmlrpclib.Fault as f:
            print f.faultCode
            print f.faultString
        except xmlrpclib.Error as e:
            print e    
        return uid
    
    def execute_action(self, model, action, args):
        try: 
            sock = xmlrpclib.ServerProxy(self.url + '/object')       
            ids = sock.execute(self.db, self.uid, self.pwd, model, action, args)
        except xmlrpclib.Fault as f:
            print f.faultCode
            print f.faultString
        except xmlrpclib.Error as e:
            print e                
        return ids
        