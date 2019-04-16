package middleman.implementations.components.counter;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import middleman.implementations.components.heartbeat.HeartbeatReceiver;
import middleman.implementations.components.heartbeat.HeartbeatSender;
import middleman.interfaces.Component;
import middleman.interfaces.Message;

/**
 * This class counts the number of nodes
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */

public class NodeCounter extends Component {
    private boolean isCancelled = false;
    private Consumer<Integer> callback;
    private UUID id;
    private UUID senderID;
    private int timeoutMillis;
    private int heartbeatMillis;

    private int totalCount = 1;

    private static enum State {
        STARTING, DISCOVERY, RUNNING, COMPLETE
    }
    private State state;

    private ConcurrentLinkedQueue<HeartbeatReceiver> neighbors = new ConcurrentLinkedQueue<>();

    private NodeCounter(Dispatcher dispatcher, Consumer<Integer> callback, UUID id, UUID senderID, int timeoutMillis, int heartbeatMillis) {
        super(dispatcher);

        this.callback = callback;
        this.id = id;
        this.timeoutMillis = timeoutMillis;
        this.heartbeatMillis = heartbeatMillis;
        this.senderID = senderID;
    }

    /**
     * Cancel the thread. Will stop listening for heartbeats.
     */
    public void cancel() {
        this.isCancelled = true;
        super.thread.interrupt();
    }

    /**
     * Private function for listening for heartbeat messages
     */
    @Override
    protected void run() {
        this.state = State.STARTING;

        send(new Message<>(
            id,
            NodeCounterMessage.makeInvoke(senderID),
            getDispatcher()
        ));

        this.state = State.DISCOVERY;

        try {
            Thread.sleep(timeoutMillis);
        } catch (InterruptedException e) {
            // SHOULD not be interrupted here, but just in case :)
            return;
        }

        this.state = State.RUNNING;

        while (!this.isCancelled && !this.neighbors.isEmpty()) {
            try {
                synchronized(this) {
                    this.wait();
                }
            } catch (InterruptedException ex) {
                // again, shouldn't happen unless we cancelled.
            }
        }

        this.state = State.COMPLETE;

        if (!this.isCancelled) {
            callback.accept(totalCount);
        }
    }

    @Override
    public void cleanUp() {
        for (HeartbeatReceiver r : this.neighbors) {
            r.cancel();
        }
    }

    public void onReceiveAcknowledgement(Message<NodeCounterMessage> message) {
        if (this.state != State.COMPLETE
            && message.id.equals(this.id)
            && message.payload.senderID.equals(this.senderID)) {

            HeartbeatReceiver heartbeat = this
                .getDispatcher(HeartbeatReceiver.Dispatcher.class)
                .dispatch(
                    message.payload.recipientID,
                    heartbeatMillis,
                    () -> this.removeHeartbeat(message.payload.recipientID)
                );

            heartbeat.start();

            this.neighbors.add(heartbeat);
        }
    }

    public synchronized void removeHeartbeat(UUID recipientID) {
        this.neighbors.removeIf(hb -> hb.id.equals(recipientID));

        if (this.neighbors.isEmpty()) {
            this.notifyAll();
        }
    }

    public void onReceiveResult(Message<NodeCounterMessage> message) {
        if (message.id.equals(this.id)
            && message.payload.senderID.equals(senderID)) {

            this.totalCount += message.payload.result;
            this.removeHeartbeat(message.payload.recipientID);
        }
    }

    public static class Dispatcher extends middleman.interfaces.Dispatcher<NodeCounter> {
        private ConcurrentHashMap<UUID, Object> seenIds = new ConcurrentHashMap<>();
        private final int timeoutMillis;
        private final int heartbeatMillis;

        public Dispatcher(int timeoutMillis, int heartbeatMillis) {
            this.timeoutMillis = timeoutMillis;
            this.heartbeatMillis = heartbeatMillis;
        }

        public NodeCounter dispatch(Consumer<Integer> callback) {
            return new NodeCounter(
                this, callback,
                UUID.randomUUID(), UUID.randomUUID(),
                timeoutMillis, heartbeatMillis
            );
        }

        @Override
        public void handleMessage(Message<?> message) {
            // Message can be:
            //   - An invocation of the algorithm
            //   - An acknowledgement
            //      - This is the same as an invocation, but uses a duplicate
            //        invocation id and also contains an id for heartbeat messages.
            //   - A result
            NodeCounterMessage payload = (NodeCounterMessage) message.payload;

            switch (payload.type) {
                case INVOKE:

                    // ignore cycles
                    if (!seenIds.contains(message.id)) {
                        seenIds.put(message.id, null);

                        UUID senderID = UUID.randomUUID();

                        // respond with acknowledgment
                        this.getMiddleMan().send(
                            new Message<NodeCounterMessage>(
                                message.id,
                                NodeCounterMessage.makeAck(
                                    payload.senderID,
                                    senderID
                                ),
                                this
                            )
                        );

                        // start up heartbeat to received senderID
                        HeartbeatSender heartbeat = this
                            .getMiddleMan()
                            .getDispatcher(HeartbeatSender.Dispatcher.class)
                            .dispatch(senderID, heartbeatMillis);

                        heartbeat.start();

                        // start up our own nodecounter
                        new NodeCounter(
                            this,
                            count -> {
                                heartbeat.stop();

                                this.getMiddleMan().send(
                                    new Message<NodeCounterMessage>(
                                        message.id,
                                        NodeCounterMessage.makeResult(
                                            payload.senderID,
                                            senderID,
                                            count
                                        ),
                                        this
                                    )
                                );
                            },
                            message.id,
                            senderID,
                            timeoutMillis,
                            heartbeatMillis
                        );
                    }
                    break;
                case ACK:
                    for (NodeCounter i : getInvocations()) {
                        i.onReceiveAcknowledgement((Message<NodeCounterMessage>) message);
                    }
                    break;
                case RESULT:
                    for (NodeCounter i : getInvocations()) {
                        i.onReceiveResult((Message<NodeCounterMessage>) message);
                    }
                    break;
            }
        }
    }

    public static class NodeCounterMessage implements Serializable {

        public static final long serialVersionUID = 1;

        public static enum Type {
            INVOKE, ACK, RESULT
        }

        public final Type type;

        public final UUID senderID;
        public final UUID recipientID;
        public final int result;

        public NodeCounterMessage(Type type, UUID senderID, UUID recipientID, int result) {
            this.type = type;
            this.senderID = senderID;
            this.recipientID = recipientID;
            this.result = result;
        }

        public static NodeCounterMessage makeInvoke(UUID senderID) {
            return new NodeCounterMessage(Type.INVOKE, senderID, null, 0);
        }

        public static NodeCounterMessage makeAck(UUID senderID, UUID recipientID) {
            return new NodeCounterMessage(Type.ACK, senderID, recipientID, 0);
        }

        public static NodeCounterMessage makeResult(UUID senderID, UUID recipientID, int result) {
            return new NodeCounterMessage(Type.RESULT, senderID, recipientID, result);
        }
    }
}