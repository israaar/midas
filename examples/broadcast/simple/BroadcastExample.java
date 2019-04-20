package examples.broadcast.simple;

import java.util.ArrayList;

import middleman.MiddleMan;

import examples.shared.SoftwareMedium;

/**
 * This class runs a basic floodfill algorithm
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class BroadcastExample {
    public static void main(String[] args) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int [] nodes = new int [] {
            10,
            50,
            100,
            250,
            500,
            750,
            900
        };

        for (int i = 1; i < nodes.length; i++) {
            System.out.println("I: " + i);
            test(nodes[i]);
        }
    }

    private static void test(int nodeCount) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Starting: " + nodeCount);
        ArrayList<Node> nodes = new ArrayList<>();

        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node(new MiddleMan("BasicTest").addDispatcher(new MessageBroadcaster.Dispatcher())
                    .addMedium(new SoftwareMedium(2, 0))));
        }

        nodes.get(0).send("Hello Middleman!");
        nodes.get(0).bc.cancel();
        System.out.println("Ending: " + nodeCount);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (Node node : nodes) {
            node.bc.cancel();
        }
    }

    private static class Node {
        private static int nodeCounter = 0;
        public final int id = nodeCounter++;

        public MessageBroadcaster bc;

        public Node(MiddleMan middleman) {
            bc = middleman
                    .getDispatcher(MessageBroadcaster.Dispatcher.class)
                    .dispatch(msg -> {
                        System.out.println(
                            id + ": "
                            + msg.id + ", "
                            + msg.payload
                        );
                        bc.cancel();
                    });
            bc.start();
        }

        public void send(String msg) {
            bc.send(msg);
        }
    }
}