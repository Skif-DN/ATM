package storage;

import transactions.Transactions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionsStorage {
    private static final String FILE_NAME = "transactions.dat";
    private static List<Transactions> transactions = new ArrayList<>();

    public static List<Transactions> loadTransactions(){
        File file = new File(FILE_NAME);
        if (!file.exists())
            return new ArrayList<>();

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
           return (List<Transactions>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveTransactions(List<Transactions> transactions){
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            output.writeObject(transactions);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void addTransaction(Transactions transaction){
        transactions.add(transaction);
        saveTransactions(transactions);
    }

    public static List<Transactions> getTransactions() {
        return transactions;
    }

    public static List<Transactions> getTransactionsForAccount(String accountId, List<Transactions> allTransactions){
        List<Transactions> result = new ArrayList<>();
        for (Transactions transaction : allTransactions){
            if (transaction.getFromAccountId().equals(accountId) || transaction.getToAccountId().equals(accountId)){
                result.add(transaction);
            }
        }
        return result;
    }
}
