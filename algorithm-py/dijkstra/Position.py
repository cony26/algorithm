import numpy as np
import matplotlib.pyplot as plt

class Position:
    X_RANGE = 100
    Y_RANGE = 100

    def __init__(self, x, y):
        self.x = x
        self.y = y

    @classmethod
    def createPositions(cls):
        x = np.random.randint(0, Position.X_RANGE, 100)
        y = np.random.randint(0, Position.Y_RANGE, 100)

        positions = []
        for i in range(100):
            positions.append(Position(x[i], y[i]))

        return positions

    def printSelf(self):
        msg = f"[x,y] = {self.x}, {self.y}"
        print(msg)

    @classmethod
    def plotPositions(cls, positions):
        X = []
        Y = []
        for position in positions:
            X.append(position.x)
            Y.append(position.y)
        plt.scatter(X,Y)
        plt.show()
