import java.io.*;
import java.util.HashMap;

public class Storage {
    private static final String FILE_NAME = "accounts.dat";

    public static void saveAccounts(HashMap<String, BankAccount> accounts) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, BankAccount> loadAccounts() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (HashMap<String, BankAccount>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No saved accounts found. Starting fresh.");
            return new HashMap<>();
        }
    }
}
