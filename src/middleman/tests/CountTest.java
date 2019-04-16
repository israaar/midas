package middleman.tests;

import java.util.ArrayList;

import middleman.MiddleMan;
import middleman.implementations.*;
import middleman.implementations.components.counter.NodeCounter;

/**
 * This class tests the NodeCounter component
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class CountTest {
    public static void main(String[] args) {
        ArrayList<MiddleMan> nodes = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            nodes.add(
                new MiddleMan("CountTest")
                    .addDispatcher(new NodeCounter.Dispatcher(2000, 1000))
                    .addMedium(new SoftwareMedium())
            );
        }

        nodes.get(0)
            .getDispatcher(NodeCounter.Dispatcher.class)
            .dispatch(System.out::println)
            .start();
    }
}