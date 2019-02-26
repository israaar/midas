package middleman.tests;

import java.util.HashSet;
import java.util.UUID;

import middleman.interfaces.*;

public class Node implements MessageReceiveListener {
    private static int nodeCounter = 0;

    private HashSet<UUID> seenMessages = new HashSet<UUID>();

    public final int id = nodeCounter++;
    public CommunicationMedium medium;
    public CommunicationManager manager;
    public RoutingAlgorithm router;

    public Node(CommunicationMedium medium,
                CommunicationManager manager,
                RoutingAlgorithm router) {
        this.medium = medium;
        this.manager = manager;
        this.router = router;

        medium.onReceive(router);
        medium.onSend(router);

        manager.addCommunicationMedium(medium);

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