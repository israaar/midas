package examples.media.pipes;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import middleman.interfaces.Medium;
import middleman.interfaces.Message;

public class Writer extends Medium {
    private ObjectOutputStream out;

    public Writer(String filename) {
        try {
            Runtime.getRuntime().exec("mkfifo " + filename).waitFor();
            out = new ObjectOutputStream(new FileOutputStream(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Message<?> message) {
        try {
            out.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}