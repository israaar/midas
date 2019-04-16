package middleman.interfaces;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class is the underlying structure of the Message
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

    public Message(Dispatcher<?> dispatcher) {
        this(UUID.randomUUID(), dispatcher);
    }

    public Message(T payload, Dispatcher<?> dispatcher) {
        this(UUID.randomUUID(), payload, dispatcher);
    }

    public Message(UUID id, Dispatcher<?> dispatcher) {
        this(id, null, dispatcher);
    }

    public Message(UUID id, T payload, Dispatcher<?> dispatcher) {
        this.id = id;
        this.payload = payload;
        this.appId = dispatcher.getMiddleMan().getAppName();
        this.dispId = dispatcher.getDispatcherId();
    }
}
