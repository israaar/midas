package middleman.tests.count;

public class Test {

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
        ArrayList<Integer> clientPorts = new ArrayList<Integer>();
        for (int i = 1; i < args.length; i++) {
            try {
                Integer clientPort = Integer.parseInt(portStr);
                clientPorts.add(clientPort);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number arg for client port number.");
                e.printStackTrace();
                return;
            }
        }

        Medium[] mediums = new Medium[args.length];
        mediums[0] = new Server(port);
        for (int i = 1; i < mediums.length; i++) {
            mediums[i] = new Client(clientPorts.get(i));
        }
        Dispatcher<MessageBroadcaster.Dispatcher>[] dispatchers = new Dispatcher<>[] {
            new MessageBroadcaster.Dispatcher()
        };

        Node node = new Node(
            new MiddleMan("BasicTest", dispatchers, mediums)
        );

        // sleep for sometime for each server to go up
        try{
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println("Errored when sleeping");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        int i = scanner.nextInt();
    }

    private static class Node {
        private static int nodeCounter = 0;
        public final int id = nodeCounter++;

        public MessageBroadcaster bc;

        public Node(MiddleMan middleman) {
            bc = middleman
                    .getDispatcher(MessageBroadcaster.Dispatcher.class)
                    .dispatch(msg -> {
                        System.out.println(
                            id + ": "
                            + msg.id + ", "
                            + msg.payload
                        );
                        bc.cancel();
                    });
            bc.start();
        }

        public void send(String msg) {
            bc.send(msg);
        }
    }

}