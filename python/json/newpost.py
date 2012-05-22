import os
import webapp2
import cgi 
import re
import jinja2
import json
import random
import string
import hmac
from google.appengine.ext import db

template_dir = os.path.join(os.path.dirname(__file__), 'templates')
jinja_env = jinja2.Environment(loader = jinja2.FileSystemLoader(template_dir), autoescape=True)

SECR = "pakhandi"  #Salt for hash

#regular expressions
USER_RE = re.compile(r"^[a-zA-Z0-9_-]{3,20}$")  #regex to validate username
PWD_RE = re.compile(r"^.{3,20}$") #regex to validate password
EMAIL_RE = re.compile(r"^[\S]+@[\S]+\.[\S]+$")  #regex to validate email

#validation messages
USR_MSG = "Invalid username!"
PWD_MSG = "Invalid password"
VER_MSG = "Passwords mismatch"
EMAIL_MSG = "Invalid email"
LOGIN_MSG = "Invalid login"

def hash_str(s):
    return hmac.new(SECR, s).hexdigest()
 
def make_secure_val(s):
    return "%s|%s" % (s, hash_str( s))
 
def check_secure_val(h):
    val = h.split('|')[0]
    if h == make_secure_val(val):
        return val

def escape_html(s):
    return cgi.escape(s, quote = True)
    
def valid_username(username):
  return USER_RE.match(username)

def valid_password(password):
  return PWD_RE.match(password)  
  
def valid_email(email):
  return EMAIL_RE.match(email)  

form3="""<html>
                <head>
                <title>Sign Up</title>
                <style type="text/css">
      .label {text-align: right}
      .error {color: red}
    </style>
    </head>
                <body>
                  User Sign Up<br/>
                  <form method="post">
                                        <table>
                                             <tr>
                                                                                              <td class="label">Username</td>
                                                                                                                                                                                                                                                                                        <td><input 
 type = "text" name = "username"    value = "%(username)s"                                                                                                                                                                                                                                                                           ></td>                                                                                                                                                                                              <td class="error">%(errname)s</td>
                                             </tr>
                                                                                           <tr>
                                                                                            <td class="label">Password</td>
                                                                                                                                                                                          <td><input type = "password" name = "password" value = ""></td>                                                                                                                                                                                              <td class="error">%(errpwd)s</td>
                                                                                          </tr>
                                                                                           <tr>
                                                                                            <td class="label">Verify password</td>
                                                                                                                                                                                          <td><input type = "password" name = "verify" value = ""></td>                                                                                                                                                                                              <td class="error">%(errverify)s</td>
                                                                                          </tr>
                                                                                           <tr>
                                                                                            <td class="label">Email(Optional)</td>
                                                                                                                                                                                          <td><input type = "text" name = "email" value = "%(email)s"></td>                                                                                                                                                                                              <td class="error">%(erremail)s</td>
                                                                                          </tr>
                                        </table>
                    <input type="submit">
                  </form>
                </body>
        </html>
          """
          
form1="""<html>
                <head>
                <title>Login</title>
                <style type="text/css">
      .label {text-align: right}
      .error {color: red}
    </style>
    </head>
                <body>
                  User Login<br/>
                  <form method="post">
                                        <table>
                                             <tr>
                                                                                              <td class="label">Username</td>
                                                                                                                                                                                                                                                                                        <td><input 
 type = "text" name = "username"    value = "%(username)s"                                                                                                                                                                                                                                                                           ></td>                                                                                                                                                                                             
                                             </tr>
                                                                                           <tr>
                                                                                            <td class="label">Password</td>
                                                                                                                                                                                          <td><input type = "password" name = "password" value = ""></td>                                                                                                                                                                                              
                                                                                          </tr>
                                                                                          <tr><td class="error">%(errlogin)s</td></tr>
                                                                                           </table>
                    <input type="submit">
                  </form>
                </body>
        </html>

                                                                                          """
class LogoutHandler(webapp2.RequestHandler):
  def get(self):
    self.response.headers.add_header('Set-Cookie', 'uid=; Path=/')
    self.redirect("/blog/signup") 
         
                                                                                          
class LoginHandler(webapp2.RequestHandler):
  def write_form(self, errlogin = "", username = ""):
    self.response.out.write(form1 %{"errlogin" : errlogin, "username": escape_html(username)})
  def get(self):
    self.write_form()
  def post(self):
    #get all the req params
    uname = self.request.get("username")
    pwd = self.request.get("password")
    
     #validate the params
    username = valid_username(uname)
    password = valid_password(pwd)
    
    #self.response.out.write("%s %s" %(uname, hash_str(pwd)))
    
    if username and password:    
      us = db.GqlQuery("SELECT * FROM UserInfo WHERE username =:1 AND password =:2", uname, hash_str(pwd))
      if us.get():
              #set cookie
        self.response.headers.add_header('Set-Cookie', 'uid=%s; Path=/' % make_secure_val(str(us.get().key().id())))
        #self.response.headers.add_header('Set-Cookie', 'uid=Jatru; Path=/')
              #redirect
        self.redirect("/blog/signup/welcome")     
      else:
        self.write_form(LOGIN_MSG, uname)
    else:
      self.write_form(LOGIN_MSG, uname)
   
