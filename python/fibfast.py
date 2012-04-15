def fibonacci(n):
  if n == 0:
    return 0
  elif n == 1:
    return 1
  else:
    f0 = 0
    f1 = 1
    while f1 < n:
        inter = f0 + f1
        f0 = f1
        f1 = inter
    return inter
    
print fibonacci(36)
