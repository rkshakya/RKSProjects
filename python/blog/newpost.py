import os
import webapp2
import cgi 
import re
import jinja2
from google.appengine.ext import db

template_dir = os.path.join(os.path.dirname(__file__), 'templates')
jinja_env = jinja2.Environment(loader = jinja2.FileSystemLoader(template_dir), autoescape=True)

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
    
class ListHandler(webapp2.RequestHandler):    
  def get(self):
    posts = db.GqlQuery("SELECT * FROM BlogPost ORDER BY created DESC")
    template = jinja_env.get_template('list.html')
    self.response.out.write(template.render(posts=posts))
#    self.render("list.html", subject=subject, content=content, created=created, posts=posts)
      
app = webapp2.WSGIApplication([(r'/blog/newpost', MainPage), (r'/blog/(\d+)', PakhandiHandler), (r'/blog', ListHandler)],
                              debug=True)