class SignHandler(webapp2.RequestHandler):
  def write_form(self, errname = "", errpwd = "", errverify = "", erremail = "", username = "", email = ""):
    self.response.out.write(form3 %{"errname" : errname, "errpwd": errpwd, "errverify" : errverify, "erremail": erremail, "username": escape_html(username), "email": escape_html(email)})
  def get(self):
    self.write_form()
  def post(self):
    #get all the req params
    uname = self.request.get("username")
    pwd = self.request.get("password")
    verpwd = self.request.get("verify")
    eml = self.request.get("email")
    
    #print uname
    
    #validate the params
    username = valid_username(uname)
    password = valid_password(pwd)
    verflag = (pwd == verpwd)
    if len(eml) > 0:
      email = valid_email(eml)

    #print "username: ", email
    
   # USR_MSG = "Invalid username"
#PWD_MSG = "Invalid password"
#VER_MSG = "Passwords mismatch"
#EMAIL_MSG

    if len(eml) > 0:
          if not (username or password or verflag or email):
            self.write_form(USR_MSG, PWD_MSG, VER_MSG, EMAIL_MSG, uname, eml)
          elif not (username or password or verflag):
            self.write_form(USR_MSG, PWD_MSG, VER_MSG, "", uname, eml)
          elif not (username or password or email):
            self.write_form(USR_MSG, PWD_MSG, "", EMAIL_MSG, uname, eml)
          elif not (username or password):
            self.write_form(USR_MSG, PWD_MSG, "", "", uname, eml)
          elif not (username or verflag or email):
            self.write_form(USR_MSG, "", VER_MSG, EMAIL_MSG, uname, eml)
          elif not (username or verflag):
            self.write_form(USR_MSG, "", VER_MSG, "", uname, eml)
          elif not (username or email):
            self.write_form(USR_MSG, "", "", EMAIL_MSG, uname, eml)
          elif not (username):
            self.write_form(USR_MSG, "", "", "", uname, eml)
          elif not (password or verflag or email):
            self.write_form("", PWD_MSG, VER_MSG, EMAIL_MSG, uname, eml)
          elif not (password or verflag):
            self.write_form("", PWD_MSG, VER_MSG, "", uname, eml)
          elif not (password or email):
            self.write_form("", PWD_MSG, "", EMAIL_MSG, uname, eml)
          elif not (password):
            self.write_form("", PWD_MSG, "", "", uname, eml)
          elif not (email or verflag):
            self.write_form("", "", VER_MSG, EMAIL_MSG, uname, eml)
          elif not (verflag):
            self.write_form("", "", VER_MSG, "", uname, eml)
          elif not (email):
            self.write_form("", "", "", EMAIL_MSG, uname, eml)
          else:
              #register in DB
#             sl = make_salt()
            u = UserInfo(username = uname, password = hash_str(pwd), email = eml)
            u.put()
            uid = u.key().id()
              #set cookie
            self.response.headers.add_header('Set-Cookie', 'uid=%s; Path=/' % make_secure_val(str(uid)))
              #redirect
            self.redirect("/blog/signup/welcome")                            
    else:
      if not username and not password and not verflag:
        self.write_form(USR_MSG, PWD_MSG, VER_MSG, "", uname, eml)
      elif not username and not password:
        self.write_form(USR_MSG, PWD_MSG, "", "", uname, eml)
      elif not username and not verflag:
        self.write_form(USR_MSG, "", VER_MSG, "", uname, eml)
      elif not (username):
        self.write_form(USR_MSG, "", "", "", uname, eml)
      elif not password and not verflag:
        self.write_form("", PWD_MSG, VER_MSG, "", uname, eml)
      elif not (password):
        self.write_form("", PWD_MSG, "", "", uname, eml)
      elif not (verflag):
        self.write_form("", "", VER_MSG, "", uname, eml)
      else:
        #sl = make_salt()
        u = UserInfo(username = uname, password = hash_str(pwd), email = eml)
        u.put()
        uid = u.key().id()
        #set cookie
        self.response.headers.add_header('Set-Cookie', 'uid=%s; Path=/' % make_secure_val(str(uid)))
        #redirect
        self.redirect("/blog/signup/welcome")        
  
class UserInfo(db.Model):
  username = db.StringProperty(required = True)
  password = db.StringProperty(required = True)
  email = db.StringProperty(required = False)
#  slt = db.StringProperty(required = True)
  created = db.DateTimeProperty(auto_now_add = True) 
    
class WelcomeHandler(webapp2.RequestHandler):
  def get(self):
  #get cookie
    cook = self.request.cookies.get('uid')
  
  #validate cookie(hash compare)
    if check_secure_val(cook):    
        #if valid,  display welcome message
        #get username from DB
      us = UserInfo.get_by_id(int(check_secure_val(cook)))
      self.response.out.write("Welcome, %s!" % us.username)    
    else:
      #else redirect to the login page
      self.redirect("/blog/signup")

