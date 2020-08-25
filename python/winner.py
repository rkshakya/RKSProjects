import random

def createRandomSortedList(num, start = 1, end = 100): 
    arr = [] 
    tmp = random.randint(start, end) 
      
    for x in range(num): 
          
        while tmp in arr: 
            tmp = random.randint(start, end) 
              
        arr.append(tmp) 
          
    arr.sort() 
      
    return arr 

store = StoreClient('ghfhfh')
store.increment_by('count', 1)
value = store.get('count') 

li = createRandomSortedList(60)

print(value)
print(li)

retString = 'loser'

if value in li:
	retString = 'winner'

if value >= 100:
	store.set('count', 1)

print(retString)
output = {"rem" : retString}
