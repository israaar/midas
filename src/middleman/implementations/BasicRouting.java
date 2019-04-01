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

public class BasicRouting extends Router {
    private HashSet<UUID> seenMessages = new HashSet<UUID>();

    @Override
    public void onMessageReceived(Message message) {
        if (seenMessages.add(message.id)) {
            this.send(message);
        }
    }

    @Override
    public void onMessageSent(Message message) {
        seenMessages.add(message.id);
    }
}
