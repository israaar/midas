package middleman.implementations.components.heartbeat;

import java.util.concurrent.ConcurrentHashMap;

import middleman.interfaces.*;

/**
 * Listens for heartbeat messages from a specific sender
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class HeartbeatReceiver extends Component<String> {
    private ConcurrentHashMap<Invocation, Object> activeInvocations = new ConcurrentHashMap<>();

    /**
     * Starts a heartbeat
     *
     * @param id  An id that HeartbeatListeners can listen for in order to
     *            communicate specifically with this node
     * @param timeoutMillis  The time to wait for heartbeat before timeout
     * @return  a started invocation
     */
    public Invocation start(String id, int timeoutMillis, TimeoutListener onTimeout) {
        return new Invocation(id, timeoutMillis, onTimeout);
    }

    @Override
    public void onMessageReceived(Message<String> message) {
        for (Invocation i : activeInvocations.keySet()) {
            i.onMessageReceived(message);
        }
    }

    @Override
    public boolean shouldHandleMessage(Message<?> m) {
        return HeartbeatSender.class.getCanonicalName().equals(m.compId);
    }

    public static interface TimeoutListener {
        public void onTimeout();
    }

    public class Invocation implements MessageReceiveListener<String> {
        private final int timeoutMillis;
        private final String id;

        private final Thread thread;
        private TimeoutListener onTimeout;
        private boolean isCancelled = false;

        public Invocation(String id, int timeoutMillis, TimeoutListener onTimeout) {
            this.id = id;
            this.timeoutMillis = timeoutMillis;
            this.onTimeout = onTimeout;
            this.thread = new Thread(this::run);

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
            activeInvocations.put(this, null);

            while (!this.isCancelled) {
                try {
                    Thread.sleep(timeoutMillis);

                    if (this.onTimeout != null) {
                        this.onTimeout.onTimeout();
                    }

                    break;
                } catch (InterruptedException ex) {}
            }

            activeInvocations.remove(this);
        }

        @Override
        public void onMessageReceived(Message<String> message) {
            if (message.payload.equals(id)) {
                thread.interrupt();
            }
        }
    }
}
