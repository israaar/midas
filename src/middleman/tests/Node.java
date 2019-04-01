package middleman.tests;

import java.util.HashSet;
import java.util.UUID;

import middleman.interfaces.*;

public class Node implements MessageReceiveListener {
    private static int nodeCounter = 0;

    private HashSet<UUID> seenMessages = new HashSet<UUID>();

    public final int id = nodeCounter++;
    public Medium medium;
    public Router router;

    public Node(Medium medium,
                Router router) {
        this.medium = medium;
        this.router = router;

        router.connectMedium(medium);

        medium.onReceive(this);
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