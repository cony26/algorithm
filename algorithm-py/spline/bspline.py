import numpy as np
import math
import matplotlib.pyplot as plt
import random


def basicFunction(i, k, knotv, t):
    # print(i,k,knotv,t)
    if k == 0:
        if (knotv[i] <= t) and (t <= knotv[i + 1]):
            return 1
        else:
            return 0

    if basicFunction(i, k-1, knotv, t) == 0 and basicFunction(i+1, k-1, knotv, t) == 0:
        return 0
    elif basicFunction(i, k-1, knotv, t) == 0:
        return (knotv[i+k+1] - t) / (knotv[i+k+1] - knotv[i+1]) * basicFunction(i+1, k-1, knotv, t)
    elif basicFunction(i+1, k-1, knotv, t) == 0:
        return (t - knotv[i]) / (knotv[i+k] - knotv[i]) * basicFunction(i, k-1, knotv, t)
    else:
        if t == knotv[i]:
            return (knotv[i+k+1] - t) / (knotv[i+k+1] - knotv[i+1]) * basicFunction(i+1, k-1, knotv, t)
        elif knotv[i+k+1] == t:
            return (t - knotv[i]) / (knotv[i+k] - knotv[i]) * basicFunction(i, k-1, knotv, t)
        else:
            return (t - knotv[i]) / (knotv[i+k] - knotv[i]) * basicFunction(i, k-1, knotv, t) + (knotv[i+k+1] - t) / (knotv[i+k+1] - knotv[i+1]) * basicFunction(i+1, k-1, knotv, t)
#     (1 - knotv[5]) / (knotv[8] - knotv[5]) * basic + (knotv[9] - 1 ) / (knotv[9] - knot[6]) * basic


def curvature(xdot, x2dot, ydot, y2dot):
    return math.fabs(xdot * y2dot - x2dot * ydot) / (xdot ** 2 + ydot ** 2) ** (3 / 2)


t = np.linspace(0, 1, num=100)
dt = t[1] - t[0]

degree = 3
# knotv should have over cpnum + degree + 1
# knotv = [0.0, 0.0, 0.0, 0.0, 0.1, 0.15, 0.2, 0.3, 0.4, 0.5, 0.6, 0.8, 0.9, 1.0, 1.0, 1.0, 1.0]
knotv = [0, 0, 0, 0, 0.2, 0.4, 0.6, 0.8, 1, 1, 1, 1]
# knotv = [0, 0, 0, 0, 0.5, 0.5, 0.5, 0.5, 1, 1, 1, 1]
M = len(knotv) - 1
cp_num = M - degree

CP = []
CP.append([0, 0])
for i in range(cp_num - 2):
    CP.append([random.randint(round(100 * i / cp_num), round(100 * (i + 1) / cp_num)), random.randint(0, 100)])
CP.append([100, 100])

print(CP)

fig = plt.figure()
ax1 = fig.add_subplot(2, 2, 1)
ax2 = fig.add_subplot(2, 2, 2)
ax3 = fig.add_subplot(2, 2, 3)

# plot basic function
for i in range(M - degree):
    y = []
    for tt in t:
        y.append(basicFunction(i, degree, knotv, tt))
    ax1.plot(t, y)


# plot [x,y]
x = []
y = []
for tt in t:
    tmpx = 0
    tmpy = 0
    for i in range(cp_num):
        tmpx += CP[i][0] * basicFunction(i, degree, knotv, tt)
        tmpy += CP[i][1] * basicFunction(i, degree, knotv, tt)
        print(i, degree, tt, basicFunction(i, degree, knotv, tt))
    x.append(tmpx)
    y.append(tmpy)

ax2.plot(x, y)
ax2.set_aspect('equal')
for p in CP:
    ax2.scatter(p[0], p[1])

# calculate curvature
x_dot = np.gradient(x, dt)
x_2dot = np.gradient(x_dot, dt)
y_dot = np.gradient(y, dt)
y_2dot = np.gradient(y_dot, dt)

kappa = []
for i in range(len(x_dot)):
    kappa.append(curvature(x_dot[i], x_2dot[i], y_dot[i], y_2dot[i]))
# print(kappa)

ax3.plot(t, kappa)

plt.show()
