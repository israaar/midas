package middleman.tests;

import middleman.interfaces.*;
import middleman.implementations.MessageBroadcaster;


/**
 * This class represents a basic node structure to be used throughout the basic tests
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class Node implements MessageReceiveListener<String> {
    private static int nodeCounter = 0;

    public final int id = nodeCounter++;
    public Medium medium;
    public MessageBroadcaster comp;

    public Node(Medium medium,
        MessageBroadcaster comp) {
        this.medium = medium;
        this.comp = comp;

        comp.connectMedium(medium);
        comp.onReceive(this);
    }

    @Override
    public void onMessageReceived(Message<String> msg) {
        System.out.println(
            id + ": "
            + msg.id + ", "
            + msg.payload
        );
    }
}