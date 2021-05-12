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
        Binv = np.linalg.inv(A[:, basis])
        x[basis] = np.dot(Binv, b)

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
            print('x = ', x)
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

def onePhasesimplex(c, A, b):
    (m, n) = A.shape
    basis = [j for j in range(m)]
    nonbasis = [m + j for j in range(n - m)]
    x = np.zeros(n)

    return searchFromBasis(c, A, b, basis, nonbasis)

def searchFromBasis(c, A, b, basis, nonbasis):
    (m,n) = A.shape
    x = np.zeros(n)
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
            print('x = ', x)
            return basis, nonbasis
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

    return None

def twoPhaseSimplex(c, A, b):
    min = np.min(b)
    if min < 0:
        print('x = 0 is not feasible solution')
        print('search with two phase simplex method')
        (m,n) = A.shape
        x0 = np.ones(m).reshape(-1,1)
        AA = np.hstack((A, x0))
        cc = np.hstack((np.zeros(n), np.array([-1,])))
        basis, nonbasis = onePhasesimplex(cc, AA, b)

        if n in basis:
            basis.remove(n)
        if n in nonbasis:
            nonbasis.remove(n)

        print('[feasible basis list]')
        print('basis:',basis)
        print('nonbasis:', nonbasis)
        searchFromBasis(c, A, b, basis, nonbasis)


# A = np.array([[1, 1, 1, 0, 0],
#               [1, 3, 0, 1, 0],
#               [2, 1, 0, 0, 1]])
# b = np.array([6, 12, 10])
# c = np.array([1, 2, 0, 0, 0])
# A = np.array([[1, 1, 1, 0, 0, 0],
#               [3, 4, 6, 0, 1, 0],
#               [4, 5, 3, 0, 0, 1]])
# b = np.array([20, 100, 100])
# c = np.array([4, 8, 10, 0, 0, 0])

# A = np.array([[2, 2, -1, 1, 0, 0],
#               [3, -2, 1, 0, 1, 0],
#               [1, -3, 1, 0, 0, 1]])
# b = np.array([10, 10, 10])
# c = np.array([1, 3, -1, 0, 0, 0])

# A = np.array([[1, 0, 1, 0],
#               [20, 1, 0, 1]])
# b = np.array([1, 100])
# c = np.array([10, 1, 0, 0])

# A = np.array([[-1, -4, -2, 1, 0],
#               [-3, -2, 0, 0, 1]])
# b = np.array([-8, -6])
# c = np.array([-2, -3, -1, 0, 0])

# A = np.array([[2, -1, 2, 1, 0, 0],
#               [2, -3, 1, 0, 1, 0],
#               [-1, 1, -2, 0, 0, 1]])
# b = np.array([4, -5, -1])
# c = np.array([1, -1, 1, 0, 0, 0])

A = np.array([[1, -1, 1, 0, 0],
              [-1, -1, 0, 1, 0],
              [2, 1, 0, 0, 1]])
b = np.array([-1, -3, 2])
c = np.array([3, 1, 0, 0, 0])

# lp_RevisedSimplex(c, A, b)
# lp_simplex(c.T, A, b.T)

twoPhaseSimplex(c.T, A, b.T)
