package examples;

import java.util.ArrayList;
import java.util.Scanner;

import examples.components.NodeCounter;
import examples.components.RoutedMessenger;
import examples.components.MessageBroadcaster;
import examples.media.tcp.*;
import middleman.MiddleMan;

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

        MiddleMan middleman = new MiddleMan("TCPExample");

        Client[] clients = new Client[args.length-1];
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new Client(clientPorts.get(i));
            middleman.addMedium(clients[i]);
        }

        Server server = new Server(serverPort, middleman);

        Node node = new Node(args[0], middleman);

        System.out.println("Commands:");
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
        private final MessageBroadcaster.Dispatcher dispatcher;
        private final MessageBroadcaster bc;

        // private final RoutedMessenger.Dispatcher dispatcher;

        // private final NodeCounter.Dispatcher dispatcher;

        public Node(String id, MiddleMan middleman) {
            dispatcher = new MessageBroadcaster.Dispatcher();
            middleman.addDispatcher(dispatcher);
            bc = dispatcher.dispatch(msg -> {
                        System.out.println(
                            id + ": "
                            + msg.id + ", "
                            + msg.payload
                        );
                        // bc.cancel();
                    });
            bc.start();

            // // dispatcher = new RoutedMessenger.Dispatcher(
            //     id, msg -> System.out.println(
            //         String.join(" -> ", msg.routes) + ": " + msg.message
            //     )
            // );
            // middleman.addDispatcher(dispatcher);

            // dispatcher = new NodeCounter.Dispatcher(1000, 500);
            // middleman.addDispatcher(dispatcher);
        }

        public void stop() {
            bc.cancel();
        }

        public void send(String cmd) {
            bc.send(cmd);

            // int index = cmd.indexOf(" ");
            // if (index < 0) {
            //     System.err.println("Command must be of the form: <port> <message>");
            //     return;
            // }
            // dispatcher.send(cmd.substring(0, index), cmd.substring(index + 1));

            // dispatcher.dispatch(System.out::println).start();
        }
    }
}