from itertools import combinations

from optimization import tree_search, TSP
from optimization.napsack import KnapsackProblemSolve
import numpy as np
import optimization.binpacking as bp
import networkx as nx
import matplotlib.pyplot as plt

# items = {1,2,3,4,5}
# c = {1:50, 2:40, 3:10, 4:70, 5:55}
# w = {1:7, 2:5, 3:1, 4:9, 5:6}
# capacity = 15

# ratio = {j:c[j]/w[j] for j in items}
# sItems = [key for key, val in sorted(ratio.items(), key=lambda x:x[1], reverse=True)]
#
# for j in sItems:
#     print('c[%d]/w[%d] = %lf' % (j,j,c[j]/w[j]))
#
# x={j:0 for j in sItems}
# cap = capacity
# for j in sItems:
#     if w[j] <= cap:
#         cap -= w[j]
#         x[j] = 1
# print(x)
# print('total price = ', sum(c[j]*x[j] for j in sItems))

# res = KnapsackProblemSolve(capacity, items, c, w)
# print('Optimal value = ', res[1])
# print('Optimal solution = ', res[2])

# capacity = 25
# items = set(range(10))
# np.random.seed(1)
# w = {i:np.random.randint(5,10) for i in items}
# w2 = [w[i] for i in items]
# print(w2)
#
# bp.binpacking(capacity, w)
# bp.binpacking2(capacity, w)

# G = nx.Graph()
# vlist = [1,2,3,4]
# elist = [(1,2),(1,3),(2,3),(2,4),(3,4)]
# G.add_nodes_from(vlist)
# G.add_edges_from(elist)
# G = nx.complete_graph(6)
# p = nx.spring_layout(G, iterations=100)
# plt.axis('off')
# nx.draw_networkx(G,pos=p,node_color='lightgray', node_size=300)

# G = nx.grid_2d_graph(4,3)
# nx.draw_networkx(G,pos={v:v for v in G.nodes()},node_color='lightgray', node_size=500)
#
# print('G vertex:', G.nodes())
# print('G vertex number:', G.number_of_nodes())
# print('vertices neighboring 1:', [v for v in nx.all_neighbors(G,1)])
#
# print('A =', nx.adjacency_matrix(G).toarray())
# print('M =', nx.incidence_matrix(G).toarray())

# G = nx.random_geometric_graph(100, 0.1)
# print('total degree:', sum(nx.degree(G,v) for v in G.nodes()))
# print('multiplied edge number:', 2*len(G.edges()))
# print('odd number:', len([v for v in G.nodes() if nx.degree(G,v)%2 == 1]))
# G = nx.path_graph(4)
# G.add_path([10,11,12])
# print(nx.is_connected(G))
# for c in nx.connected_components(G):
#     print(c)
# nx.draw_networkx(G)
from optimization.prim import prim

# weighted_elist = [('D', 'G', 195), ('D','R',130),('D','S',260),
#                   ('G','R',195),('G','N',166),('R','S',132),
#                   ('R','M',114),('R','N',227),('M','S',114),
#                   ('M','P',114),('M','N',166),('N','P',195),
#                   ('P','S',114)]
# p = {'D':(0,15), 'G':(11,19), 'N':(17,12), 'R':(6,9), 'M':(10,4), 'P':(15,0), 'S':(5,0)}
# G = nx.Graph()
# G.add_weighted_edges_from(weighted_elist)
# elbs = {(u,v):G[u][v]['weight'] for (u,v) in G.edges()}
#
# # mst = prim(G)
# print(nx.minimum_spanning_tree(G,algorithm='prim'))
# mst = nx.minimum_spanning_tree(G,algorithm='prim')
#
# nx.draw_networkx(G,pos=p,node_color='lightgray', node_size=500,width=1)
# nx.draw_networkx(G,pos=p,edgelist=mst.edges(), width=5)
# nx.draw_networkx_edge_labels(G,pos=p, edgelist=mst.edges(), edge_labels=elbs)

# G = nx.grid_2d_graph(4,3)
# p = {v:v for v in G.nodes()}
# G = nx.Graph()
# vlist = [1,2,3,4,5,6]
# elist = [(1,2),(1,3),(2,4),(3,5),(5,6)]
# G.add_nodes_from(vlist)
# G.add_edges_from(elist)
#
# nx.draw_networkx(G, node_color='lightgray', node_size=1300,with_labels=True)
# plt.axis('off')
# plt.show()
#
# # dfst = tree_search.depth_first_search(G)
# dfst = tree_search.breadth_first_search(G)
# DG = nx.DiGraph()
# DG.add_edges_from(dfst)
# nx.draw_networkx(DG, edgelist=dfst, node_color='lightgray', node_size=1300,with_labels=True)
# plt.axis('off')
# plt.show()

