def greatest(plist):
    retval = 0
    if len(plist) > 0:
         for el in plist:
              if el > retval:
                 retval = el
    else:
        retval = 0
    return retval

print greatest([2, 6, 88, 54])
