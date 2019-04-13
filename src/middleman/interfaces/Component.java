package middleman.interfaces;

import middleman.MiddleMan;
import java.io.Serializable;

/**
 * This class is the underlying structure of the Medium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public abstract class Component<T extends Serializable> implements MessageReceiveListener<T> {
    protected MiddleMan middleman;

    public void attach(MiddleMan middleman) {
        if (this.middleman != null) {
            throw new IllegalStateException("Can only attach MiddleMan once." +
                " If addeded as component to a Middleman object, attach is already called.");
        }
        this.middleman = middleman;
    }

    protected void send(Message<T> message) {
        middleman.send(message);
    }

    public final MiddleMan getMiddleMan() {
        return this.middleman;
    }

    public String getComponentId() {
        return this.getClass().getCanonicalName();
    }

    public boolean shouldHandleMessage(Message<?> m) {
        return this.getClass().getCanonicalName().equals(m.compId);
    }
}