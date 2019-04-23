package examples.media.tcp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import middleman.interfaces.Medium;
import middleman.interfaces.Message;

public class Client extends Medium {
    private Thread thread;
    private boolean cancel = false;
    private LinkedBlockingQueue<Message<?>> messages;
    private final int port;

    public Client(int port) {
        this.port = port;
        this.messages = new LinkedBlockingQueue<>();
        this.thread = new Thread(this::run);
        this.thread.start();
    }

    public void cancel() {
        this.cancel = true;
        this.thread.interrupt();
    }

    public void run() {
        try {
            synchronized(this) {
                this.wait();
            }
            try (Socket socket = new Socket("localhost", port);
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                ) {
                while (!cancel) {
                    objectOutputStream.writeObject(messages.take());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (InterruptedException ex) {
            // cancelled
        }
    }

    @Override
    public synchronized void send(Message<?> message) {
        this.messages.add(message);
        this.notifyAll();
    }
}
