def product_list(plist):
    prod = 0
    if len(plist) > 0:
        prod = 1
        for el in plist:
            prod = prod * el
    return prod

print product_list([])
