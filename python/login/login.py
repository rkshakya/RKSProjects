import webapp2
import cgi 
import re

#regular expressions
USER_RE = re.compile(r"^[a-zA-Z0-9_-]{3,20}$")  #regex to validate username
PWD_RE = re.compile(r"^.{3,20}$") #regex to validate password
EMAIL_RE = re.compile(r"^[\S]+@[\S]+\.[\S]+$")  #regex to validate email

#validation messages
USR_MSG = "Invalid username"
PWD_MSG = "Invalid password"
VER_MSG = "Passwords mismatch"
EMAIL_MSG = "Invalid email"

def escape_html(s):
    return cgi.escape(s, quote = True)
    
def valid_username(username):
  return USER_RE.match(username)

def valid_password(password):
  return PWD_RE.match(password)  
  
def valid_email(email):
  return EMAIL_RE.match(email)  

form="""<html>
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
   
class MainPage(webapp2.RequestHandler):
  def write_form(self, errname = "", errpwd = "", errverify = "", erremail = "", username = "", email = ""):
    self.response.out.write(form %{"errname" : errname, "errpwd": errpwd, "errverify" : errverify, "erremail": erremail, "username": escape_html(username), "email": escape_html(email)})
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
            self.redirect("/hw2/login/welcome?username=" + uname)                            
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
        self.redirect("/hw2/login/welcome?username=" + uname) 
  
    
    
class WelcomeHandler(webapp2.RequestHandler):
  def get(self):
    self.response.out.write("Welcome, %s!" % self.request.get("username"))    
      
app = webapp2.WSGIApplication([('/hw2/login', MainPage), ('/hw2/login/welcome', WelcomeHandler)],
                              debug=True)