# tree_search.dijkstra()
# GR = nx.grid_2d_graph(3,3)
# GR.add_edges_from([((0,1),(1,2))])
# GR.add_edges_from([((1,0),(2,1))])
# nx.draw_networkx(GR,pos={v:v for v in GR.nodes()},node_color='lightgray', node_size=1300, with_labels=True)
#
# print(nx.is_eulerian(GR))
# ee = nx.eulerian_circuit(GR)
# for (i,j) in ee:
#     print(i, end='->')

# np.random.seed(1000)
# G = nx.grid_2d_graph(4,3)
# for (u,v) in G.edges():
#     G[u][v]['weight'] = np.random.randint(1,6)

# nx.draw_networkx(G, pos={v:v for v in G.nodes()}, node_color='lightgray', node_size=1500, width=1)
# nx.draw_networkx_edge_labels(G, edge_labels={(u,v):G[u][v]['weight'] for (u,v) in G.edges()}, pos={v:v for v in G.nodes()})

# Vodd = [v for v in G.nodes() if G.degree(v)%2 == 1]
# dist = dict(nx.all_pairs_dijkstra_path_length(G))
#
# K = nx.Graph()
# K.add_weighted_edges_from([(u,v,dist[u][v]) for (u,v) in combinations(Vodd,2)])
# # nx.draw_networkx(K,pos={v:v for v in K.nodes()}, node_color='lightgray', node_size=1500, width=1)
# # nx.draw_networkx_edge_labels(K,pos={v:v for v in K.nodes()}, edge_labels={(u,v):K[u][v]['weight'] for (u,v) in K.edges()})
#
# CK = K.copy()
# wm = max(CK[u][v]['weight'] for (u,v) in CK.edges())
# for (u,v) in K.edges():
#     CK[u][v]['weight'] = wm - CK[u][v]['weight'] + 1
#
# m = nx.max_weight_matching(CK, maxcardinality=True)
# md = dict(m)
# mm = []
# for (u,v) in md.items():
#     if(u,v) not in mm and (v,u) not in mm:
#         mm.append((u,v))

# nx.draw_networkx(CK,pos={v:v for v in CK.nodes()},node_color='lightgray', node_size=1500, width=1)
# nx.draw_networkx_edge_labels(CK,pos={v:v for v in CK.nodes()}, edge_labels={(u,v):CK[u][v]['weight'] for (u,v) in CK.edges()})
# nx.draw_networkx_edges(CK,pos={v:v for v in CK.nodes()},edgelist=mm,width=5)

# CG = G.copy()
# for (u,v) in mm:
#     dp = nx.dijkstra_path(G,u,v)
#     for i in range(len(dp) - 1):
#         (ux,uy) = dp[i]
#         (vx,vy) = dp[i+1]
#         if ux == vx:
#             wx = ux+0.3
#             wy = (uy+vy)/2.0
#         else:
#             wx = (ux+vx)/2.0
#             wy = uy+0.3
#         CG.add_edges_from([((ux,uy),(wx,wy)),((wx,wy),(vx,vy))])
#
# nx.draw_networkx(CG,pos={v:v for v in CG.nodes()}, node_color='lightgray', node_size=1500, width=1)
# plt.axis('off')
# plt.show()

# n = 100
# vlist = [i for i in range(n)]
# Tours = nx.Graph()
# Tours.add_nodes_from(vlist)
#
# np.random.seed(1234)
# x = np.random.randint(low=0, high=1000, size=n)
# y = np.random.randint(low=0, high=1000, size=n)
# p = {i: (x[i], y[i]) for i in range(n)}
#
# TSP.TSPSolveSubtourElim(Tours,x,y)
# nx.draw_networkx(Tours,pos=p,node_color='k',node_size=10, with_labels=False)
# plt.axis('off')
# plt.show()
# G = nx.DiGraph()
# G.add_edge(1,2,capacity=4)
# G.add_edge(1,3,capacity=5)
# G.add_edge(2,3,capacity=2)
# G.add_edge(2,4,capacity=2)
# G.add_edge(3,4,capacity=2)
# G.add_edge(3,5,capacity=3)
# G.add_edge(4,5,capacity=4)
# G.add_edge(4,6,capacity=3)
# G.add_edge(5,6,capacity=5)
#
# val, flowdict = nx.maximum_flow(G,1,6)
# print('maxflow:', val)
# for u,v in G.edges():
#     print((u,v),':',flowdict[u][v])
#
# print('minimum cut:', nx.minimum_cut(G,1,6))

G = nx.grid_2d_graph(3,3)
for (u,v) in G.edges():
    G[u][v]['weight'] = np.random.randint(1,10)
elbs = {(u,v):G[u][v]['weight'] for (u,v) in G.edges()}
pos = {v: v for v in G.nodes()}

M = nx.maximal_matching(G)
mw = nx.max_weight_matching(G)
# MW = set(mw.)

nx.draw_networkx(G,pos=pos,node_color='lightgray',node_size=1000, width=1)
nx.draw_networkx_edges(G,pos=pos,edgelist=mw, width=5)
nx.draw_networkx_edge_labels(G,pos=pos,edge_labels=elbs)
plt.axis('off')
plt.show()