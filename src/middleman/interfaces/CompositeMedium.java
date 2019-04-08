/*
 * CompositeMedium.java
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
 * This class is the underlying structure of the CompositeMedium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.interfaces;

import java.util.ArrayList;

public class CompositeMedium extends Medium implements MessageReceiveListener {
    private ArrayList<Medium> media = new ArrayList<>();

    public CompositeMedium(Medium ... media) {
        for (Medium m : media) {
            this.addMedium(m);
        }
    }

    public void addMedium(Medium medium) {
        media.add(medium);
        medium.onReceive(this);
    }

    public ArrayList<Medium> getMedia() {
        return media;
    }

    @Override
    protected void sendImpl(Message message) {
        media.forEach(medium -> medium.send(message));
    }

    @Override
    public void onMessageReceived(Message message) {
        this.receive(message);
    }
}