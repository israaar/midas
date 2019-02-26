/*
 * CommunicationMedium.java
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
 * This class is the underlying structure of the CommunicationMedium
 *
 * @author Jesse Saran
 * @author Kyle Cutler
 * @author Allahsera Auguste Tapo
 */

public interface CommunicationMedium {
    void init();
    void send(Message message);
    void onReceive(MessageListener messageListener);
}
