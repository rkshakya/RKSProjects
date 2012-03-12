#Define a procedure, measure_udacity,
#that takes its input a list of Strings,
#and outputs a number that is a count
#of the number of elements in the input
#list that start with the letter 'U'
#(uppercase).

#For example,

#measure_udacity(['Dave','Sebastian','Katy']) => 0

#measure_udacity(['Umika','Umberto']) => 2

def measure_udacity(p):
    cn = 0
    for st in p:
        if st[0] == 'U':
            cn = cn + 1
    return cn

print measure_udacity(['Umika', 'Ravi'])
