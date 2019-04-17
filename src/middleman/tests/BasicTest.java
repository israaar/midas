package middleman.tests;

import java.util.ArrayList;

import middleman.MiddleMan;
import middleman.implementations.*;

/**
 * This class runs a basic floodfill algorithm
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class BasicTest {
    public static void main(String[] args) {
        ArrayList<Node> nodes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            nodes.add(new Node(
                new MiddleMan("BasicTest")
                    .addDispatcher(new MessageBroadcaster.Dispatcher())
                    .addMedium(new SoftwareMedium(2, 0))
            ));
        }

        nodes.get(0).send("Hello Middleman!");
        nodes.get(0).bc.cancel();
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