package examples.components;

import java.io.Serializable;
import java.util.function.Consumer;

import midas.interfaces.*;

public class RoutedMessenger extends Component {
    protected RoutedMessenger(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void run() {

    }

    public static final class Dispatcher extends midas.interfaces.Dispatcher<RoutedMessenger> {
        private final String id;
        private final Consumer<RoutedMessage> callback;

        public Dispatcher(String id, Consumer<RoutedMessage> callback) {
            this.id = id;
            this.callback = callback;
        }

        public void send(String recipientId, String message) {
            getMidas().send(new Message<>(
                new RoutedMessage(recipientId, message, new String[] {id}),
                this
            ));
        }

        @Override
        public void handleMessage(Message<?> msg) {
            RoutedMessage payload = (RoutedMessage) msg.payload;

            if (payload.recipientId.equals(id)) {
                callback.accept(payload);
            } else {
                for (String rid : payload.routes) {
                    if (rid.equals(id)) return;
                }

                getMidas().send(new Message<>(
                    msg.id,
                    payload.addRoute(id),
                    this
                ));
            }
        }
    }

    public static class RoutedMessage implements Serializable {
        private static final long serialVersionUID = 1L;

        public final String recipientId;
        public final String message;
        public final String[] routes;

        public RoutedMessage(String recipientId, String message, String[] routes) {
            this.recipientId = recipientId;
            this.message = message;
            this.routes = routes;
        }

        public RoutedMessage addRoute(String id) {
            String[] newRoutes = new String[routes.length + 1];
            System.arraycopy(routes, 0, newRoutes, 0, routes.length);
            newRoutes[routes.length] = id;

            return new RoutedMessage(recipientId, message, newRoutes);
        }
    }
}