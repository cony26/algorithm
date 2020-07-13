import time

import matplotlib.pyplot as plt

from dijkstra.dikstra_module import *

class Dijkstra:
    def __init__(self):
        Position.create_positions()
        self.__nodes = Node.create_nodes()
        self.__candidates = self.__nodes.copy()
        self.__removed_nodes = list()
        self.__connection = Node.create_connection(self.__nodes)
        self.__count = 0
        self.__next_node = None
        Presenter.plot_nodes(self.__nodes)
        Presenter.plot_line(self.__nodes, self.__connection)
        Node.START_NODE.cost = 0

    def is_continue(self):
        if len(self.__candidates) == 0:
            Presenter.plot_highLight()
            return False

        if len([node for node in Node.END_NODE.connected_nodes if node.is_node_alive is True]) == 0:
            Node.END_NODE.is_node_alive = False
            Presenter.plot_nodes(self.__nodes)
            Presenter.plot_highLight()
            return False

        connected_nodes = Node.candidates_connected_removed_nodes(self.__candidates, self.__removed_nodes)
        if (self.__count > 0) and (len(connected_nodes) == 0) and (Node.END_NODE.prev_node is None):
            print("not reached")
            return False

        if self.__count == 0:
            self.__next_node = min(self.__candidates, key = lambda node:node.cost)
        else:
            self.__next_node = min(connected_nodes, key = lambda node:node.cost)

        self.__next_node.is_node_alive = False
        self.__candidates.remove(self.__next_node)
        self.__removed_nodes.append(self.__next_node)
        return True

    def process(self):
        for node in self.__next_node.connected_nodes:
            if(node.cost > self.__next_node.cost + node.get_distance(self.__next_node)):
                node.cost = self.__next_node.cost + node.get_distance(self.__next_node)
                node.prev_node = self.__next_node
                print(str(node) + "is updated")
            else:
                print(str(node) + "is NOT updated")

        self.__count += 1
        Presenter.plot_nodes(self.__nodes)
        time.sleep(0.01)

def launch():
    dijkstra = Dijkstra()

    while(dijkstra.is_continue()):
        dijkstra.process()


launch()
