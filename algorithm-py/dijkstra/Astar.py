import time

import matplotlib.pyplot as plt

from dijkstra.algorithm_module import *

class Astar:
    def __init__(self):
        self.__nodes = Node.create_nodes()
        self.__open_list = list()
        self.__open_list.append(Node.START_NODE)
        Node.START_NODE.status = Node.STATUS.OPEN

        self.__next_node = None
        self.__close_list = list()
        self.__connection = Node.create_connection(self.__nodes)
        self.__count = 0
        Presenter.plot_nodes(self.__nodes)
        Presenter.plot_line(self.__nodes, self.__connection)

    def is_continue(self):
        if len(self.__open_list) == 0:
            print("not reached")
            return False

        self.__next_node = self.__get_next_node()
        if self.__next_node is Node.END_NODE:
            Presenter.plot_nodes(self.__nodes)
            Presenter.plot_highLight()
            return False

        return True

    def process(self):
        self.__open_list.remove(self.__next_node)
        self.__close_list.append(self.__next_node)
        self.__next_node.status = Node.STATUS.CLOSE

        for node in self.__next_node.connected_nodes:
            estimate = self.__get_estimate_cost(self.__next_node, node)
            if (not (node in self.__open_list)) and (not (node in self.__close_list)):
                node.cost = estimate
                self.__open_list.append(node)
                node.prev_node = self.__next_node
                node.status = Node.STATUS.OPEN
            elif node in self.__open_list:
                prev_cost = node.cost
                if estimate < prev_cost:
                    node.cost = estimate
                    node.prev_node = self.__next_node
            elif node in self.__close_list:
                prev_cost = node.cost
                if estimate < prev_cost:
                    node.cost = estimate
                    self.__close_list.remove(node)
                    self.__open_list.append(node)
                    node.prev_node = self.__next_node
                    node.status = Node.STATUS.OPEN

        self.__count += 1
        Presenter.plot_nodes(self.__nodes)
        time.sleep(0.1)

        print(self.__count)

    def __get_next_node(self):
        return min(self.__open_list, key = lambda node:node.cost)

    def __get_g_estimate(self, node):
        return node.cost - self.__get_heulistic_estimate(node)

    def __get_heulistic_estimate(self, node):
        return math.sqrt((node.position.x - Node.END_NODE.position.x)**2 + (node.position.y - Node.END_NODE.position.y)**2)

    def __get_estimate_cost(self, node, connected_node):
        return self.__get_g_estimate(node) + node.get_distance(connected_node) + self.__get_heulistic_estimate(connected_node)

def launch():
    astar = Astar()

    print("start Astar algorithm")
    while astar.is_continue():
        astar.process()
    print("end Astar algorithm")

launch()
