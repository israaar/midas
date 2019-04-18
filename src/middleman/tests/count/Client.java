package middleman.tests.count;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    private Thread thread;
    private boolean cancel = false;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    public Client(Socket socket) {
        this.socket = socket;
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
        while (true) {
            try {
                this.inputStream = socket.getInputStream();
                this.outputStream = socket.getOutputStream();
                
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}