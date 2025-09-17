import java.util.HashMap;

public class BankSystem {
    private HashMap<String, BankAccount> accounts = new HashMap<>();

    public BankSystem() {
        this.accounts = Storage.loadAccounts();
    }

    public void addAccount(BankAccount account) {
        accounts.put(account.getFullname(), account);
        Storage.saveAccounts(accounts);
    }

    public void removeAccount(String accountName) {
        BankAccount account = accounts.get(accountName);
        if (account != null) {
            account.delete();
            Storage.saveAccounts(accounts);
        }
    }

    public BankAccount getAccount(String firstName, String lastName) {
        String fullName = firstName + " " + lastName;
        return accounts.get(fullName);
    }

    public HashMap<String, BankAccount> getAccounts() {
        return accounts;
    }

}
