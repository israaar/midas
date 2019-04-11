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

    public static String staticAppId;

    public final UUID id;
    public final String appId;
    public final String compId;
    public final T payload;

    public Message(String compId) {
        this(UUID.randomUUID(), compId);
    }

    public Message(T payload, String compId) {
        this(UUID.randomUUID(), payload, compId);
    }

    public Message(UUID id, String compId) {
        this(id, null, compId);
    }

    public Message(UUID id, T payload, String compId) {
        if (staticAppId == null) {
            throw new RuntimeException("Message.staticAppId must be set!");
        }

        this.id = id;
        this.payload = payload;
        this.appId = staticAppId;
        this.compId = compId;
    }

    public static void setStaticAppId(String appId) {
        if (appId == null) {
            throw new IllegalArgumentException("AppId must not be null");
        }

        staticAppId = appId;
    }
}
