from pulp import *
from itertools import combinations
import numpy as np
import scipy.linalg as linalg

# prob = LpProblem(name='Lp-Sample', sense=LpMaximize)
# x1 = LpVariable('x1', lowBound=0.0)
# x2 = LpVariable('x2', lowBound=0.0)
# prob += 2*x1 + 3*x2
# prob += x1 + 3*x2 <= 9, 'ineq1'
# prob += x1 + x2 <= 4, 'ineq2'
# prob += x1 + x2 <= 6, 'ineq3'
# print(prob)
# prob.solve()
#
# print(LpStatus[prob.status])
# print('Optimal value =', value(prob.objective))
# for v in prob.variables():
#     print(v.name, '=', value(v))

# A = np.array([[3,1,2],[1,3,0],[0,2,4]])
# c = np.array([150,200,300])
# b = np.array([60,36,48])
# (m,n) = A.shape
# prob = LpProblem(name='Production', sense=LpMaximize)
# x = [LpVariable('x'+str(i+1), lowBound=0) for i in range(n)]
# prob += lpDot(c,x)
# for i in range(m):
#     prob += lpDot(A[i],x) <= b[i], 'ineq'+str(i)
# print(prob)
#
# prob.solve()
# print('Optimal value =', value(prob.objective))
# for v in prob.variables():
#     print(v.name, '=', value(v))
#
# X = np.array([v.varValue for v in prob.variables()])
# print(np.all(np.abs(b - np.dot(A,X)) <= 1.0e-5))

MEPS = 1.0e-10


def lp_RevisedSimplex(c, A, b):
    np.seterr(divide='ignore')
    (m, n) = A.shape
    AI = np.hstack((A, np.identity(m)))
    c0 = np.r_[c, np.zeros(m)]
    basis = [n + i for i in range(m)]
    nonbasis = [j for j in range(n)]

    while True:
        y = linalg.solve(AI[:, basis].T, c0[basis])
        cc = c0[nonbasis] - np.dot(y, AI[:, nonbasis])
        print('----------------')
        print(y)
        print(cc)

        if np.all(cc <= MEPS):
            x = np.zeros(n + m)
            x[basis] = linalg.solve(AI[:, basis], b)
            print('Optimal')
            print('Optimal value = ', np.dot(c0[basis], x[basis]))
            for i in range(m):
                print('x', i, '=', x[i])
            break
        else:
            s = np.argmax(cc)

        d = linalg.solve(AI[:, basis], AI[:, nonbasis[s]])
        if np.all(d <= MEPS):
            print('Unbounded')
            break
        else:
            bb = linalg.solve(AI[:, basis], b)
            ratio = bb / d
            ratio[ratio < -MEPS] = np.inf
            r = np.argmin(ratio)
            nonbasis[s], basis[r] = basis[r], nonbasis[s]


def lp_simplex(c, A, b):
    (m, n) = A.shape
    print(A, m, n)
    candidates = list(combinations(range(n), m))
    print(candidates)
    i = 0
    basis = None
    nonbasis = None
    x = None
    while True:
        basis = list(candidates[i])
        nonbasis = [j for j in range(n) if j not in basis]
        if np.linalg.det(A[:, basis]) == 0:
            print('singular')
            i += 1
            continue
        x = np.zeros(n)
        x[basis] = linalg.solve(A[:, basis], b)

        if np.all(x[basis] >= 0):
            print('basis:', basis)
            print('nonbasis:', nonbasis)
            print('B:', A[:, basis])
            print('basic feasible solution:', x[basis])
            break
        else:
            print('non basic feasible solution:', x[basis])
            i += 1
            if i == len(candidates):
                print('can\'t find basic feasible solution')
                return

    while True:
        print('nextB:', A[:, basis])
        Binv = np.linalg.inv(A[:, basis])
        y = np.dot(Binv.T, c[basis])
        cn = c[nonbasis] - np.dot(A[:, nonbasis].T, y)
        bbar = np.dot(Binv, b)
        if np.all(cn <= 0):
            print('Optimal')
            print('Optimal value=', np.dot(c[basis].T, bbar))
            x[basis] = linalg.solve(A[:, basis], b)
            print('x = ', x[basis])
            break
        else:
            index = 0
            for j in range(len(cn)):
                if cn[j] > 0:
                    index = j
                    break
            Nbar = np.dot(Binv, A[:, nonbasis])
            ak = Nbar[:, index]

            if np.all(ak <= 0):
                print('not bounded')
                break
            else:
                theta = np.inf
                indextheta = 0
                for j in range(len(bbar)):
                    if ak[j] > 0:
                        if theta > bbar[j] / ak[j]:
                            theta = bbar[j] / ak[j]
                            indextheta = j
                nonbasis[index], basis[indextheta] = basis[indextheta], nonbasis[index]


A = np.array([[1, 1, 1, 0, 0],
              [1, 3, 0, 1, 0],
              [2, 1, 0, 0, 1]])
b = np.array([6, 12, 10])
c = np.array([1, 2, 0, 0, 0])

# lp_RevisedSimplex(c, A, b)
lp_simplex(c.T, A, b.T)
