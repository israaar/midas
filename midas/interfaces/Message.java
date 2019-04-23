package midas.interfaces;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class is the underlying structure of the Message.
 * All messages will be created by extending this base type of message.
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class Message<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1;

    public final UUID id;
    public final String appId;
    public final String dispId;
    public final T payload;

    /**
     * Message constructor for creating a message.
     *
     * @param dispatcher dispatcher instance
     */
    public Message(Dispatcher<?> dispatcher) {
        this(UUID.randomUUID(), dispatcher);
    }

    /**
     * Message constructor for creating a message.
     *
     * @param T payload type
     * @param payload payload included in message
     * @param dispatcher dispatcher instance
     */
    public Message(T payload, Dispatcher<?> dispatcher) {
        this(UUID.randomUUID(), payload, dispatcher);
    }

    /**
     * Message constructor for creating a message.
     *
     * @param id the unique message indentifier for this message
     * @param dispatcher dispatcher instance
     */
    public Message(UUID id, Dispatcher<?> dispatcher) {
        this(id, null, dispatcher);
    }

    /**
     * Message constructor for creating a message.
     *
     * @param id the unique message indentifier for this message
     * @param T payload type
     * @param payload payload included in message
     * @param dispatcher dispatcher instance
     */
    public Message(UUID id, T payload, Dispatcher<?> dispatcher) {
        this.id = id;
        this.payload = payload;
        this.appId = dispatcher.getMidas().getAppName();
        this.dispId = dispatcher.getDispatcherId();
    }

    /**
     * Overrides the ToString to provide information about this
     * message.
     *
     * @return the string representation of this message.
     */
    @Override
    public String toString() {
        return "Msg: " + id
            + "\n  App ID: " + appId
            + "\n  Dispatcher ID: " + dispId
            + "\n  Payload:" + ("\n" + payload).replace("\n", "\n    ");
    }
}
