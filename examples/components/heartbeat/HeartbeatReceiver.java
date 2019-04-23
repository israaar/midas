package examples.components.heartbeat;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import midas.interfaces.*;

/**
 * Listens for heartbeat messages from a specific sender
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class HeartbeatReceiver extends Component {
    private final int timeoutMillis;
    public final UUID id;

    private TimeoutListener onTimeout;
    private boolean isCancelled = false;

    private HeartbeatReceiver(Dispatcher dispatcher, UUID id, int timeoutMillis, TimeoutListener onTimeout) {
        super(dispatcher);

        this.id = id;
        this.timeoutMillis = timeoutMillis;
        this.onTimeout = onTimeout;
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
    @Override
    protected void run() {
        while (!this.isCancelled) {
            try {
                Thread.sleep(timeoutMillis);

                if (this.onTimeout != null) {
                    this.onTimeout.onTimeout();
                }

                break;
            } catch (InterruptedException ex) {}
        }
    }

    private void onHeartbeatReceived(Message<String> message) {
        if (message.id.equals(id)) {
            thread.interrupt();
        }
    }

    public static interface TimeoutListener {
        public void onTimeout();
    }

    public static class Dispatcher extends midas.interfaces.Dispatcher<HeartbeatReceiver> {
        /**
         * Starts a heartbeat
         *
         * @param id  An id that HeartbeatListeners can listen for in order to
         *            communicate specifically with this node
         * @param timeoutMillis  The time to wait for heartbeat before timeout
         * @return  a started invocation
         */
        public HeartbeatReceiver dispatch(UUID id, int timeoutMillis, TimeoutListener onTimeout) {
            return new HeartbeatReceiver(this, id, timeoutMillis, onTimeout);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message<?> message) {
            for (HeartbeatReceiver i : getInvocations()) {
                i.onHeartbeatReceived((Message<String>) message);
            }
        }

        @Override
        public boolean shouldHandleMessage(Message<?> m) {
            return HeartbeatSender.Dispatcher.class.getCanonicalName().equals(m.dispId);
        }
    }
}
