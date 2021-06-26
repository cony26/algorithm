from pulp import *
import numpy as np

from optimization.napsack import KnapsackProblemSolve

MEPS = 1.0e-8

def binpacking(capacity, w):
    m = len(w)
    items = set(range(m))
    A = np.identity(m)

    solved = False
    columns = 0
    dual = LpProblem(name='D(K)', sense=LpMaximize)
    y = [LpVariable('y'+str(i), lowBound=0) for i in items]

    print('m', m)
    print('items ', items)
    print('w ', w)
    print('y ', y)

    dual += lpSum(y[i] for i in items)
    for j in range(len(A.T)):
        print('A.T ', A.T)
        print('A.T[j] ', A.T[j])
        dual += lpDot(A.T[j],y) <= 1, 'ineq'+str(j)

    while not(solved):
        dual.solve()

        costs = {i: y[i].varValue for i in items}
        weights = {i: w[i] for i in items}
        (state, val, sol) = KnapsackProblemSolve(capacity, items, costs, weights)

        if val >= 1.0+MEPS:
            print('val ', val)
            print('sol ', sol)
            a = np.array([int(sol[i]) for i in items])
            print('a ', a)
            dual += lpDot(a, y) <= 1, 'ineq'+str(m+columns)
            A = np.hstack((A, a.reshape(-1,1)))
            columns += 1
        else:
            solved = True

    print('Generated columns: ', columns)
    m, n = A.shape
    primal = LpProblem(name='P(K)', sense=LpMinimize)
    x = [LpVariable('x'+str(j), lowBound=0, cat='Binary') for j in range(n)]

    primal += lpSum(x[j] for j in range(n))
    for i in range(m):
        primal += lpDot(A[i], x) >= 1, 'ineq'+str(i)

    primal.solve()
    if value(primal.objective) - value(dual.objective) < 1.0-MEPS:
        print('Optimial solution found')
    else:
        print('Approximiated solution found:')

    K = [j for j in range(n) if x[j].varValue > MEPS]
    result = []
    itms = set(range(m))
    for j in K:
        J = {i for i in range(m) if A[i,j] > MEPS and i in itms}
        r = [w[i] for i in J]
        itms -= J
        result.append(r)

    print(result)

def binpacking2(capacity, w):
    n = len(w)
    items = range(n)
    bpprob = LpProblem(name='BinPacking2', sense=LpMinimize)
    z = [LpVariable('z'+str(j), lowBound=0, cat='Binary') for j in items]
    x = [[LpVariable('x'+str(i)+str(j), lowBound=0, cat='Binary') for j in items] for i in items]

    bpprob += lpSum(z[i] for i in items)
    for i in items:
        bpprob += lpSum(x[i][j] for j in items) == 1
    for j in items:
        bpprob += lpSum(x[i][j] * w[i] for i in items) <= capacity * z[j]

    bpprob.solve()
    result = []
    for j in items:
        if z[j].varValue > MEPS:
            r = [w[i] for i in items if x[i][j].varValue > MEPS]
            result.append(r)
    print(result)
