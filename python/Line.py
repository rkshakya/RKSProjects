class Line(object):
    def __init__(self, coor1, coor2):
        self.coor2 = coor2
        self.coor1 = coor1
        
        print 'carp'

        for x, y in coor1:
            self.x1 = x
            self.y1 = y

        for x, y in coor2:
            self.x2 = x
            self.y2 = y
            
        print self.coor2
        print self.coor1    

    def distance(self):
        print ( (self.y2-self.y1)**2 + (self.x2-self.x1)**2 )**0.5

    def slope(self):
        print (self.y2-self.y1/float(self.x2-self.x1))