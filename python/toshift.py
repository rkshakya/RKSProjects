import os
import pyodbc
import shift_planning
import datetime
import time

#
# script to populate clockin/clockout entries from tblTimes table of Access DB to shiftplanning via API
#
# Author : Ravi Kishor Shakya
#
# Please read the README file bundled with the script before running it
#


# IMPORTANT : Please make shiftplanning related settings here
key = "SET YOUR API KEY HERE"
username = 'YOUR USERNAME'
pwd = 'YOUR PASSWORD'
s = shift_planning.ShiftPlanning(key,username,pwd)
s.do_login()

#db_path = os.path.join(".",  "timeclock.mdb")

# IMPORTANT : Please provide location of the access .mdb file below
db_path = './timeclock.mdb'
#conn = pyodbc.connect('DRIVER={Microsoft Access Driver (*.mdb)};DBQ='+DBfile)
#use below conn if using with Access 2007, 2010 .accdb file
odbc_connection_str = 'DRIVER={MDBTools};DBQ=%s;' % (db_path)
conn = pyodbc.connect(odbc_connection_str)
cursor = conn.cursor()

#read last row index from index.txt file
file = open('index.txt', 'r')
index = file.readline()
print 'index ', index
index = int(index)
file.close() 

#file for logging
ts = time.time()
st = datetime.datetime.fromtimestamp(ts).strftime('%Y_%m_%d_%H_%M_%S')
logfilename = 'log_%s.txt' % (st)
logfile = open(logfilename, 'w')

SQL = 'SELECT lngID, lngEmployeeID, datEvent, blnEventType FROM tblTimes WHERE lngID > %d' % (index)
for row in cursor.execute(SQL): 
    print row.lngID, row.lngEmployeeID, row.datEvent, row.blnEventType
    
    # call shiftplanning API 
    if row.blnEventType == True:
        mod = 'timeclock.clockin'
        state = 'clock_in_time'
    else:
        mod = 'timeclock.clockout'
        state = 'clock_out_time'
          
    params = {
            'module':mod,
            'method':'GET',
            'employee':row.lngEmployeeID,
             '%s' % (state) : '%s' % (row.datEvent)
        }
    resp = s.perform_request(params)
    #print "RESP " , resp

    try:
        if resp.has_key('error'):
            logfile.write('API call FAILED for lngID : %d\n' % (row.lngID) )
        else:
            logfile.write('API call SUCCESS for lngID : %d\n' % (row.lngID) )
    except:  
        logfile.write('API call SUCCESS for lngID : %d\n' % (row.lngID) )


logfile.close()
    
index = row.lngID

file = open('index.txt', 'w')
file.write("%d\n" % (index))  
file.close() 

cursor.close()
conn.close()
