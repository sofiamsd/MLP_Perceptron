import numpy as np
import matplotlib.pyplot as plt

xs = []
ys = []
labels = []
predictions = []

with open("./TestSet.txt") as file:
    for line in file.readlines():
        x, y, label = line.split()
        xs.append(float(x))
        ys.append(float(y))
        labels.append(int(label))

with open("./PredictedSet.txt") as file:
    for line in file.readlines():
        _, _, prediction = line.split()
        predictions.append(int(prediction))

xs = np.array(xs)
ys = np.array(ys)
labels = np.array(labels)
predictions = np.array(predictions)

colors = ["g","y","b","m"]

for l in range(4):
    index = (labels==predictions) & (labels==l)
    plt.scatter(xs[index], ys[index], c=colors[l], marker="+")

    index = (labels!=predictions) & (labels==l)

    plt.scatter(xs[index], ys[index], c=colors[l], marker="_")
    

plt.show()