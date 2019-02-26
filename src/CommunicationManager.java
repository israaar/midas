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

import java.util.ArrayList;

public final class CommunicationManager {
    private static ArrayList<CommunicationMedium> media;

    private CommunicationManager() {}

    public static void addCommunicationMedium(CommunicationMedium medium) {
        media.add(medium);
    }

    public static ArrayList<CommunicationMedium> getCommunicationMedia() {
        return media;
    }
}