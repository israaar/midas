/*
 * MessageSendListener.java
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
 * This class is the underlying structure of the MessageSendListener
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.interfaces;


public interface MessageSendListener {
    void onMessageSent(Message message);
}
