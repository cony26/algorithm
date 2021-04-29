import numpy as np
import math
import matplotlib.pyplot as plt
import random


def bernstein(i, n, t):
    return math.factorial(n) * (1 - t) ** (n - i) * t ** i / math.factorial(i) / math.factorial(n - i)


def curvature(xdot, x2dot, ydot, y2dot):
    return math.fabs(xdot * y2dot - x2dot * ydot) / (xdot ** 2 + ydot ** 2) ** (3 / 2)


t = np.linspace(0, 1, num=100)
dt = t[1] - t[0]
degree = 8

CP = []
CP.append([0, 0])
for i in range(degree - 1):
    CP.append([random.randint(round(100 * i / degree), round(100 * (i + 1) / degree)), random.randint(0, 100)])
CP.append([100, 100])

print(CP)

fig = plt.figure()
ax1 = fig.add_subplot(2, 2, 1)
ax2 = fig.add_subplot(2, 2, 2)
ax3 = fig.add_subplot(2, 2, 3)

# plot basic function
for i in range(degree + 1):
    ax1.plot(t, bernstein(i, degree, t))

# plot [x,y]
x = []
y = []
for tt in t:
    tmpx = 0
    tmpy = 0
    for i in range(degree + 1):
        tmpx += CP[i][0] * bernstein(i, degree, tt)
        tmpy += CP[i][1] * bernstein(i, degree, tt)
    # tmpx = 1 * math.cos(tt * 2 * math.pi)
    # tmpy = 1 * math.sin(tt * 2 * math.pi)
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
