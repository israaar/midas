package examples.media.tcp;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import middleman.MiddleMan;
import middleman.interfaces.*;

public class Server {
    private Thread thread;
    private int port;
    private boolean cancel = false;
    private MiddleMan middleman;

    public Server(int port, MiddleMan middleman) {
        this.middleman = middleman;
        this.port = port;
        this.thread = new Thread(this::run);
        this.thread.start();
    }

    public void cancel() {
        this.cancel = true;
        this.thread.interrupt();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + port);

            while (!cancel) {
                Socket clientConnection = serverSocket.accept();
                ServerClient serverClient = new ServerClient(clientConnection);
                serverClient.start();
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
            this.thread.interrupt();
        }

        public void run() {
            try (
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                ){
                System.out.println("Client on port: " + port + " started");

                while (!cancel) {
                    try {
                        receive((Message<?>) objectInputStream.readObject());
                    } catch (Exception e) {
                        System.out.println("Client on port: " + port + " disconnected");
                        break;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void send(Message<?> message) {
            // Client doesn't send data
        }
    }
}

// package examples.media.tcp;

// import java.io.ObjectInputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.net.ServerSocket;
// import java.net.Socket;

// import middleman.MiddleMan;
// import middleman.interfaces.*;

// public class Server extends Medium {
//     private Thread thread;
//     private int port;
//     private boolean cancel = false;

//     public Server(int port) {
//         this.port = port;
//         this.thread = new Thread(this::run);
//         this.thread.start();
//     }

//     public void cancel() {
//         this.cancel = true;
//         this.thread.interrupt();
//     }

//     @Override
//     public void send(Message<?> message) {
//         // Severs don't send messages
//     }

//     private void run() {
//         try (ServerSocket serverSocket = new ServerSocket(port)) {
//             System.out.println("Server started on port: " + port);

//             while (!cancel) {
//                 try (
//                     Socket clientConnection = serverSocket.accept();
//                     InputStream inputStream = clientConnection.getInputStream();
//                     ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                     ) {
//                     Message<?> msg = (Message<?>) objectInputStream.readObject();
//                     System.out.println("Received message: " + msg);
//                     receive(msg);
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }