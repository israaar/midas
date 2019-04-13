package middleman;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import middleman.interfaces.*;

/**
 * This class is used by components in order to interact with one another.
 * More or less this is a manager of components.
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class MiddleMan {

    // Java types won't let us associate the types of the key and value
    private HashMap<Class<?>, Component<?>> components = new HashMap<>();
    private ArrayList<Medium> media = new ArrayList<>();
    private String appName;

    public MiddleMan(String appName) {
        this(appName, null, null);
    }

    public MiddleMan(String appName, Component<?>[] components, Medium[] media) {
        if (appName == null) {
            throw new IllegalArgumentException("App Name must not be null");
        }

        this.appName = appName;

        if (components != null) {
            for (Component<?> c : components) {
                this.addComponent(c);
            }
        }
        if (media != null) {
            for (Medium m : media) {
                this.addMedium(m);
            }
        }
    }

    public MiddleMan addComponent(Component<?> comp) {
        if (comp == null) {
            throw new IllegalArgumentException("Component must not be null");
        }

        // Don't attach a component twice
        if (!components.containsKey(comp.getClass())) {
            comp.attach(this);
            components.put(comp.getClass(), comp);
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

    @SuppressWarnings("unchecked")
    public final void onMessageReceived(Message<?> msg) {
        for (Component comp : components.values()) {
            // TODO: figure out more specific exception type to use
            // Intended to catch errors in the case of a type mismatch
            try {
                if (msg.appId.equals(appName) && comp.shouldHandleMessage(msg)) {
                    comp.onMessageReceived(msg);
                } else {
                    // TODO: figure out a way to make this configurable
                    // System.err.println("Mismatch1 " + comp.shouldHandleMessage(msg));
                }
            } catch (Exception e) {
                // TODO: print error?
                // e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Component<? extends Serializable>> T getComponent(Class<T> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Class must not be null");
        }

        if (components.containsKey(cls)) {
            return (T) components.get(cls);
        }

        throw new RuntimeException("Class Component has not been added to this instance MiddleMan");
    }

    public String getAppName() {
        return this.appName;
    }
}
