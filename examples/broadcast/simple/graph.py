from pylab import *
import pandas as pd

filename = 'broadcast_simple_thread_data'
df=pd.read_csv('test_data/' + filename + '.csv', sep=',')
df['ts'] = df['Time'] - df['Time'].iloc[0]

fig, ax1 = plt.subplots()

ax2 = ax1.twinx()
ax1.plot(df['ts'], df['Live threads'], 'r-')
ax2.plot(df['ts'], df['Peak'], 'g-')

ax1.set_xlabel('Simulation Time (s)')
ax1.set_ylabel('Live Thread Count', color='r')
ax2.set_ylabel('Peak Thread Count', color='g')
grid(True)
plt.savefig(filename + '.png')
plt.clf()

filename = 'broadcast_simple_cpu_data'
df=pd.read_csv('test_data/' + filename + '.csv', sep=',')
df['ts'] = df['Time'] - df['Time'].iloc[0]
plot(df['ts'], df['CPU Usage'])
 
xlabel('Simulation Time (s)')
ylabel('Percentage')
title('CPU Usage')
grid(True)
plt.savefig(filename + '.png')
plt.clf()