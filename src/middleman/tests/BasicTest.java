package middleman.tests;

import java.util.ArrayList;

import middleman.MiddleMan;
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
        ArrayList<Node> nodes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            nodes.add(new Node(
                new MiddleMan("BasicTest")
                    .addComponent(new MessageBroadcaster())
                    .addMedium(new SoftwareMedium())
            ));
        }

        nodes.get(0).send("Hello Middleman!");
    }
}