#validation messages
SUB_MSG = "No subject"
CON_MSG = "No content"

def escape_html(s):
    return cgi.escape(s, quote = True)

form="""<html>
                <head>
                <title>New post</title>
                <style type="text/css">
      .label {text-align: right}
      .error {color: red}
    </style>
    </head>
                <body>
                  New post<br/>
                  <form method="post">
                                        <table>
                                             <tr>
                                                                                              <td class="label">Subject</td>
                                                                                                                                                                                                                                                                                        <td><input 
 type = "text" name = "subject"    value = "%(subject)s"                                                                                                                                                                                                                                                                           ></td>                                                                                                                                                                                              <td class="error">%(errsub)s</td>
                                             </tr>
                                                                                           <tr>
                                                                                            <td class="label">Content</td>
                                                                                                                                                                                          <td><textarea name = "content"></textarea></td>                                                                                                                                                                                              <td class="error">%(errcon)s</td>
                                                                                          </tr>
                                                                                           
                                        </table>
                    <input type="submit">
                  </form>
                </body>
        </html>
          """
          
form2="""<html>
                <head>
                <title>Permalink</title>                
                <style type="text/css">
      .body {
        font-family: sans-serif; width: 800px; margin: 0 auto; padding: 10px;
    }
      hr {
        margin: 20px auto;
    }   
    .title {
        font-weight: bold; font-size: 20px;
    }
    </style>
    </head>
                <body>
                  <div class = "title">%(subject)s</div> <div align = "right">%(created)s</div><hr>
                  <div class = "body">%(content)s</div                
                  
                  </body>
                  </html>
                  """   
                  

class MainPage(webapp2.RequestHandler):
  def write_form(self, errsub = "", errcon = "",  subject = "", content = ""):
    self.response.out.write(form %{"errsub" : errsub, "errcon": errcon, "subject": escape_html(subject), "content": escape_html(content)})
  def get(self):
    self.write_form()
  def post(self):
    #get all the req params
    sub = self.request.get("subject")
    con = self.request.get("content")
    
    #validate the posted data
    if sub and con:
      #populate into data store if valid
      #redirect to permalink page
      p = BlogPost(subject = sub, content = con)
      p.put()
      entid = p.key().id()
      self.redirect("/blog/%s" %entid)
    elif sub and not con:
      self.write_form("", CON_MSG,  sub, con)
    elif not sub and con:
      self.write_form(SUB_MSG, "",  sub, con)
    else:
      self.write_form(SUB_MSG, CON_MSG,  sub, con)
      
class BlogPost(db.Model):
  subject = db.StringProperty(required = True)
  content = db.TextProperty(required = True)
  created = db.DateTimeProperty(auto_now_add = True)
    
class PakhandiHandler(webapp2.RequestHandler):
  def write_form2(self,  subject = "", content = "", created = ""):
    self.response.out.write(form2 %{"subject": escape_html(subject), "content": escape_html(content), "created":created})
    
  def get(self, post_id):
    #self.response.out.write("Welcome, %s!" % post_id)   
    bpost = BlogPost.get_by_id(int(post_id))
    self.write_form2(bpost.subject, bpost.content, bpost.created)
    
class PermJsonHandler(webapp2.RequestHandler):
  def get(self, post_id):
    #self.response.out.write("Welcome, %s!" % post_id)   
    bpost = BlogPost.get_by_id(int(post_id))
    self.response.headers['Content-Type'] = 'application/json'
    p = json.dumps({"content": bpost.content, "subject": bpost.subject})
    self.response.out.write(p)
 
class AllJsonHandler(webapp2.RequestHandler):
  def get(self):
    posts = db.GqlQuery("SELECT * FROM BlogPost ORDER BY created DESC")
    #form a list of dicts
    pos = []
    for p in posts:
        indipost = {"content":p.content,"subject":p.subject}
        pos.append(indipost)
    self.response.headers['Content-Type'] = 'application/json'
    self.response.out.write(json.dumps(pos))
          
    
class ListHandler(webapp2.RequestHandler):    
  def get(self):
    posts = db.GqlQuery("SELECT * FROM BlogPost ORDER BY created DESC")
    template = jinja_env.get_template('list.html')
    self.response.out.write(template.render(posts=posts))
#    self.render("list.html", subject=subject, content=content, created=created, posts=posts)
      
app = webapp2.WSGIApplication([('/blog/signup', SignHandler), ('/blog/signup/welcome', WelcomeHandler), ('/blog/login', LoginHandler), ('/blog/logout', LogoutHandler),(r'/blog/newpost', MainPage), (r'/blog/(\d+)', PakhandiHandler), (r'/blog/(\d+).json', PermJsonHandler),(r'/blog/.json', AllJsonHandler),(r'/blog', ListHandler)],debug=True)
