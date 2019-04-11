package middleman.tests;

import java.util.HashSet;
import java.util.UUID;

import middleman.interfaces.*;

public class Node implements MessageReceiveListener {
    private static int nodeCounter = 0;

    private HashSet<UUID> seenMessages = new HashSet<UUID>();

    public final int id = nodeCounter++;
    public Medium medium;
    public Component comp;

    public Node(Medium medium,
        Component comp) {
        this.medium = medium;
        this.comp = comp;

        comp.connectMedium(medium);

        medium.onReceive(comp);
    }

    @Override
    public void onMessageReceived(Message msg) {
        if (seenMessages.add(msg.id)) {
            System.out.println(
                id + ": "
                + msg.id + ", "
                + new String(msg.payload)
            );
        }
    }
}