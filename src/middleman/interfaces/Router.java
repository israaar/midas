/*
 * Router.java
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
 * This class is the underlying structure of the Router
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.interfaces;

public abstract class Router
    implements MessageReceiveListener, MessageSendListener {
    private Medium medium;

    public void connectMedium(Medium medium) {
        this.medium = medium;
        medium.onReceive(this);
    }

    protected void send(Message message) {
        this.medium.send(message);
    }
}
