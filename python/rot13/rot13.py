import webapp2
import cgi 

def escape_html(s):
    return cgi.escape(s, quote = True)

form="""<html>
                <title>ROT13 encoding</title>
                <body>
                  Input text to convert to ROT13: <br/>
                  <form method="post">
                    <textarea name="text">%(val)s</textarea>
                    <input type="submit">
                  </form>
                </body>
        </html>
          """
   
class MainPage(webapp2.RequestHandler):
  def write_form(self, val=""):
    self.response.out.write(form %{"val": escape_html(val)})
  def get(self):
    self.write_form()
  def post(self):
    va = self.request.get("text")
    newtext = va.encode('rot13')
    self.write_form(newtext)
      
      
app = webapp2.WSGIApplication([('/', MainPage)],
                              debug=True)
