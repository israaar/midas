from pylab import *
import pandas as pd

filename = 'broadcast_simple_thread_data'
df=pd.read_csv('test_data/' + filename + '.csv', sep=',')
df['ts'] = (df['Time'] - df['Time'].iloc[0]) * 100000

fig, ax1 = plt.subplots()
lables = [10,50,100,250,500,750,900]
lablesXI = [2, 4, 8, 12, 16, 20, 23]
lablesY = [14, 64, 114, 264, 514, 764, 914]
for i in range(0, len(lables)):
    plt.annotate(
        str(lables[i]) + " Cps.",
        xy=(df['ts'].iloc[lablesXI[i]], lablesY[i]), xytext=(-5, 5),
        textcoords='offset points', ha='right', va='bottom',
        arrowprops=dict(arrowstyle = '->', connectionstyle='arc3,rad=0'))

ax2 = ax1.twinx()
ax1.plot(df['ts'], df['Live threads'], 'r-')
ax2.plot(df['ts'], df['Peak'], 'g-')

ax1.set_xlabel('Simulation Time (s)')
ax1.set_ylabel('Live Thread Count', color='r')
ax2.set_ylabel('Peak Thread Count', color='g')
title('Thread Usage')
grid(True)
plt.savefig(filename + '.png')
plt.clf()

filename = 'broadcast_simple_heap_memory'
df=pd.read_csv('test_data/' + filename + '.csv', sep=',')
df['ts'] = (df['Time'] - df['Time'].iloc[0]) * 100000
df['mb'] = (df['Used']) / 1000000
plot(df['ts'], df['mb'])

xlabel('Simulation Time (s)')
ylabel('Committed Memory (Mb)')
title('Heap Memory Usage')
plt.savefig(filename + '.png')
plt.clf()