/*
 * CommunicationMedium.java
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
 * This class is the underlying structure of the CommunicationMedium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.interfaces;

import java.util.ArrayList;

public abstract class CommunicationMedium {
    private ArrayList<MessageSendListener> sendListeners = new ArrayList<>();
    private ArrayList<MessageReceiveListener> receiveListeners = new ArrayList<>();

    public CommunicationMedium() {
        this.init();
    }

    /**
     * Initialize the medium
     */
    public abstract void init();

    /**
     * Sends the given message via the medium
     *
     * @param message The message to send
     */
    public void send(Message message) {
        sendListeners.forEach(listener -> listener.onMessageSent(message));
    }

    /**
     * Called internally whenever a message is received. Notifies all registered
     * MessageReceiveListeners.
     *
     * @param message The message that has been received
     */
    protected void receive(Message message) {
        receiveListeners.forEach(listener -> listener.onMessageReceived(message));
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

    /**
     * Registers the given MessageReceiveListener so that it will be notified whenever
     * a message is received
     *
     * @param MessageReceiveListener The MessageReceiveListener to notify
     */
    public void onReceive(MessageReceiveListener MessageReceiveListener) {
        receiveListeners.add(MessageReceiveListener);
    }
}
