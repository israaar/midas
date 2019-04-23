package examples.media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import midas.interfaces.*;

/**
 * This class is the underlying structure of the SoftwareMedium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class SoftwareMedium extends Medium {
    private static ArrayList<SoftwareMedium> networkNodes = new ArrayList<>();
    private static HashMap<Integer, HashSet<Integer>> connections = new HashMap<>();

    private final int index;
    private final double dropRate;

    public SoftwareMedium(int maxDegree, double dropRate) {
        networkNodes.add(this);
        index = networkNodes.size() - 1;
        connections.put(index, new HashSet<>());

        this.dropRate = dropRate;

        int remaining = Math.min(
            (int)(Math.random() * maxDegree) + 1,
            networkNodes.size() - 1
        );
        while (remaining > 0) {
            int guess = (int)(Math.random() * networkNodes.size());
            if (guess != index && !connections.get(index).contains(guess)) {
                connections.get(index).add(guess);
                connections.get(guess).add(index);
                remaining--;
            }
        }
    }

    public void send(Message<?> message) {
        for (int connected : connections.get(index)) {
            if (Math.random() > dropRate) {
                // System.out.println("Message\n" + message +
                // "\nfrom " + index +
                // " to " + connected + " SENT");
                networkNodes.get(connected).receive(message);
            } else {
                // System.out.println("Message\n" + message +
                // "\nfrom " + index +
                // " to " + connected + " DROPPED");
            }
        }
    }
}
