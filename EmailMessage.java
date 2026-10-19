package m4a2_borja;

public class EmailMessage extends Message implements Trackable {

    public EmailMessage(String recipient) {
        super(recipient);
    }

    @Override
    public void send() {
        System.out.println("Sending campus notice through email to "
                + getRecipient() + ".");
    }

    @Override
    public void showStatus() {
        System.out.println("Email status: Delivered.");
    }
}