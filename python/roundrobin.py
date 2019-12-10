store = StoreClient('ka')
store.increment_by('count', 1)
value = store.get('count') 

pathsdict = {
	"1" : "path1",
	"2" : "path2",
	"0" : "path3"
}

rem = value % 3

output = {"rem" : pathsdict[rem]}
