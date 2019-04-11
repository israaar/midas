package middleman.tests;

import java.util.ArrayList;

import middleman.implementations.*;
import middleman.interfaces.*;

/**
 * This class runs a basic floodfill algorithm
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class BasicTest {
    public static void main(String[] args) {
        Message.setStaticAppId("BasicTest");
        ArrayList<Node> nodes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            nodes.add(new Node(
                new CompositeMedium(new SoftwareMedium()),
                new MessageBroadcaster()
            ));
        }

        nodes.get(0).comp.send("Hello Middleman!");
    }
}