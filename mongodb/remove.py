import pymongo
import sys

# establish a connection to the database
connection = pymongo.Connection("mongodb://localhost", safe=True)

# get a handle to the school database
db=connection.photo
albms = db.albums
imgs = db.images
     
try:
       curs = imgs.find()      
        
except:
        print "Unexpected error:", sys.exc_info()[0]

imid = None

for image in curs:
       delflag = True
       imid = image.get("_id")
       
       acurs = albms.find()
       #iterate over albums
       for album in acurs:
              imarr = album.get("images")              
              
              if imid in imarr:
                     delflag = False
                     break
       
       if delflag == True:
              print "Removing image %d", imid
              imgs.remove({"_id" : imid})
              
       
       
                     
                     
                     
                     
        