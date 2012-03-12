#Define a procedure, find_element,
#using index that takes as its
#inputs a List and a value of any
#type, and outputs the index of
#the first element in the input
#list that matches the value.

#If there is no matching element,
#output -1.

#find_element([1,2,3],3) => 2

#find_element(['alpha','beta'],'gamma') => -1

def find_element(plist, tgt):
    idx = -1
    try:
        idx = plist.index(tgt)
    except:
        idx = -1
    return idx

print find_element([1,2,3, 4], 3)

