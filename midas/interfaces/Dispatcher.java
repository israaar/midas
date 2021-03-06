package midas.interfaces;

import java.util.concurrent.ConcurrentHashMap;

import midas.Midas;

/**
 * Handles messages that are relevant for the existing component.
 * The Dispatchers handles messages as well that are intended
 * for all invocations of components.
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public abstract class Dispatcher<T extends Component> {
    private Midas midas;
    private ConcurrentHashMap<T, Object> invocations = new ConcurrentHashMap<>();

    /**
     * To attach the dispatcher to the midas. Should only be called once
     * or it will through a runtime error.
     *
     * @param midas the midas instance
     */
    public void attach(Midas midas) {
        if (this.midas != null) {
            throw new IllegalStateException("Can only attach Dispatcher once." +
                " If added as component to a Dispatcher object, attach is already called.");
        }
        this.midas = midas;
    }

    /**
     * Returns the instance of Midas that this dispatcher is associated with
     */
    public final Midas getMidas() {
        return midas;
    }

    /**
     * Adds an Invocation of a Component
     *
     * @param T a component instance known as an Invocation
     */
    protected final void addInvocation(T comp) {
        invocations.put(comp, 0);
    }

    /**
     * Removes an component instance
     *
     * @param T removes the Invocation/instance of a component
     */
    protected final void removeInvocation(T comp) {
        invocations.remove(comp);
    }

    /**
     * Retuns a list of invocations
     *
     * @return iterable of the current invocations
     */
    protected final Iterable<T> getInvocations() {
        return invocations.keySet();
    }

    /**
     * To be implemented by an extended Dispatcher instance.
     * Will pass any messages recevied by the medium that is
     * associated with this dispatcher.
     *
     * @param msg the message that should be handled by the underlying dispatcher
     */
    public abstract void handleMessage(Message<?> msg);

    /**
     * Gets the dispatchers ID, which is its canonical name.
     *
     * @return dispatcher canonical name
     */
    public String getDispatcherId() {
        return this.getClass().getCanonicalName();
    }

    /**
     * Returns whether this message should be handled by this dispatcher.
     * Uses the canonical name for comparison against the messages dispatcher
     * id.
     *
     * @return true if it should be handled.
     */
    public boolean shouldHandleMessage(Message<?> m) {
        return getDispatcherId().equals(m.dispId);
    }
}