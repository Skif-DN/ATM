package bank;

import storage.AccountsStorage;
import storage.TransactionsStorage;
import transactions.Transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BankSystem {
    private HashMap<String, BankAccount> accounts = new HashMap<>();
    private List<Transactions> transactions = new ArrayList<>();

    public BankSystem() {
        this.accounts = AccountsStorage.loadAccounts();
        this.transactions = TransactionsStorage.loadTransactions();
    }

    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountId(), account);
        AccountsStorage.saveAccounts(accounts);
    }

    public void removeAccount(String accountId) {
        BankAccount account = accounts.get(accountId);
        if (account != null) {
            account.delete();
            AccountsStorage.saveAccounts(accounts);
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

    public void transfer (String fromUserName, String toUserName, double amount) {
        BankAccount from = getAccountByUserName(fromUserName);
        BankAccount to = getAccountByUserName(toUserName);

        if (from == null || to == null) {
            System.out.println("One of the accounts does not exist!");
            return;
        }

        try {
            if (amount <=0) throw new Exception("Amount must be positive");
            if (from.getBalance().doubleValue() < amount){
                Transactions transaction = new Transactions(from.getAccountId(), to.getAccountId(), amount, "FAILED", "Insufficient funds");
                transactions.add(transaction);
                TransactionsStorage.saveTransactions(transactions);
                System.out.println("Not enough founds!");
                return;
            }

            from.withdraw(amount);
            to.deposit(amount);

            Transactions transaction = new Transactions(from.getAccountId(), to.getAccountId(), amount, "SUCCSESS", "Trasaction completed");
            transactions.add(transaction);
            AccountsStorage.saveAccounts(accounts);
            TransactionsStorage.saveTransactions(transactions);
            System.out.println("Transfer successful!");
        } catch (Exception e){
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    public List<Transactions> getTransactions(){
        return transactions;
    }

    public HashMap<String, BankAccount> getAccounts() {
        return accounts;
    }
}
