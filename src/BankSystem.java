import java.util.HashMap;

public class BankSystem {
    private HashMap<String, BankAccount> accounts = new HashMap<>();

    public BankSystem() {
        this.accounts = Storage.loadAccounts();
    }

    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountId(), account);
        Storage.saveAccounts(accounts);
    }

    public void removeAccount(String accountId) {
        BankAccount account = accounts.get(accountId);
        if (account != null) {
            account.delete();
            Storage.saveAccounts(accounts);
        }
    }

    public BankAccount getAccount(String userName) {
        return accounts.get(userName);
    }

    public BankAccount getAccountById(String accountId) {
        return accounts.get(accountId);
    }

    public BankAccount getAccountByUserName(String userName) {
        for (BankAccount acc : accounts.values()) {
            if (acc.getUserName().equalsIgnoreCase(userName)) {
                return acc;
            }
        }
        return null;
    }

    public BankAccount getAccountByFullName (String fullName){
        for (BankAccount acc : accounts.values()){
            if (acc.getFullname().equalsIgnoreCase(fullName)) {
                return acc;
            }
        }
        return null;
    }

    public HashMap<String, BankAccount> getAccounts() {
        return accounts;
    }

}
