package middleman.implementations.components.heartbeat;

import middleman.interfaces.*;

/**
 * Sends out heartbeat messages
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class HeartbeatSender extends Component<String> {
    private final int delayMillis;
    private final String id;

    private final Thread thread;

    /**
     *
     * @param id  An id that HeartbeatListeners can listen for in order to
     *            communicate specifically with this node
     * @param delayMillis  The time to pause between sending heartbeat messages
     */
    public HeartbeatSender(String id, int delayMillis) {
        this.id = id;
        this.delayMillis = delayMillis;

        this.thread = new Thread(this::run);
    }

    /**
     * Starts the heartbeat
     */
    public void start() {
        this.thread.start();
    }

    /**
     * Stops the heartbeat
     */
    public void stop() {
        this.thread.interrupt();
    }

    /**
     * Private function for running the heartbeat
     */
    private void run() {
        try {
            while (true) {
                this.send(new Message<String>(id, this));
                Thread.sleep(delayMillis);
            }
        } catch (InterruptedException ex) {}
    }

    @Override
    public void onMessageReceived(Message<String> message) {
        // HeartbeatSender doesn't listen for messages
    }

    @Override
    public boolean shouldHandleMessage(Message<?> m) {
        return false;
    }
}
