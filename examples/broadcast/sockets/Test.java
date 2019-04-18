package examples.broadcast.sockets;

import java.util.ArrayList;
import java.util.Scanner;

import examples.broadcast.simple.MessageBroadcaster;
import middleman.MiddleMan;
import middleman.interfaces.*;

public class Test {

    public static void main(String [] args) {
        String portStr = "";
        int serverPort = -1;
        if (args.length > 0) {
            portStr = args[0];
        }
        try {
            serverPort = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        ArrayList<Integer> clientPorts = new ArrayList<Integer>();
        for (int i = 1; i < args.length; i++) {
            try {
                Integer clientPort = Integer.parseInt(args[i]);
                clientPorts.add(clientPort);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number arg for client port number.");
                e.printStackTrace();
                return;
            }
        }

        Client[] clients = new Client[args.length-1];
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new Client(clientPorts.get(i));
        }

        Node node = new Node(
            new MiddleMan("Message Broadcast: Different Medium", null, clients)
                .addDispatcher(new MessageBroadcaster.Dispatcher()),
            serverPort
        );

        System.out.println("Commands:");
        System.out.println("    startclients - starts all client connections (call once all other servers are set)");
        System.out.println("    stop - stops current server");
        System.out.println("    all other commands are sent as text to all other clients");
        boolean clientsStarted = false;
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("stop")) {
                break;
            } else if (line.equals("startclients")) {
                if (!clientsStarted) {
                    for (Client client : clients) {
                        client.start();
                    }
                } else {
                    System.out.println("clients can only be started once");
                }
            }
            node.send(scanner.nextLine());
        }
        scanner.close();
    }

    private static class Node {
        private static int nodeCounter = 0;
        public final int id = nodeCounter++;

        public MessageBroadcaster bc;

        public Node(MiddleMan middleman, int serverPort) {
            bc = middleman
                    .getDispatcher(MessageBroadcaster.Dispatcher.class)
                    .dispatch(msg -> {
                        System.out.println(
                            id + ": "
                            + msg.id + ", "
                            + msg.payload
                        );
                        // bc.cancel();
                    });
            Server server = new Server(serverPort, middleman);
            bc.start();
        }

        public void send(String msg) {
            bc.send(msg);
        }
    }

}