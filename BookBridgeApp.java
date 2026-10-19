import java.util.Scanner;

public class BookBridgeApp {

    // constants
    static final int TRANSACTION_COUNT = 5;
    static final int MIN_REQUESTED_DAYS = 1;
    static final int MAX_REQUESTED_DAYS = 30;

    static final String TYPE_STUDENT = "STUDENT";
    static final String TYPE_FACULTY = "FACULTY";
    static final String TYPE_GUEST = "GUEST";

    static final int STUDENT_MAX_DAYS = 14;
    static final int FACULTY_MAX_DAYS = 30;
    static final int GUEST_MAX_DAYS = 7;

    static final double STUDENT_FEE_PER_DAY = 5.0;
    static final double FACULTY_FEE_PER_DAY = 2.0;
    static final double GUEST_FEE_PER_DAY = 10.0;

    static final String STATUS_APPROVED = "APPROVED";
    static final String STATUS_RESERVED = "RESERVED";
    static final String STATUS_DENIED = "DENIED";

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Arrays (same index = same transaction)
        String[] memberIds = new String[TRANSACTION_COUNT];
        String[] memberNames = new String[TRANSACTION_COUNT];
        String[] membershipTypes = new String[TRANSACTION_COUNT];
        String[] isbns = new String[TRANSACTION_COUNT];
        String[] bookTitles = new String[TRANSACTION_COUNT];
        int[] requestedDays = new int[TRANSACTION_COUNT];
        boolean[] availability = new boolean[TRANSACTION_COUNT];
        int[] lateDays = new int[TRANSACTION_COUNT];
        String[] statuses = new String[TRANSACTION_COUNT];
        double[] lateFees = new double[TRANSACTION_COUNT];

        System.out.println("=== BookBridge Library Request Console ===");
        System.out.println("Enter " + TRANSACTION_COUNT + " borrowing requests.\n");

        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            System.out.println("--- Transaction " + (i + 1) + " of " + TRANSACTION_COUNT + " ---");

            memberIds[i] = readRequiredText(input, "Member ID: ");
            memberNames[i] = readRequiredText(input, "Member Name: ");
            membershipTypes[i] = readMembershipType(input, "Membership Type (STUDENT/FACULTY/GUEST): ");
            isbns[i] = readRequiredText(input, "ISBN: ");
            bookTitles[i] = readRequiredText(input, "Book Title: ");
            requestedDays[i] = readRequestedDays(input, "Requested Loan Days (1-30): ");
            availability[i] = readAvailability(input, "Is the book available right now? (Y/N): ");
            lateDays[i] = readLateDays(input, "Late Days (0 if none): ");
            
            // Policies: maxiumum day and daily fine depeding on member type
            int maxDays = getMaximumDays(membershipTypes[i]);
            double dailyFine = getDailyFine(membershipTypes[i]);

            statuses[i] = classifyRequest(availability[i], requestedDays[i], maxDays);
            lateFees[i] = lateDays[i] * dailyFine;

