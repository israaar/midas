package middleman.interfaces;

public abstract class Component implements MessageReceiveListener {
    private Medium medium;

    public void connectMedium(Medium medium) {
        this.medium = medium;
        medium.onReceive(this);
    }

    protected void send(Message message) {
        this.medium.send(message);
    }
}