package middleman.implementations;

import java.util.ArrayList;
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
public class MessageBroadcaster extends Component<String>{
    private HashSet<UUID> seenMessages = new HashSet<UUID>();
    private ArrayList<MessageReceiveListener<String>> listeners = new ArrayList<>();

    @Override
    public void onMessageReceived(Message<String> message) {
        if (seenMessages.add(message.id)) {
            this.listeners.forEach(listener -> listener.onMessageReceived(message));
            this.send(message);
        }
    }

    public void send(String message) {
        Message<String> msg = new Message<>(message, this);

        seenMessages.add(msg.id);

        send(msg);
    }

    public void onReceive(MessageReceiveListener<String> listener) {
        this.listeners.add(listener);
    }
}
