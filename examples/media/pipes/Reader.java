package examples.media.pipes;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import midas.interfaces.Medium;
import midas.interfaces.Message;

public class Reader extends Medium {
    public Reader(String filename) {
        new Thread(() -> {
            try {
                Runtime.getRuntime().exec("mkfifo " + filename).waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (true) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
                    receive((Message<?>) in.readObject());
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    @Override
    public void send(Message<?> message) {
        // Writers send, Readers don't
    }
}