package middleman.interfaces;

import java.io.Serializable;

/**
 * This class is the underlying structure of the MessageReceiveListener
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public interface MessageReceiveListener<T extends Serializable> {
    void onMessageReceived(Message<T> message);
}
