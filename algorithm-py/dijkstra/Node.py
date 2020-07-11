from dijkstra.Position import *
import math

class Node:
    NODE_NUMBER = 100
    INITIAL_COST = 1000
    CONNECTED_LENGTH = 30

    def __init__(self, id, position):
        self.__id = id
        self.__position = position

    @classmethod
    def createNodes(cls):
        positions = Position.createPositions()
        Position.plotPositions(positions)
        nodes = []
        for i in range(Node.NODE_NUMBER):
            nodes.append(Node(i, positions[i]))

        return nodes

    @classmethod
    def createConnection(cls, nodes):
        connection = np.zeros((Node.NODE_NUMBER, Node.NODE_NUMBER), dtype = int)
        for a in nodes:
            for b in nodes:
                if a.getDistance(b) < Node.CONNECTED_LENGTH:
                    connection[a.__id, b.__id] = 1
                    connection[b.__id, b.__id] = 1

        return connection

    def setPrev(self, prev):
        self.__prev = prev

    def getPrev(self):
        return self.__prev

    def setCost(self, cost):
        self.__cost = cost

    def getCost(self):
        return self.__cost

    def setIsAlive(self, isAlive):
        self.__isAlive = isAlive

    def getIsAlive(self):
        return self.__isAlive

    def printNode(self):
        self.__position.printSelf()

    def getDistance(self, node):
        return math.sqrt((self.__position.x - node.__position.x)**2 + (self.__position.y - node.__position.y)**2)


