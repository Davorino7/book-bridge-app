package m4a2_borja;

public abstract class Message {
    private String recipient;

    public Message(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public abstract void send();
}