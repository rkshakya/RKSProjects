#Creating an Empty Hash Table
#Define a procedure, make_hashtable,
#that takes as input a number, nbuckets,
#and outputs an empty hash table with
#nbuckets empty buckets.

def make_hashtable(nbuckets):
    finlist = []
    for cnt in range(nbuckets):
        finlist.append([])
   #print len(finlist)
    return finlist

make_hashtable(20)
        
