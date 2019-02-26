/*
 * Message.java
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
 * This class is the underlying structure of the Message
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.interfaces;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    public final UUID id;
    public final byte[] payload;

    private static final long serialVersionUID = 1;

    public Message() {
        this(UUID.randomUUID());
    }

    public Message(byte[] payload) {
        this(UUID.randomUUID(), payload);
    }

    public Message(UUID id) {
        this(id, new byte[]{});
    }

    public Message(UUID id, byte[] payload) {
        this.id = id;
        this.payload = payload;
    }
}
