package middleman.tests.count;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private Thread thread;
    private int port;
    private boolean cancel = false;

    public Server(int port) {
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
        System.out.println("Server attempting to start listening on port: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + port);
            while(!cancel) {
                Socket clientConnection = serverSocket.accept();
                
            } 

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static void main(String [] args) {
        String portStr = System.getenv("SERVER_PORT");
        if (args.length > 0) {
            portStr = args[0];
        }
        int port = -1;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        Server server = new Server(port);
        server.start();
    }
}