            printTransactionResult(i + 1, memberNames[i], bookTitles[i], statuses[i],
                    maxDays, dailyFine, lateFees[i]);
        }

        printSummary(memberNames, requestedDays, statuses, lateFees);

        input.close();
    }
    // =========================================================================
    // Input + validation methods

    // Keeps asking until non-blank is entered, non-blank trimmed
    static String readRequiredText(Scanner input, String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = input.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field can't be blank. Try again.");
        }
    }

    // Must be Student/Faculty/Guest
    static String readMembershipType(Scanner input, String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = input.nextLine().trim().toUpperCase();
            if (validateMembershipType(value)) {
                return value;
            }
            System.out.println("Invalid membership type. Use STUDENT, FACULTY, or GUEST.");
        }
    }

    // Rule: only S/F/G are allowed 
    static boolean validateMembershipType(String type) {
        return type.equals(TYPE_STUDENT) || type.equals(TYPE_FACULTY) || type.equals(TYPE_GUEST);
    }

    // Validates whole number, 1-30 only 
    static int readRequestedDays(Scanner input, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            String raw = input.nextLine().trim();
            try {
                value = Integer.parseInt(raw);
                if (value >= MIN_REQUESTED_DAYS && value <= MAX_REQUESTED_DAYS) {
                    return value;
                }
                System.out.println("Requested days must be between " + MIN_REQUESTED_DAYS
                        + " and " + MAX_REQUESTED_DAYS + ".");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    // Y or N only, case-insensitive
    static boolean readAvailability(Scanner input, String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = input.nextLine().trim().toUpperCase();
            if (value.equals("Y")) {
                return true;
            }
            if (value.equals("N")) {
                return false;
            }
            System.out.println("Please answer Y or N.");
        }
    }

    // Late days: whole number, 0 or higher only
    static int readLateDays(Scanner input, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            String raw = input.nextLine().trim();
            try {
                value = Integer.parseInt(raw);
                if (value >= 0) {
                    return value;
                }
                System.out.println("Late days can't be negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    // ---------------------------------------------------------------
    // Policy + decision methods

    // max loan days by member type
    static int getMaximumDays(String membershipType) {
        switch (membershipType) {
            case TYPE_STUDENT:
                return STUDENT_MAX_DAYS;
            case TYPE_FACULTY:
                return FACULTY_MAX_DAYS;
            case TYPE_GUEST:
                return GUEST_MAX_DAYS;
            default:
                return 0;
        }
    }

    // daily late fee by member type
    static double getDailyFine(String membershipType) {
        switch (membershipType) {
            case TYPE_STUDENT:
                return STUDENT_FEE_PER_DAY;
            case TYPE_FACULTY:
                return FACULTY_FEE_PER_DAY;
            case TYPE_GUEST:
                return GUEST_FEE_PER_DAY;
            default:
                return 0.0;
        }
    }

    /* unavailable: reserved
       within limit: approved
       else: denied */
    static String classifyRequest(boolean isAvailable, int requestedDays, int maxDays) {
        if (!isAvailable) {
            return STATUS_RESERVED;
        }
        if (requestedDays <= maxDays) {
            return STATUS_APPROVED;
        }
        return STATUS_DENIED;
    }

    // ---------------------------------------------------------------
    // Output

    static void printTransactionResult(int txnNumber, String memberName, String bookTitle,
            String status, int maxDays, double dailyFine, double lateFee) {
        System.out.println();
        System.out.println("Result for Transaction " + txnNumber + ":");
        System.out.println("  Member: " + memberName);
        System.out.println("  Book: " + bookTitle);
        System.out.println("  Status: " + status);
        System.out.println("  Max allowed days for this member: " + maxDays);
        System.out.println("  Late fee rate: PHP " + dailyFine + "/day");
        System.out.println("  Estimated late charge: PHP " + lateFee);
        System.out.println();
    }

    // outcome counts, the longest single request, and total late charge //
    static void printSummary(String[] memberNames, int[] requestedDays, String[] statuses, double[] lateFees) {
        int approvedCount = 0;
        int reservedCount = 0;
        int deniedCount = 0;
        double totalLateCharge = 0.0;

        int longestIndex = 0;

        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            switch (statuses[i]) {
                case STATUS_APPROVED:
                    approvedCount++;
                    break;
                case STATUS_RESERVED:
                    reservedCount++;
                    break;
                case STATUS_DENIED:
                    deniedCount++;
                    break;
            }

            totalLateCharge += lateFees[i];

            if (requestedDays[i] > requestedDays[longestIndex]) { // longest requested days so far
                longestIndex = i;
            }
        }

        System.out.println("=== Operational Summary ===");
        System.out.println("Approved: " + approvedCount);
        System.out.println("Reserved: " + reservedCount);
        System.out.println("Denied: " + deniedCount);
        System.out.println("Longest request: " + memberNames[longestIndex]
                + " (" + requestedDays[longestIndex] + " days)");
        System.out.println("Total estimated late charge: PHP " + totalLateCharge);
    }
}
