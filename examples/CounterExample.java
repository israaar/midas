package examples;

import java.util.ArrayList;

import middleman.MiddleMan;

import examples.components.NodeCounter;
import examples.media.SoftwareMedium;

/**
 * This class tests the NodeCounter component
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class CounterExample {
    public static void main(String[] args) {
        ArrayList<MiddleMan> nodes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            nodes.add(
                new MiddleMan("CounterExample")
                    .addDispatcher(new NodeCounter.Dispatcher(1000, 400))
                    .addMedium(new SoftwareMedium(2, 0))
            );
        }

        for (int i = 0; i < 10; i++) {
            nodes.get(i)
                .getDispatcher(NodeCounter.Dispatcher.class)
                .dispatch(System.out::println)
                .start();
        }
    }
}