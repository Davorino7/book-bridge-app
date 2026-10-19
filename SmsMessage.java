package m4a2_borja;

public class SmsMessage extends Message implements Trackable {

    public SmsMessage(String recipient) {
        super(recipient);
    }

    @Override
    public void send() {
        System.out.println("Sending campus notice through SMS to "
                + getRecipient() + ".");
    }

    @Override
    public void showStatus() {
        System.out.println("SMS status: Sent.");
    }
}