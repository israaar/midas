package middleman.implementations.components.heartbeat;

import middleman.interfaces.*;

/**
 * Listens for heartbeat messages from a specific sender
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class HeartbeatReceiver extends Component<String> {
    private final int timeoutMillis;
    private final String id;

    private final Thread thread;
    private TimeoutListener onTimeout;
    private boolean isCancelled = false;

    /**
     *
     * @param id  An id to listen for. Only heartbeats with this id are counted
     * @param delayMillis  The time to pause between sending heartbeat messages
     */
    public HeartbeatReceiver(String id, int timeoutMillis) {
        this.id = id;
        this.timeoutMillis = timeoutMillis;

        this.thread = new Thread(this::run);
    }

    /**
     * Starts listening for the heartbeat
     *
     * @param onTimeout  a callback to be called when a timeout occurs
     */
    public void start(TimeoutListener onTimeout) {
        this.onTimeout = onTimeout;
        this.thread.start();
    }

    /**
     * Cancel the thread. Will stop listening for heartbeats.
     */
    public void cancel() {
        this.isCancelled = true;
        this.thread.interrupt();
    }

    /**
     * Private function for listening for heartbeat messages
     */
    private void run() {
        while (true) {
            try {
                Thread.sleep(timeoutMillis);

                if (this.onTimeout != null) {
                    this.onTimeout.onTimeout();
                }

                break;
            } catch (InterruptedException ex) {
                if (isCancelled) break;
            }
        }
    }

    @Override
    public void onMessageReceived(Message<String> message) {
        if (message.payload.equals(id)) {
            thread.interrupt();
        }
    }

    @Override
    public boolean shouldHandleMessage(Message<?> m) {
        return HeartbeatSender.class.getCanonicalName().equals(m.compId);
    }

    public static interface TimeoutListener {
        public void onTimeout();
    }
}
