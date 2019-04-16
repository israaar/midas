package middleman.interfaces;

import java.util.concurrent.ConcurrentHashMap;

import middleman.MiddleMan;

/**
 * Handles messages that are relevant for the existing component
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public abstract class Dispatcher<T extends Component> {
    private MiddleMan middleman;
    private ConcurrentHashMap<T, Object> invocations = new ConcurrentHashMap<>();

    public final void attach(MiddleMan middleman) {
        if (this.middleman != null) {
            throw new IllegalStateException("Can only attach Dispatcher once." +
                " If added as component to a Dispatcher object, attach is already called.");
        }
        this.middleman = middleman;
    }

    public final MiddleMan getMiddleMan() {
        return middleman;
    }

    protected final void addInvocation(T comp) {
        invocations.put(comp, 0);
    }

    protected final void removeInvocation(T comp) {
        invocations.remove(comp);
    }

    protected final Iterable<T> getInvocations() {
        return invocations.keySet();
    }

    public abstract void handleMessage(Message<?> msg);

    public String getDispatcherId() {
        return this.getClass().getCanonicalName();
    }

    public boolean shouldHandleMessage(Message<?> m) {
        return this.getClass().getCanonicalName().equals(m.dispId);
    }
}