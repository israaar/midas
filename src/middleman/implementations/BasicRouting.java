/*
 * BasicRouting.java
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
 * This class imeplements a basic flood-fill routing algorithm
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.implementations;

import java.util.HashSet;
import java.util.UUID;

import middleman.interfaces.*;

public class BasicRouting extends RoutingAlgorithm {
    private HashSet<UUID> seenMessages = new HashSet<UUID>();

    public BasicRouting(CommunicationManager manager) {
        super(manager);
    }

    @Override
    public void startRouting() {

    }

    @Override
    public void onMessageReceived(Message message) {
        if (seenMessages.add(message.id)) {
            manager.getCommunicationMedia().forEach(
                medium -> medium.send(message)
            );
        }
    }

    @Override
    public void onMessageSent(Message message) {
        seenMessages.add(message.id);
    }
}
