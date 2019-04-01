package middleman.tests;

import java.util.ArrayList;

import middleman.implementations.*;
import middleman.interfaces.*;

public class BasicTest {
    public static void main(String[] args) {
        ArrayList<Node> nodes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            nodes.add(new Node(
                new CompositeMedium(new SoftwareMedium()),
                new BasicRouting()
            ));
        }

        nodes.get(0).medium.send(new Message(
            "Hello MiddleMan!".getBytes()
        ));
    }
}