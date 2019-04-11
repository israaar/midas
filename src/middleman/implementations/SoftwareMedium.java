package middleman.implementations;

import java.util.ArrayList;

import middleman.interfaces.*;

/**
 * This class is the underlying structure of the SoftwareMedium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
public class SoftwareMedium extends Medium {
    private static ArrayList<SoftwareMedium> networkNodes = new ArrayList<>();

    public SoftwareMedium() {
        networkNodes.add(this);
    }

    public void send(Message<?> message) {
        networkNodes.forEach(node -> node.receive(message));
    }
}
