package middleman.implementations;

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

    @Override
    public void onMessageReceived(Message<String> message) {
        if (seenMessages.add(message.id)) {
            this.send(message);
        }
    }

    @Override
    public void send(Message<String> message) {
        seenMessages.add(message.id);
        super.send(message);
    }

    @Override
    public void onReceive() {

    }
}
