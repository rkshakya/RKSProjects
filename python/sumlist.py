#Define a procedure, sum_list,
#that takes as its input a
#List of numbers, and produces
#as its output the sum of all
#the elements in the input list.

#For example,
#sum_list([1,7,4]) => 12

def sum_list(li):
    su = 0
    for e in li:
        su = su + e
    print su

sum_list([1,7,4, 7])
