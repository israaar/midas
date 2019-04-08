/*
 * Router.java
 *
 * Version:
 *     $Id$
 *
 *     0.1
 *
 * Revisions:
 *     $Log$
 *
 *     Implementation 0.1
 *
 */

/**
 * This class is the underlying structure of the Router
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.interfaces;

public abstract class Router extends Component implements MessageSendListener {
    @Override
    public void connectMedium(Medium medium) {
        super.connectMedium(medium);
        medium.onSend(this);
    }
}
