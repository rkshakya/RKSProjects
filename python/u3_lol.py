def total_enrollment(plist):
    tenroll = 0
    tcost = 0
    for el in plist:
         tenroll = tenroll + el[1]
         tcost = tcost + el[1] * el[2]
    return tenroll, tcost

print total_enrollment([['Udacity', 90000, 10],
['KU', 10000, 34]])
