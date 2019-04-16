package middleman.implementations.components;

import middleman.interfaces.*;

class NodeDirectMessage extends Component {
    public NodeDirectMessage(Dispatcher<NodeDirectMessage> d) {
        super(d);
    }

    public void onMessageReceived(Message<?> message) {
        // TODO
    }

    @Override
    protected void run() {

    }
}