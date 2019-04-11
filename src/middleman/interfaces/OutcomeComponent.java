package middleman.interfaces;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import middleman.interfaces.Component;
import middleman.interfaces.Message;

class OutcomeComponent extends Component {
    private Map<Integer, Boolean> neighborIds = new HashMap<Integer, Boolean>();
    private final int nodeID;

    public OutcomeComponent(int nodeID) {
        this.nodeID = nodeID;
    }

    public void send(byte[] payload, UUID convoUUID) {
        
    }

    @Override
    public void onMessageReceived(Message message) {
        
    }
}