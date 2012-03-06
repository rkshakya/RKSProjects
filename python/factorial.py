def print_multiplication_table(n):
    outer = 1
    while outer <= n:
        inner = 1
        while inner <= n:
            print str(outer) + " * " + str(inner) + " = " + str(outer*inner)
            inner = inner + 1
        outer = outer + 1
    

print_multiplication_table(2)
