package middleman.interfaces;

import java.io.Serializable;
import java.util.UUID;

import middleman.MiddleMan;

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
    public final String compId;
    public final T payload;

    public Message(Component<?> component) {
        this(UUID.randomUUID(), component);
    }

    public Message(T payload, Component<?> component) {
        this(UUID.randomUUID(), payload, component);
    }

    public Message(UUID id, Component<?> component) {
        this(id, null, component);
    }

    public Message(UUID id, T payload, Component<?> component) {
        this.id = id;
        this.payload = payload;
        this.appId = component.getMiddleMan().getAppName();
        this.compId = component.getComponentId();
    }
}
