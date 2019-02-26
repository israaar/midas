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

import java.io.Serializable;

public class Message implements Serializable {
    public final int id;
    public final byte[] payload;

    private static final long serialVersionUID = 1;

    public Message(int id) {
        this(id, new byte[]{});
    }

    public Message(int id, byte[] payload) {
        this.id = id;
        this.payload = payload;
    }
}
