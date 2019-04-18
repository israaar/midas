package middleman.tests.count;

import java.io.ObjectInputStream;
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
                ServerClient serverClient = new ServerClient(clientConnection);
                serverClient.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public class ServerClient extends Medium {
        private Thread thread;
        private boolean cancel = false;
        private Socket socket;

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
            try {
                InputStream inputStream = socket.getInputStream();
                //todo hookup to medium
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                while (true) {
                    super.receive((Message<?>) objectInputStream.readObject());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
        }

        @Override
        public void send(Message<?> message) {
            // NEVER sends data
        }
    }
}