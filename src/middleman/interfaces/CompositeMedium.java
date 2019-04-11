package middleman.interfaces;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is the underlying structure of the CompositeMedium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class CompositeMedium extends Medium {
    private ArrayList<Medium> media = new ArrayList<>();
    private MessagePasser passer = new MessagePasser();

    public CompositeMedium(Medium ... media) {
        for (Medium m : media) {
            this.addMedium(m);
        }
    }

    public void addMedium(Medium medium) {
        media.add(medium);
        medium.onReceive(passer);
    }

    public ArrayList<Medium> getMedia() {
        return media;
    }

    @Override
    public void send(Message<?> message) {
        media.forEach(medium -> medium.send(message));
    }

    private void onMessageReceived(Message<?> message) {
        this.receive(message);
    }

    private class MessagePasser extends Component<Serializable> {

        @Override
        public void onMessageReceived(Message<Serializable> message) {
            CompositeMedium.this.onMessageReceived(message);
        }
    }
}