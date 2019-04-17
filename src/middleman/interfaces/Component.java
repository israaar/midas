package middleman.interfaces;

/**
 * This class is the underlying structure of the Medium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public abstract class Component {
    protected Dispatcher dispatcher;
    protected Thread thread;

    /**
     * This class should be extended by any components. Each implemented
     * component should have a single instance of a dispatcher. The dispatcher
     * is added as reference for use within the component.
     *
     * @param dispatcher dispatcher associated with this component
     */
    public Component(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.thread = new Thread(this::execute);
    }

    /**
     * Method to start running the Component
     */
    public final void start() {
        this.thread.start();
    }

    /**
     * Method for calling the run method and adding and removing the
     * current invocation
     */
    private final void execute() {
        try {
            this.dispatcher.addInvocation(this);

            this.run();
        } finally {
            cleanUp();
            this.dispatcher.removeInvocation(this);
        }
    }

    /**
     * Method that is called last when shutting down the component
     */
    protected void cleanUp() {

    }

    /**
     * Run method to implement in subclass that is called in a thread that is
     * ran by the Component
     */
    protected abstract void run();

    /**
     * Simplified method for sending a message
     *
     * @param message message to send
     */
    protected void send(Message<?> message) {
        dispatcher.getMiddleMan().send(message);
    }

    /**
     * Gaining access to the Dispatcher
     *
     * @return dispatcher instance
     */
    public final Dispatcher<?> getDispatcher() {
        return this.dispatcher;
    }

    /**
     * Gets the distpatcher based off the class of the dispatcher
     *
     * @param <T> type of the class
     * @param cls class name
     * @return the dispatcher instance associated with the class
     */
    public final <T extends Dispatcher<?>> T getDispatcher(Class<T> cls) {
        return this.dispatcher.getMiddleMan().getDispatcher(cls);
    }
}