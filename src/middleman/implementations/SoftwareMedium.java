/*
 * SoftwareMedium.java
 *
 * Version:
 *     $Id$
 *
 *     0.1
 *
 * Revisions:
 *     $Log$
 *
 *     Implementation 0.1
 *
 */

/**
 * This class is the underlying structure of the SoftwareMedium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.implementations;

import java.util.ArrayList;

import middleman.interfaces.*;

public class SoftwareMedium extends Medium {
    private static ArrayList<SoftwareMedium> networkNodes = new ArrayList<>();

    public SoftwareMedium() {
        networkNodes.add(this);
    }

    @Override
    public void send(Message message) {
        networkNodes.forEach(node -> node.receive(message));
    }
}