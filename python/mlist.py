def proc3(p):
    q = p
    p.append(3)
    q.pop()
    return p

print proc3([1,4,5])
