#Define a procedure, add_to_index,
#that takes 3 inputs:

# - an index: [[<keyword>,[<url>,...]],...]
# - a keyword: String
# - a url: String

#If the keyword is already
#in the index, add the url
#to the list of urls associated
#with that keyword.

#If the keyword is not in the index,
#add an entry to the index: [keyword,[url]]

index = []


#add_to_index(index,'udacity','http://udacity.com')
#add_to_index(index,'computing','http://acm.org')
#add_to_index(index,'udacity','http://npr.org')
#print index => [['udacity', ['http://udacity.com', 'http://npr.org']], ['computing', ['http://acm.org']]]

def add_to_index(index,keyword,url): 
    #iterate over all the elements
    if len(index) > 0:
        for elem in index:
            if keyword in elem:
                #print "1st part"
                thislist = elem
                thislist[1].append(url)
                break;
            else:
                #print "2nd part"
                index.append([keyword,[url]])
                break;
    else:
        #print "3rd part"
        index.append([keyword,[url]])
    return index
#add_to_index(index,'udacity','http://udacity.com')
#add_to_index(index,'computing','http://acm.org')
#print add_to_index(index,'udacity','http://npr.org')

