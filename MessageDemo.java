package m4a2_borja;

public class MessageDemo {

    public static void main(String[] args) {
        EmailMessage email = new EmailMessage("dav.borja@school.edu");
        SmsMessage sms = new SmsMessage("09123456789");

        Message[] messages = {email, sms};

        System.out.println("--- Test 1: Message Array ---");

        for (Message message : messages) {
            message.send();
        }

        System.out.println("\n--- Test 2: Trackable Reference ---");

        Trackable trackedMessage = email;
        trackedMessage.showStatus();

        System.out.println("\n--- Polymorphism Explanation ---");
        System.out.println("Upcasting occurs when EmailMessage and SmsMessage objects are stored as Message references.");
        System.out.println("Dynamic dispatch selects the correct send() method based on the object's actual type at runtime.");
    }
}