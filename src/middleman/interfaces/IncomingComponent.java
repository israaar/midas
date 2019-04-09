package middleman.interfaces;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import middleman.interfaces.Component;
import middleman.interfaces.Message;

class OutcomeComponent extends Component {
    private final int nodeID;

    public OutcomeComponent(int nodeID) {
        this.nodeID = nodeID;
    }

    @Override
    public void onMessageReceived(Message message) {
        
    }

    /**
     * Neighbors Heartbeat - Send Heartbeat ever X - seconds
     */
    public void SendHeartbeatMessage() {
        //super.send()
    }

    /**
     * Neighbors Response - Send Response when receiving Response
     */
    public void SendResponseMessage() {
        //super.send()
    }
}