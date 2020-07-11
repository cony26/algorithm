from dijkstra.Position import *
from dijkstra.Node import *
import matplotlib.pyplot as plt

def isContinue():
    return False

nodes = Node.createNodes()
connection = Node.createConnection(nodes)

for i in range(Node.NODE_NUMBER):
    for j in range(i, Node.NODE_NUMBER):
        if connection[i][j] == 1:
            X = []
            Y = []
            X.append(nodes[i].position.x)
            X.append(nodes[j].position.x)
            Y.append(nodes[i].position.y)
            Y.append(nodes[j].position.y)
            plt.plot(X,Y)
