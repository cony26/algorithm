def prim(G):
    V = [v for v in G.nodes()]
    n = len(V)
    T = []
    S = [V[0]]
    while len(S) < n:
        for u in S:
            print('G[' + u + '].items():', G[u].items())
        candidates = [(u,v,w['weight']) for u in S for v,w in G[u].items() if not(v in S)]
        (u,v,w) = min(candidates,key=lambda x:x[2])
        S += [v]
        T += [(u,v)]
    return T