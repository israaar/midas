package midas.interfaces;

import midas.Midas;

/**
 * This class is the underlying structure of the Medium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public abstract class Medium {
    protected Midas midas;

    /**
     * Sends the given message (implementation specific).
     *
     * @param message The message to send
     */
    public abstract void send(Message<?> message);

    /**
     * Called internally whenever a message is received. Notifies all registered
     * Components.
     *
     * @param message The message that has been received
     */
    protected final void receive(Message<?> msg) {
        if (this.midas != null) {
            this.midas.onMessageReceived(msg);
        }
    }

    /**
     * Attaches this Medium to the given Midas
     *
     * @param Component The Component to notify
     */
    public final void attach(Midas midas) {
        this.midas = midas;
    }
}
