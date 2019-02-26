/*
 * RoutingAlgorithm.java
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
 * This class is the underlying structure of the RoutingAlgorithm
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.interfaces;


public abstract class RoutingAlgorithm
    implements MessageReceiveListener, MessageSendListener {

    protected CommunicationManager manager;

    public RoutingAlgorithm(CommunicationManager manager) {
        this.manager = manager;
    }

    public abstract void startRouting();
}
