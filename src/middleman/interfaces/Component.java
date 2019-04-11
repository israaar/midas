package middleman.interfaces;

import java.io.Serializable;

/**
 * This class is the underlying structure of the Medium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public abstract class Component<T extends Serializable> implements MessageReceiveListener<T> {
    private Medium medium;

    public void connectMedium(Medium medium) {
        this.medium = medium;
        medium.onReceive(this);
    }

    protected void send(Message<T> message) {
        this.medium.send(message);
    }

    public String getComponentId() {
        return this.getClass().getCanonicalName();
    }

    public boolean shouldHandleMessage(Message<?> m) {
        return this.getClass().getCanonicalName().equals(m.compId);
    }
}