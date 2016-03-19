import sys
class Node:
    def __init__(self,data):
        self.right=self.left=None
        self.data = data
class Solution:
    def insert(self,root,data):
        if root==None:
            return Node(data)
        else:
            if data<=root.data:
                cur=self.insert(root.left,data)
                root.left=cur
            else:
                cur=self.insert(root.right,data)
                root.right=cur
        return root

    def levelOrder(self,root):
         #Write your code here
        q = []
        q.append(root)
        result = []
            
        while len(q) > 0:
            result.append(q[0].data)
            node = q.pop(0)
                
            if node.left is not None:
                q.append(node.left)
                    
            if node.right is not None:
                q.append(node.right)
        print " ".join(map(str, result))

T=int(raw_input())
myTree=Solution()
root=None
for i in range(T):
    data=int(raw_input())
    root=myTree.insert(root,data)
myTree.levelOrder(root)