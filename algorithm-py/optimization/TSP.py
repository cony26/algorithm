from pulp import *
import numpy as np
from itertools import product
import networkx as nx
MEPS = 1.0e-10

def TSPSolveSubtourElim(G,x,y):
    n = len(G.nodes())
    nodes = list(G.nodes())
    edges = [(nodes[i], nodes[j]) for (i,j) in product(range(n), range(n)) if nodes[i] < nodes[j]]
    D = np.sqrt((x.reshape(-1,1) - x)**2 + (y.reshape(-1,1) -y)**2)

    prob = LpProblem('TSP', LpMinimize)

    x = {(u,v): LpVariable('x'+str(u)+","+str(v),lowBound=0,cat='Binary') for (u,v) in edges}
    prob += lpSum(D[i,j]*x[i,j] for (i,j) in edges)
    for i in nodes:
        ss = [(j,i) for j in nodes if (j,i) in edges] + [(i,j) for j in nodes if (i,j) in edges]
        prob += lpSum(x[e] for e in ss) == 2, 'Eq'+str(i)

    prob.solve()
    subtours = []
    for (i,j) in edges:
        if x[i,j].varValue > MEPS:
            subtours.append([i,j])
    G.add_edges_from(subtours)

    CC = list(nx.connected_components(G))
    while len(CC) > 1:
        for S in CC:
            prob += lpSum(x[i,j] for (i,j) in edges if i in S and j in S) <= len(S) - 1
        prob.solve()

        G.remove_edges_from(subtours)
        subtours = []
        for (i,j) in edges:
            if x[i,j].varValue > MEPS:
                subtours.append([i,j])
        G.add_edges_from(subtours)
        CC = list(nx.connected_components(G))

    len_tour = 0
    for (u,v) in G.edges():
        len_tour += D[u,v]

    return len_tour


