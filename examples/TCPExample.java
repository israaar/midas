package examples;

import java.util.ArrayList;
import java.util.Scanner;

import examples.components.NodeCounter;
import examples.components.RoutedMessenger;
import examples.components.MessageBroadcaster;
import examples.media.tcp.*;
import midas.Midas;

public class TCPExample {
    public static void main(String [] args) {
        if (args.length < 1) {
            System.out.println("Usage: Test <serverPort> [<clientPort> ...]");
            return;
        }

        int serverPort = -1;
        try {
            serverPort = Integer.parseInt(args[0]);
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

        Midas midas = new Midas("TCPExample");

        Client[] clients = new Client[args.length-1];
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new Client(clientPorts.get(i));
            midas.addMedium(clients[i]);
        }

        Server server = new Server(serverPort, midas);

        Node node = new Node(args[0], midas);

        System.out.println("Commands:");
        System.out.println("    count - counts number of connected nodes");
        System.out.println("    send <msg> - broadcasts the message to all nodes");
        System.out.println("    trace <port> <msg> - sends the message to the given node via the network");
        System.out.println("    stop - stops current server");
        System.out.println("    all other commands are sent as text to all other clients");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().equals("stop")) {
                break;
            }
            node.send(line);
        }

        System.out.println("Stopping");
        node.stop();
        for (int i = 0; i < clients.length; i++) {
            clients[i].cancel();
        }
        server.cancel();
        scanner.close();
        System.exit(0);
    }

    private static class Node {
        private static int nodeCounter = 0;
        public final int id = nodeCounter++;
        private final MessageBroadcaster.Dispatcher mbDispatcher;
        private final MessageBroadcaster bc;

        private final RoutedMessenger.Dispatcher rmDispatcher;

        private final NodeCounter.Dispatcher ncDispatcher;

        public Node(String id, Midas midas) {
            mbDispatcher = new MessageBroadcaster.Dispatcher();
            midas.addDispatcher(mbDispatcher);
            bc = mbDispatcher.dispatch(msg -> {
                        System.out.println(
                            id + ": "
                            + msg.id + ", "
                            + msg.payload
                        );
                        // bc.cancel();
                    });
            bc.start();

            rmDispatcher = new RoutedMessenger.Dispatcher(
                id, msg -> System.out.println(
                    String.join(" -> ", msg.routes) + ": " + msg.message
                )
            );
            midas.addDispatcher(rmDispatcher);

            ncDispatcher = new NodeCounter.Dispatcher(1000, 500);
            midas.addDispatcher(ncDispatcher);
        }

        public void stop() {
            bc.cancel();
        }

        public void send(String cmd) {
            cmd = cmd.trim();

            if (cmd.equals("count")) {
                ncDispatcher.dispatch(System.out::println).start();
            } else if (cmd.startsWith("trace ")) {
                cmd = cmd.substring(6);

                int index = cmd.indexOf(" ");
                if (index < 0) {
                    System.err.println("Command must be of the form: <port> <message>");
                    return;
                }
                rmDispatcher.send(cmd.substring(0, index), cmd.substring(index + 1));
            } else if (cmd.startsWith("send ")) {
                cmd = cmd.substring(5);
                bc.send(cmd);
            }
        }
    }
}