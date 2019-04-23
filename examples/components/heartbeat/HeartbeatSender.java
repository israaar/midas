package examples.components.heartbeat;

import java.util.UUID;

import middleman.interfaces.*;

/**
 * Sends out heartbeat messages
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class HeartbeatSender extends Component {
    private UUID id;
    private int delayMillis;
    private boolean isCancelled = false;

    private HeartbeatSender(Dispatcher dispatcher, UUID id, int delayMillis) {
        super(dispatcher);

        this.id = id;
        this.delayMillis = delayMillis;
    }

    public void stop() {
        this.isCancelled = true;
        super.thread.interrupt();
    }

    @Override
    protected void run() {
        while (!isCancelled) {
            try {
                send(new Message<String>(id, "", getDispatcher()));
                Thread.sleep(delayMillis);
            } catch (InterruptedException ex) {}
        }
    }

    public static class Dispatcher extends middleman.interfaces.Dispatcher<HeartbeatSender> {
        /**
         * Starts a heartbeat
         *
         * @param id  An id that HeartbeatListeners can listen for in order to
         *            communicate specifically with this node
         * @param delayMillis  The time to pause between sending heartbeat messages
         * @return  a started invocation
         */
        public HeartbeatSender dispatch(UUID id, int delayMillis) {
            return new HeartbeatSender(this, id, delayMillis);
        }

        @Override
        public void handleMessage(Message<?> message) {
            // HeartbeatSender doesn't listen for messages
        }

        @Override
        public boolean shouldHandleMessage(Message<?> m) {
            return false;
        }
    }
}
