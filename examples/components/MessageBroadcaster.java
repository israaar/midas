package examples.components;

import java.io.Serializable;
import java.util.HashSet;
import java.util.UUID;

import middleman.interfaces.*;

/**
 * This class imeplements a basic flood-fill routing algorithm
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class MessageBroadcaster extends Component {
    private HashSet<UUID> seenMessages = new HashSet<UUID>();
    private MessageReceiveListener<String> callback;

    private MessageBroadcaster(Dispatcher dispatcher, MessageReceiveListener<String> callback) {
        super(dispatcher);

        this.callback = callback;
    }

    private void onMessageReceived(Message<String> message) {
        if (seenMessages.add(message.id)) {
            callback.onMessageReceived(message);
            this.send(message);
        }
    }

    @Override
    public synchronized void run() {
        try {
            this.wait();
        } catch (InterruptedException e) {

        }
    }

    public synchronized void cancel() {
        this.notifyAll();
    }

    public void send(String message) {
        Message<String> msg = new Message<>(message, getDispatcher());

        seenMessages.add(msg.id);

        super.send(msg);
    }

    public interface MessageReceiveListener<T extends Serializable> {
        void onMessageReceived(Message<T> message);
    }

    public static class Dispatcher extends middleman.interfaces.Dispatcher<MessageBroadcaster> {
        public MessageBroadcaster dispatch(MessageReceiveListener<String> listener) {
            return new MessageBroadcaster(this, listener);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message<?> message) {
            for (MessageBroadcaster i : getInvocations()) {
                i.onMessageReceived((Message<String>) message);
            }
        }
    }
}
