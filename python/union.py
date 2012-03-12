#Define a procedure, union,
#that takes as inputs two lists.
#It should modify the first input
#list to be the set union of the two
#lists.

#a = [1,2,3]
#b = [2,4,6]
#union(a,b)
#a => [1,2,3,4,6]
#b => [2,4,6]

def union(alist, blist):
    for e in blist:
         if e not in alist:
            alist.append(e)
    return alist

print union([1,2,3], [2,4,6])
         
               
