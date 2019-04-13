package middleman.implementations.components.heartbeat;

import java.util.UUID;

import middleman.interfaces.*;

/**
 * Sends out heartbeat messages
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class HeartbeatSender extends Component<String> {
    /**
     * Starts a heartbeat
     *
     * @param id  An id that HeartbeatListeners can listen for in order to
     *            communicate specifically with this node
     * @param delayMillis  The time to pause between sending heartbeat messages
     * @return  a started invocation
     */
    public Invocation start(UUID id, int delayMillis) {
        return new Invocation(id, delayMillis);
    }

    @Override
    public void onMessageReceived(Message<String> message) {
        // HeartbeatSender doesn't listen for messages
    }

    @Override
    public boolean shouldHandleMessage(Message<?> m) {
        return false;
    }

    public class Invocation {
        private UUID id;
        private int delayMillis;
        private boolean isCancelled;

        private Thread thread;

        public Invocation(UUID id, int delayMillis) {
            this.id = id;
            this.delayMillis = delayMillis;
            this.thread = new Thread(this::run);

            this.thread.start();
        }

        public void stop() {
            this.isCancelled = false;
            this.thread.interrupt();
        }

        /**
         * Private function for running the heartbeat
         */
        private void run() {
            try {
                while (!isCancelled) {
                    send(new Message<String>(id, "", HeartbeatSender.this));
                    Thread.sleep(delayMillis);
                }
            } catch (InterruptedException ex) {}
        }
    }
}
