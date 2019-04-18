package examples.broadcast.sockets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import middleman.interfaces.Medium;
import middleman.interfaces.Message;

public class Client extends Medium {

    private Thread thread;
    private boolean cancel = false;
    private Message<?> message;
    private final int port;

    public Client(int port) {
        this.port = port;
        this.thread = new Thread(this::run);
    }

    public void start() {
        this.thread.start();
    }

    public void cancel() {
        this.cancel = true;
        this.thread.interrupt();
    }

    public void run() {
        try (Socket socket = new Socket("localhost", port);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            ) {
            while (true) {
                synchronized (this) {
                    this.wait();
                    objectOutputStream.writeObject(message);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            // should not be interrupted
        }
    }

    @Override
    public synchronized void send(Message<?> message) {
        this.message = message;
        this.notifyAll();
    }
}