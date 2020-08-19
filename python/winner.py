import random

store = StoreClient('hfjsdhfd')
store.increment_by('count', 1)
value = store.get('count') 

if store.get('li') is None:
	randomList = []
	for i in range(0, 60):
		randomList.append(random.randint(0, 100))
	store.set('li', randomList)	

li = store.get('li')

print(value)
print(li)

retString = 'loser'

if value in li:
	retString = 'winner'

if value >= 100:
	store.set('count', 1)

print(retString)
output = {"rem" : retString}
