package middleman.implementations.components.counter;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import middleman.interfaces.Component;
import middleman.interfaces.Message;

public class NodeCounter extends Component<NodeCounter.NodeCounterMessage> {
    private ConcurrentHashMap<UUID, Object> seenIds = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Invocation, Object> activeInvocations = new ConcurrentHashMap<>();

    public Invocation start(Consumer<Integer> callback) {
        return new Invocation(callback, UUID.randomUUID());
    }

    @Override
    public void onMessageReceived(Message<NodeCounterMessage> message) {
        // Message can be:
        //   - An invocation of the algorithm
        //   - An acknowledgement
        //      - This is the same as an invocation, but uses a duplicate
        //        invocation id and also contains an id for heartbeat messages.
        //   - A result

        switch (message.payload.type) {
            case INVOKE:

                // ignore cycles
                if (!seenIds.contains(message.id)) {
                    seenIds.put(message.id, null);
                    // TODO
                    // new Invocation(callback, UUID.randomUUID());
                }
                break;
            case ACK:
                for (Invocation i : activeInvocations.keySet()) {
                    i.onReceiveAcknowledgement(message);
                }
                break;
            case RESULT:
                for (Invocation i : activeInvocations.keySet()) {
                    i.onReceiveResult(message);
                }
                break;
        }
    }

    public static class NodeCounterMessage implements Serializable {

        public static final long serialVersionUID = 1;

        public static enum Type {
            INVOKE, ACK, RESULT
        }

        public final Type type;
        public final Integer totalCount;

        public NodeCounterMessage(Type type, Integer totalCount) {
            this.type = type;
            this.totalCount = totalCount;
        }
    }

    public class Invocation {
        private boolean isRoot;

        private final Thread thread;
        private boolean isCancelled = false;
        private Consumer<Integer> callback;
        private UUID id;
        private int timeoutMillis;

        public Invocation(Consumer<Integer> callback, UUID id, int timeoutMillis) {
            this.callback = callback;
            this.thread = new Thread(this::run);
            this.id = id;
            this.timeoutMillis = timeoutMillis;

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

            runProtocol();

            activeInvocations.remove(this);
        }

        private void runProtocol() {
            send(new Message<>(
                id,
                new NodeCounterMessage(NodeCounterMessage.Type.INVOKE, -1),
                NodeCounter.this
            ));

            try {
                Thread.sleep(timeoutMillis);

                // no friends
                send(new Message<>(
                    id,
                    new NodeCounterMessage(NodeCounterMessage.Type.RESULT, 1),
                    NodeCounter.this
                ));

                return;
            } catch (InterruptedException e) {

            }

            while (!this.isCancelled) {

            }
        }

        public void onReceiveAcknowledgement(Message<NodeCounterMessage> message) {
            if (message.id.equals(this.id)) {

            }
        }

        public void onReceiveResult(Message<NodeCounterMessage> message) {
            if (message.id.equals(this.id)) {

            }
        }
    }
}