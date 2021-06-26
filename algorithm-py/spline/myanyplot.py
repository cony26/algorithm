import matplotlib.pyplot as plt
import numpy as np
import math

x = np.arange(0, 20, 1)
y1 = 2**x * x**2
y2 = list()
for i in x:
   y2.append(math.factorial(i))
plt.plot(x, y1, label='dp')
plt.plot(x,y2,label='factorial')
plt.yscale('log')
plt.legend()
plt.show()