def bigger(a,b):
    if a > b:
        return a
    else:
        return b
        
def biggest(a,b,c):
    return bigger(a,bigger(b,c))

def median(x,y,z):
    m = bigger(x,y)
    n = bigger(y,z)
    o = bigger(z,x)
    bigst = biggest(x,y,z)
    if m != bigst:
        return m
    elif n != bigst:
        return n
    else:
        return o

print median(2, 2, 2)
