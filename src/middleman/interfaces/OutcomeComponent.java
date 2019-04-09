package middleman.interfaces;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
        CustomMessage ret = CustomMessage.Deserilizing(message.payload);

        if (ret.convoUUID != null && ret.convoUUID.equals(convoUUID)) {
            // received message is what it sent
            switch (ret.type) {
                case HeartBeat:
                    break;
                case Response:
                    break;
                case Sent:
                    break;
            }
        }
    }

    public static final int HeartBeat = 1;
    public static final int Response = 2;
    public static final int Sent = 3;

    public static class HeartBeatMessage extends CustomMessage {
        public HeartBeatMessage(String appName, String componentName, UUID convoUUID) {
            super(appName, componentName, HeartBeat, convoUUID);
        }
    }

    public static class ResponseMessage extends CustomMessage {
        public final byte[] payload;
        public ResponseMessage(String appName, String componentName, byte[] payload, UUID convoUUID) {
            super(appName, componentName, Response, convoUUID);
            this.payload = payload;
        }
    }

    public static class CountMessage extends CustomMessage {
        public final byte[] payload;
        public CountMessage(String appName, String componentName, byte[] payload, UUID convoUUID) {
            super(appName, componentName, Sent, convoUUID);
            this.payload = payload;
        }
    }

    public static class CustomMessage {
        public final int type;
        public final String appName;
        public final String componentName;
        public final UUID convoUUID;

        public CustomMessage(String appName, String componentName, int type, UUID convoUUID) {
            this.type = type;
            this.appName = appName;
            this.componentName = componentName;
            this.convoUUID = convoUUID;
        }

        public byte[] Serilize() {
            try (
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutput out = new ObjectOutputStream(byteArrayOutputStream);
                ) {
                out.writeObject(this);
                out.flush();
                return byteArrayOutputStream.toByteArray();
            } catch (IOException ex) {
                return null;
            }
        }

        public static CustomMessage Deserilizing(byte[] payload) {
            try (
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload);
                    ObjectInput in = new ObjectInputStream(byteArrayInputStream);
                ) {
                return (CustomMessage) in.readObject();
            } catch (IOException ex) {
                return null;
            } catch (ClassNotFoundException ex) {
                return null;
            }
        }
    }
}