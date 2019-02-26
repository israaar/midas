/*
 * CommunicationManager.java
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
 * This class is the underlying structure of the CommunicationManager
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */
package middleman.interfaces;

import java.util.ArrayList;

public class CommunicationManager {
    private ArrayList<CommunicationMedium> media = new ArrayList<>();

    public CommunicationManager() {}

    public void addCommunicationMedium(CommunicationMedium medium) {
        media.add(medium);
    }

    public ArrayList<CommunicationMedium> getCommunicationMedia() {
        return media;
    }
}