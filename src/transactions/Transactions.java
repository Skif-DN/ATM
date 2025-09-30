package transactions;

import bank.BankSystem;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transactions implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String transactionId;
    private String fromAccountId;
    private String toAccountId;
    private String status;
    private String message;
    private double amount;
    private LocalDateTime dateTime;

    public Transactions (String fromAccountId, String toAccountId, double amount, String status, String message) {
        this.transactionId = UUID.randomUUID().toString();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = status;
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }


    public String getFromAccountId (){
        return this.fromAccountId;
    }

    public String getToAccountId(){
        return this.toAccountId;
    }

    @Override
    public String toString(){
        BankSystem system = new BankSystem();
        String fromUser = system.getAccountById(fromAccountId) != null ? system.getAccountById(fromAccountId).getUserName() : "Unknown";
        String toUser   = system.getAccountById(toAccountId) != null ? system.getAccountById(toAccountId).getUserName() : "Unknown";


        return "Transaction{" +
                "id='" + transactionId + '\'' +
                ", from='" + fromUser + '\'' +
                ", to='" + toUser + '\'' +
                ", amount=$" + amount +
                ", date=" + dateTime +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
