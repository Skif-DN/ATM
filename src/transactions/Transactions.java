package transactions;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transactions implements Serializable {
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

    public String toString(){
        return "Transaction{" +
                "id='" + transactionId + '\'' +
                ", from='" + fromAccountId + '\'' +
                ", to='" + toAccountId + '\'' +
                ", amount=" + amount +
                ", date=" + dateTime +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
