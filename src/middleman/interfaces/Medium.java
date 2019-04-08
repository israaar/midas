/*
 * Medium.java
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
 * This class is the underlying structure of the Medium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.interfaces;

import java.util.ArrayList;

public abstract class Medium {
    private ArrayList<MessageSendListener> sendListeners = new ArrayList<>();
    private ArrayList<MessageReceiveListener> receiveListeners = new ArrayList<>();

    /**
     * Sends the given message via the medium. This method is used as a wrapper
     * to ensure that send listeners are called without needing a super() call.
     * The sendImpl() method should implement the actual sending of a message.
     *
     * @param message The message to send
     */
    public final void send(Message message) {
        sendListeners.forEach(listener -> listener.onMessageSent(message));
        sendImpl(message);
    }

    /**
     * Sends the given message (implementation specific).
     *
     * @param message The message to send
     */
    protected abstract void sendImpl(Message message);

    /**
     * Called internally whenever a message is received. Notifies all registered
     * MessageReceiveListeners.
     *
     * @param message The message that has been received
     */
    protected final void receive(Message message) {
        receiveListeners.forEach(listener -> listener.onMessageReceived(message));
    }

    /**
     * Registers the given MessageSendListener so that it will be notified whenever
     * a message is sent
     *
     * @param MessageSendListener The MessageSendListener to notify
     */
    public final void onSend(MessageSendListener MessageSendListener) {
        sendListeners.add(MessageSendListener);
    }

    /**
     * Registers the given MessageReceiveListener so that it will be notified whenever
     * a message is received
     *
     * @param MessageReceiveListener The MessageReceiveListener to notify
     */
    public final void onReceive(MessageReceiveListener MessageReceiveListener) {
        receiveListeners.add(MessageReceiveListener);
    }
}
