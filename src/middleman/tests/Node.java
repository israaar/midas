package middleman.tests;

import middleman.MiddleMan;
import middleman.implementations.MessageBroadcaster;

/**
 * This class represents a basic node structure to be used throughout the basic tests
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class Node {
    private static int nodeCounter = 0;
    public final int id = nodeCounter++;

    private MiddleMan middleman;

    public Node(MiddleMan middleman) {
        this.middleman = middleman;

        middleman.getComponent(MessageBroadcaster.class)
            .onReceive(msg -> System.out.println(
                id + ": "
                + msg.id + ", "
                + msg.payload
            ));
    }

    public void send(String msg) {
        middleman.getComponent(MessageBroadcaster.class).send(msg);
    }
}