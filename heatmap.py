import sys
import numpy as np
import matplotlib.pyplot as plt

data = np.random.random((32, 32))

plt.imshow(data, cmap=sys.argv[1], interpolation=sys.argv[2])
plt.show()
