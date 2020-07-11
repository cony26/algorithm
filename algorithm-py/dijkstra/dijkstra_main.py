from dijkstra.Position import *
from dijkstra.Node import *

def isContinue():
    return False

nodes = Node.createNodes()
connection = Node.createConnection(nodes)
print(connection)

