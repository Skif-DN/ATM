package bank;

import storage.AccountsStorage;

import java.util.Scanner;

public class AdminPanel {
    private BankSystem system;
    private Scanner scanner = new Scanner(System.in);

    public AdminPanel(BankSystem system) {
        this.system = system;
    }

    void adminPanel() {
        final String ADMIN_PIN = "0000";


        System.out.print("Enter admin PIN: ");
        String inputPin = scanner.nextLine().trim();

        if (!inputPin.equals(ADMIN_PIN)) {
            System.out.println("Wrong PIN!");
            return;
        }

        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View all active accounts");
            System.out.println("2. View blocked accounts");
            System.out.println("3. View deleted accounts");
            System.out.println("4. Unblock account");
            System.out.println("5. Restore deleted account");
            System.out.println("0. Exit admin panel");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("\n--- Active Accounts ---");
                    int activeIndex = 1;
                    boolean hasActive = false;
                    for (BankAccount acc : system.getAccounts().values()) {
                        if (!acc.isBlocked() && !acc.isDeleted()) {
                            System.out.println(activeIndex + ". [ID "+ acc.getAccountId() + "] " + acc.getFullname() + "\n Username: " + acc.getUserName() + "\n Balance: $" + acc.getBalance() + "\n Created: " + acc.getFormattedCreatedAt() + "\n");
                            activeIndex ++;
                            hasActive = true;
                        }
                    }
                    if (!hasActive) {
                        System.out.println("No active accounts");
                    }
                    break;

                case "2":
                    System.out.println("\n--- Blocked Accounts ---");
                    int blockedIndex = 1;
                    boolean hasBlocked = false;
                    for (BankAccount acc : system.getAccounts().values()) {
                        if (acc.isBlocked() && !acc.isDeleted()) {
                            System.out.println(blockedIndex + ". [ID "+ acc.getAccountId() + "] " + acc.getFullname() + "\n Blocked on: " + acc.getFormattedBlockedAt() + "\n");
                            blockedIndex ++;
                            hasBlocked = true;
                        }
                    }
                    if (!hasBlocked) {
                        System.out.println("No blocked accounts");
                    }
                    break;

                case "3":
                    System.out.println("\n--- Deleted Accounts ---");
                    int deletedIndex = 1;
                    boolean hasDeleted = false;
                    for (BankAccount acc : system.getAccounts().values()) {
                        if (acc.isDeleted() && !acc.isBlocked()) {
                            System.out.println(deletedIndex + ". [ID "+ acc.getAccountId() + "] " + acc.getFullname() + "\n Deleted on: " + acc.getFormattedDeletedAt() + "\n");
                            deletedIndex ++;
                            hasDeleted = true;
                        }
                    }
                    if (!hasDeleted) {
                        System.out.println("No deleted accounts");
                    }
                    break;

                case "4":
                    System.out.print("Enter account ID to unblock: ");
                    String unblockId = scanner.nextLine().trim();

                    BankAccount unblockAcc = system.getAccountById(unblockId);

                    if (unblockAcc != null && unblockAcc.isBlocked()) {
                        unblockAcc.unblock();
                        AccountsStorage.saveAccounts(system.getAccounts());
                        System.out.println("Account '" + unblockAcc.getFullname() + "' has been unblocked.");
                    } else {
                        System.out.println("Account not found or already active.");
                    }
                    break;

                case "5":
                    System.out.print("Enter account ID to restore: ");
                    String restoreId = scanner.nextLine();

                    BankAccount restoreAcc = system.getAccountById(restoreId);

                    if (restoreAcc != null && restoreAcc.isDeleted()) {
                        restoreAcc.restore();
                        AccountsStorage.saveAccounts(system.getAccounts());
                        System.out.println("Account '" + restoreAcc.getFullname() + "' has been restored.");
                    } else {
                        System.out.println("Account not found or not deleted.");
                    }
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Wrong choice!");
            }

        }
    }
}