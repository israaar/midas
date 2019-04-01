/*
 * CompositeMedium.java
 *
 * Version:
 *     $Id$
 *
 *     0.1
 *
 * Revisions:
 *     $Log$
 *
 *     Implementation 0.1
 *
 */

/**
 * This class is the underlying structure of the CompositeMedium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.interfaces;

import java.util.ArrayList;

public class CompositeMedium extends Medium implements MessageReceiveListener {
    private ArrayList<MessageSendListener> sendListeners = new ArrayList<>();
    private ArrayList<Medium> media = new ArrayList<>();

    public CompositeMedium(Medium ... media) {
        for (Medium m : media) {
            this.addMedium(m);
        }
    }

    public void addMedium(Medium medium) {
        media.add(medium);
        medium.onReceive(this);
    }

    public ArrayList<Medium> getMedia() {
        return media;
    }

    /**
     * Sends the given message via the medium
     *
     * @param message The message to send
     */
    @Override
    public void send(Message message) {
        sendListeners.forEach(listener -> listener.onMessageSent(message));
        media.forEach(medium -> medium.send(message));
    }

    /**
     * Registers the given MessageSendListener so that it will be notified whenever
     * a message is sent
     *
     * @param MessageSendListener The MessageSendListener to notify
     */
    public void onSend(MessageSendListener MessageSendListener) {
        sendListeners.add(MessageSendListener);
    }

    @Override
    public void onMessageReceived(Message message) {
        this.receive(message);
    }

    public void connectRouter(Router router) {
        this.onSend(router);
        this.onReceive(router);
    }
}