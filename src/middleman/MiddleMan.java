package middleman;

import java.util.ArrayList;
import java.util.HashMap;

import middleman.interfaces.*;

/**
 * This class is used by dispatchers in order to interact with one another.
 * More or less this is a manager of dispatchers.
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class MiddleMan {

    // Java types won't let us associate the types of the key and value
    private HashMap<Class<?>, Dispatcher<?>> dispatchers = new HashMap<>();
    private ArrayList<Medium> media = new ArrayList<>();
    private String appName;

    public MiddleMan(String appName) {
        this(appName, null, null);
    }

    public MiddleMan(String appName, Dispatcher<?>[] dispatchers, Medium[] media) {
        if (appName == null) {
            throw new IllegalArgumentException("App Name must not be null");
        }

        this.appName = appName;

        if (dispatchers != null) {
            for (Dispatcher<?> c : dispatchers) {
                this.addDispatcher(c);
            }
        }
        if (media != null) {
            for (Medium m : media) {
                this.addMedium(m);
            }
        }
    }

    public MiddleMan addDispatcher(Dispatcher<?> disp) {
        if (disp == null) {
            throw new IllegalArgumentException("Dispatcher must not be null");
        }

        // Don't attach a dispatcher twice
        if (!dispatchers.containsKey(disp.getClass())) {
            disp.attach(this);
            dispatchers.put(disp.getClass(), disp);
        }

        return this;
    }

    public MiddleMan addMedium(Medium medium) {
        if (medium == null) {
            throw new IllegalArgumentException("Medium must not be null");
        }

        medium.attach(this);
        media.add(medium);

        return this;
    }

    public void send(Message<?> message) {
        for (Medium m : media) {
            m.send(message);
        }
    }

    public final void onMessageReceived(Message<?> msg) {
        for (Dispatcher<?> disp : dispatchers.values()) {
            // TODO: figure out more specific exception type to use
            // Intended to catch errors in the case of a type mismatch
            try {
                if (msg.appId.equals(appName) && disp.shouldHandleMessage(msg)) {
                    disp.handleMessage(msg);
                } else {
                    // TODO: figure out a way to make this configurable
                    // System.err.println("Mismatch1 " + disp.shouldHandleMessage(msg));
                }
            } catch (Exception e) {
                // TODO: print error?
                // e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Dispatcher<?>> T getDispatcher(Class<T> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Class must not be null");
        }

        if (dispatchers.containsKey(cls)) {
            return (T) dispatchers.get(cls);
        }

        throw new RuntimeException("Class Dispatcher has not been added to this instance MiddleMan");
    }

    public String getAppName() {
        return this.appName;
    }
}
