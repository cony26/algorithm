import matplotlib.pyplot as plt
import math
import numpy as np

class Node:
    NODE_NUMBER = 50
    INITIAL_COST = 1000
    CONNECTED_LENGTH = 30

    def __init__(self, id, position):
        self.__id = id
        self.__position = position
        self.__prev = None
        self.__is_node_alive = True
        self.__cost = Node.INITIAL_COST
        self.__connected_nodes = []

    def __str__(self):
        # return "f"Node[{self.__id}]""
        return "Node[" + str(self.__id) + "]"

    @classmethod
    def create_nodes(cls):
        positions = Position.create_positions()
        nodes = []

        for i in range(0, Node.NODE_NUMBER):
            nodes.append(Node(i, positions[i]))

        cls.START_NODE = nodes[0]
        cls.END_NODE = nodes[Node.NODE_NUMBER - 1]
        return nodes

    @classmethod
    def create_connection(cls, nodes):
        connection = np.zeros((Node.NODE_NUMBER, Node.NODE_NUMBER), dtype=int)
        for a in nodes:
            for b in nodes:
                if a.get_distance(b) < Node.CONNECTED_LENGTH:
                    connection[a.__id, b.__id] = 1
                    connection[b.__id, b.__id] = 1
                    a.connected_nodes.append(b)
            if len(a.connected_nodes) == 0:
                a.set_minimum_distance_node(nodes)

        return connection

    @property
    def START_NODE(cls):
        return cls.START_NODE

    @property
    def END_NODE(cls):
        return cls.END_NODE

    @property
    def prev_node(self):
        return self.__prev

    @prev_node.setter
    def prev_node(self, prev):
        self.__prev = prev

    @property
    def cost(self):
        return self.__cost

    @cost.setter
    def cost(self, cost):
        self.__cost = cost

    @property
    def is_node_alive(self):
        return self.__is_node_alive

    @is_node_alive.setter
    def is_node_alive(self, is_node_alive):
        self.__is_node_alive = is_node_alive

    @property
    def position(self):
        return self.__position

    @property
    def id(self):
        return self.__id

    @property
    def connected_nodes(self):
        return self.__connected_nodes

    def print_node(self):
        self.__position.printSelf()

    def get_distance(self, node):
        return math.sqrt((self.__position.x - node.__position.x) ** 2 + (self.__position.y - node.__position.y) ** 2)

    def is_within_range(self, node):
        if self is node:
            return False

        return self.get_distance(node) <= Node.CONNECTED_LENGTH

    def set_minimum_distance_node(self, nodes):
        min_node = None
        for node in nodes:
            if node is self:
                continue
            if min_node is None:
                min_node = node
            if self.get_distance(node) < self.get_distance(min_node):
                min_node = node
        self.__connected_nodes.append(min_node)
        min_node.connected_nodes.append(self)

    def get_connected_nodes_number(self):
        return len(self.__connected_nodes)

    @classmethod
    def candidates_connected_removed_nodes(cls, candidates, removed_nodes):
        connecting_nodes = list()
        for removed_node in removed_nodes:
            for node in removed_node.connected_nodes:
                if not (node in connecting_nodes):
                    connecting_nodes.append(node)

        connecting_candidates = list()
        for node in candidates:
            if node in connecting_nodes:
                connecting_candidates.append(node)

        return  connecting_candidates

class Position:
    X_RANGE = 100
    Y_RANGE = 100

    def __init__(self, x, y):
        self.__x = x
        self.__y = y

    @classmethod
    def create_positions(cls):
        x = np.random.randint(0, Position.X_RANGE, Node.NODE_NUMBER - 2)
        y = np.random.randint(0, Position.Y_RANGE, Node.NODE_NUMBER - 2)
        cls.START = Position(0,0)
        cls.END = Position(100, 100)

        positions = []
        positions.append(cls.START)
        for i in range(Node.NODE_NUMBER - 2):
            positions.append(Position(x[i], y[i]))
        positions.append(cls.END)

        return positions

    @classmethod
    def plot_positions(cls, positions):
        X = []
        Y = []
        for position in positions:
            X.append(position.x)
            Y.append(position.y)
        plt.scatter(X,Y)
        plt.show()

    def print_self(self):
        msg = f"[x,y] = {self.x}, {self.y}"
        print(msg)

    @property
    def START(cls):
        return cls.START

    @property
    def END(cls):
        return cls.END

    @property
    def x(self):
        return self.__x

    @property
    def y(self):
        return self.__y

class Presenter:
    @classmethod
    def plot_nodes(cls, nodes):
        x_alive = []
        x_not_alive = []
        y_alive = []
        y_not_alive = []
        for node in nodes:
            if(node.is_node_alive):
                x_alive.append(node.position.x)
                y_alive.append(node.position.y)
            else:
                x_not_alive.append(node.position.x)
                y_not_alive.append(node.position.y)
        plt.scatter(x_alive, y_alive, c="blue")
        plt.scatter(x_not_alive, y_not_alive, c="red")
        plt.pause(0.05)

    @classmethod
    def plot_line(cls, nodes, connection):
        for i in range(Node.NODE_NUMBER):
            for j in range(i, Node.NODE_NUMBER):
                if connection[i][j] == 1:
                    X = []
                    Y = []
                    X.append(nodes[i].position.x)
                    X.append(nodes[j].position.x)
                    Y.append(nodes[i].position.y)
                    Y.append(nodes[j].position.y)
                    plt.plot(X, Y, color = "black", linewidth = 1, alpha = 0.4)

    @classmethod
    def plot_highLight(cls):
        X = []
        Y = []
        node = Node.END_NODE
        while node is not None:
            X.append(node.position.x)
            Y.append(node.position.y)
            node = node.prev_node
        plt.plot(X, Y, color = "blue", linewidth = 3, alpha = 0.8)
        plt.pause(3)



