package middleman.interfaces;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is the underlying structure of the Medium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public abstract class Medium {
    private ArrayList<Component<?>> components = new ArrayList<>();

    /**
     * Sends the given message (implementation specific).
     *
     * @param message The message to send
     */
    public abstract void send(Message<?> message);

    /**
     * Called internally whenever a message is received. Notifies all registered
     * Components.
     *
     * @param message The message that has been received
     */
    protected final void receive(Message<?> message) {
        components.forEach(comp -> sendMessageToComponent(comp, message));
    }

    /**
     * Registers the given Component so that it will be notified whenever
     * a message is received
     *
     * @param Component The Component to notify
     */
    public final void onReceive(Component<?> Component) {
        components.add(Component);
    }

    @SuppressWarnings("unchecked")
    private final <T extends Serializable> void sendMessageToComponent(Component<T> comp, Message<?> msg) {
        
        // TODO: figure out more specific exception type to use
        // Intended to catch errors in the case of a type mismatch
        try {
            if (comp.getClass().getCanonicalName().equals(msg.compId) && msg.appId == Message.staticAppId) {
                comp.onMessageReceived((Message<T>) msg);
            }
        } catch (Exception e) {
            // TODO: print error?
        }
    }
}
