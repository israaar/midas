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

    public Component(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.thread = new Thread(this::execute);
    }

    public final void start() {
        this.thread.start();
    }

    private final void execute() {
        try {
            this.dispatcher.addInvocation(this);

            this.run();
        } finally {
            cleanUp();
            this.dispatcher.removeInvocation(this);
        }
    }

    protected void cleanUp() {

    }

    protected abstract void run();

    protected void send(Message<?> message) {
        dispatcher.getMiddleMan().send(message);
    }

    public final Dispatcher<?> getDispatcher() {
        return this.dispatcher;
    }

    public final <T extends Dispatcher<?>> T getDispatcher(Class<T> cls) {
        return this.dispatcher.getMiddleMan().getDispatcher(cls);
    }
}