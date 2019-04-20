package examples.broadcast.sockets;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import middleman.MiddleMan;
import middleman.interfaces.*;

public class Server {

    private Thread thread;
    private int port;
    private boolean cancel = false;
    private MiddleMan middleman;
    private ArrayList<ServerClient> clients = new ArrayList<ServerClient>();

    public Server(int port, MiddleMan middleman) {
        this.middleman = middleman;
        this.port = port;
        this.thread = new Thread(this::run);
        this.thread.start();
    }

    public void cancel() {
        this.cancel = true;
        this.thread.interrupt();
        for (ServerClient client : clients) {
            client.cancel();
        }
    }

    public void run() {
        System.out.println("Server attempting to start listening on port: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + port);
            while (!cancel) {
                Socket clientConnection = serverSocket.accept();
                ServerClient serverClient = new ServerClient(clientConnection);
                serverClient.start();
                this.clients.add(serverClient);
                middleman.addMedium(serverClient);
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

        public ServerClient(Socket socket) {
            this.socket = socket;
            this.thread = new Thread(this::run);
        }

        public void start() {
            this.thread.start();
        }

        public void cancel() {
            this.cancel = true;
            if (!this.thread.isAlive()) {
                this.thread.interrupt();
            }
        }

        public void run() {
            try (
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                ){
                System.out.println("Client on port: " + port + " started");
                while (!cancel) {
                    try {
                        super.receive((Message<?>) objectInputStream.readObject());
                    } catch (Exception e) {
                        System.out.println("Client on port: " + port + " disconnected");
                        break;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("SERVER CLOSED");
        }

        @Override
        public void send(Message<?> message) {
            // NEVER sends data
        }
    }
}