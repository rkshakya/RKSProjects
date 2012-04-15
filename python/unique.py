#Procedures and If

#Define a procedure, unique, that takes three numbers as its inputs and returns
#the Boolean value True if all three numbers are different. Otherwise, it should
#return the Boolean value False.

def unique(a, b, c):
  if a == b:
    return False
  else:
    if b == c:
        return False
    else:
          if c == a:
            return False
          else:
            return True
            

#For example,

print unique(1, 2, 3)
#>>> True

print unique(1, 0, 1)
#>>> False

print unique(7, 7, 7)
#>>> False

print unique(3, 4, 3)
print unique(3, 3, 7)

