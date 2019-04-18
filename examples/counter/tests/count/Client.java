package middleman.tests.count;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
        try (Socket socket = new Socket("0.0.0.0", port);
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
            break;
        } catch (InterruptedException ex) {
            // should not be interrupted
        }
    }

    @Override
    public synchronized void send(Message<?> message) {
        this.message = message;
        this.notifyAll();
    }

    @Override
    protected final void receive(Message<?> msg) {
        // NEVER receive data
    }
}