package bank;

import storage.AccountsStorage;
import storage.TransactionsStorage;
import transactions.Transactions;
import utils.Utils;

import java.math.BigDecimal;
import java.util.Scanner;

public class ATM {
    private BankSystem system;
    private Scanner scanner = new Scanner(System.in);

    public ATM(BankSystem system) {
        this.system = system;
    }

    public void start() {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Create account");
            System.out.println("2. Enter to account");
            System.out.println("3. Admin panel");
            System.out.println("4. EXIT");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createAccount();
                    break;

                case "2":
                    login();
                    break;

                case "3":
                    AdminPanel admin = new AdminPanel(system);
                    admin.adminPanel();
                    break;

                case "4":
                    System.out.println("Good Bye! \nExiting...");
                    return;

                default:
                    System.out.println("Incorrect choice!");
            }
        }
    }

    private void createAccount() {
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();
        String formattedFirstName = Utils.capitalizeFirstLetter(firstName);

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        String formattedLastName = Utils.capitalizeFirstLetter(lastName);

        System.out.print("Enter your username: ");
        String userName = scanner.nextLine();

        if (system.getAccountByUserName(userName) != null) {
            System.out.println("This username is already taken!");
            return;
        }

        System.out.print("Create PIN (4 digits): ");
        String pin = scanner.nextLine();

        if (!pin.matches("\\d{4}")) {
            System.out.println("The PIN must consist of 4 digits!");
            return;
        }

        System.out.print("Confirm new PIN: ");
        String pin2 = scanner.nextLine();
        if (!pin.equals(pin2)) {
            System.out.println("PINs do not match!");
            return;
        }

        System.out.print("Deposit: $");
        double initialDeposit;
        try {
            initialDeposit = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong number!");
            return;
        }

        BankAccount newAccount = new BankAccount(formattedFirstName, formattedLastName, userName, pin, initialDeposit);
        system.addAccount(newAccount);
        System.out.println("Account created successfully!");
    }

    private void login() {
        System.out.print("Enter your username: ");
        String userName = scanner.nextLine();

        BankAccount account = system.getAccountByUserName(userName);

        if (account == null) {
            System.out.println("Account not found");
            return;
        }

        if (account.isBlocked()) {
            System.out.println("This account is blocked due to multiple failed PIN attempts.");
            AccountsStorage.saveAccounts(system.getAccounts());
            return;
        }

        if (account.isDeleted()) {
            System.out.println(("This account has been deleted!"));
            AccountsStorage.saveAccounts(system.getAccounts());
            return;
        }

        while (true) {
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            if (pin.equalsIgnoreCase("exit")) {
                System.out.println("Login cancelled");
                return;
            }

            if (account.checkPin(pin)) {
                System.out.println("Welcome, " + account.getFullname() + "!");
                AccountsStorage.saveAccounts(system.getAccounts());
                menu(account);
                return;
            } else {
                if (account.isBlocked()) {
                    System.out.println("Account has been blocked due to 3 failed attempts!");
                    AccountsStorage.saveAccounts(system.getAccounts());
                    return;
                } else {
                    System.out.println("Incorrect PIN! Try again or type \"exit\" to cancel");
                }
            }

        }
    }

        private void menu (BankAccount account){
            while (true) {
                System.out.println("\n--- Account MENU ---");
                System.out.println("1. Account info");
                System.out.println("2. Withdraw money");
                System.out.println("3. Deposit money");
                System.out.println("4. Transfer money");
                System.out.println("5. Transactions");
                System.out.println("6. Change PIN");
                System.out.println("7. Close account");
                System.out.println("8. Return to main menu");
                System.out.print("Choose option: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.println("\n--- ACCOUNT INFO ---");
                        System.out.println("Account: " + account.getFullname());
                        System.out.println("Balance: $" + account.getBalance());
                        System.out.println("Created: " + account.getFormattedCreatedAt());
                        System.out.println("Last deposit: " + account.getFormattedLastDepositAt());
                        System.out.println("Last withdraw: " + account.getFormattedLastWithdrawAt());
                        break;

                    case "2":
                        System.out.print("Withdraw: $");
                        String withdrawInput = scanner.nextLine();
                        try {
                            double amountWithdraw = Double.parseDouble(withdrawInput);
                            if (account.withdraw(amountWithdraw)) {
                                System.out.println("Money is withdrawn");
                                AccountsStorage.saveAccounts(system.getAccounts());
                            } else {
                                System.out.println("Not enough funds!");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid amount!");
                        }

                        break;

                    case "3":
                        System.out.print("Deposit: $");
                        String depositInput = scanner.nextLine();
                        try {
                            double amountDeposit = Double.parseDouble(depositInput);
                            account.deposit(amountDeposit);
                            AccountsStorage.saveAccounts(system.getAccounts());
                            System.out.println("Money deposited");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid amount!");
                        }
                        break;

                    case"4":
                        System.out.println("Enter recipient's username: ");
                        String recipientUserName = scanner.nextLine().trim();

                        BankAccount recipient = system.getAccountByUserName(recipientUserName);

                        if (recipient == null) {
                            System.out.println("Recipient not found");
                            break;
                        }

                        System.out.println("Enter amount to transfer $: ");
                        double amount;
                        try {
                            amount = Double.parseDouble(scanner.nextLine());
                        } catch (NumberFormatException e){
                            System.out.println("Invalid amount");
                            break;
                        }

                        system.transfer(account.getUserName(), recipientUserName, amount);
                        break;

                    case "5":
                        System.out.println("\n--- Your Transactions ---");
                        boolean hasTx = false;
                        for (Transactions tx : system.getTransactions()) {
                            if (tx.getFromAccountId().equals(account.getAccountId()) ||
                                    tx.getToAccountId().equals(account.getAccountId())) {
                                System.out.println(tx);  // тут викличеться toString()
                                hasTx = true;
                            }
                        }
                        if (!hasTx) {
                            System.out.println("No transactions found.");
                        }
                        break;

                    case "6":
                        System.out.print("Enter current PIN: ");
                        String currentPin = scanner.nextLine();
                        if (!account.checkPin(currentPin)) {
                            System.out.println("Incorrect current PIN!");
                            break;
                        }

                        System.out.print("Enter new PIN (4 digits): ");
                        String newPin1 = scanner.nextLine();
                        if (!newPin1.matches("\\d{4}")) {
                            System.out.println("PIN must be 4 digits!");
                            break;
                        }

                        System.out.print("Confirm new PIN: ");
                        String newPin2 = scanner.nextLine();
                        if (!newPin1.equals(newPin2) || !newPin2.matches("\\d{4}")) {
                            System.out.println("PINs do not match!");
                            break;
                        }

                        account.setPin(newPin1);
                        AccountsStorage.saveAccounts(system.getAccounts());
                        System.out.println("PIN successfully changed!");
                        return;

                    case "7":
                        if (account.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                            String confirm;
                            while (true) {
                                System.out.print("Are you sure you want to close your account? (yes/no): ");
                                confirm = scanner.nextLine().trim().toLowerCase();

                                if (confirm.equals("yes")) {
                                    system.removeAccount(account.getAccountId());
                                    System.out.println("Account successfully closed.");
                                    return;
                                } else if (confirm.equals("no")) {
                                    System.out.println("Account closure cancelled.");
                                    break;
                                } else {
                                    System.out.println("Invalid input. Please type 'yes' or 'no'.");
                                }
                            }
                        } else {
                            System.out.println("You can close your account only if the balance is $0.00");
                        }
                        break;

                    case "8":
                        return;
                    default:
                        System.out.println("Incorrect choice");
                }
            }
        }
}