from pulp import *
import numpy as np

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

A = np.array([[3,1,2],[1,3,0],[0,2,4]])
c = np.array([150,200,300])
b = np.array([60,36,48])
(m,n) = A.shape
prob = LpProblem(name='Production', sense=LpMaximize)
x = [LpVariable('x'+str(i+1), lowBound=0) for i in range(n)]
prob += lpDot(c,x)
for i in range(m):
    prob += lpDot(A[i],x) <= b[i], 'ineq'+str(i)
print(prob)

prob.solve()
print('Optimal value =', value(prob.objective))
for v in prob.variables():
    print(v.name, '=', value(v))

X = np.array([v.varValue for v in prob.variables()])
print(np.all(np.abs(b - np.dot(A,X)) <= 1.0e-5))
