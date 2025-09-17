import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private String fullName;
    private String hashedPin;
    private BigDecimal balance;
    private int failedAttempts = 0;
    private boolean isBlocked = false;
    private boolean isDeleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime blockedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime lastDepositAt;
    private LocalDateTime lastWithdrawAt;

    public BankAccount(String firstName, String lastName, String pin, double balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.hashedPin = Security.hashPin(pin);
        this.balance = BigDecimal.valueOf(balance).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.createdAt = LocalDateTime.now();

        if (balance > 0) {
            this.lastDepositAt = LocalDateTime.now();
        } else
            this.lastDepositAt = null;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullname(){
        return fullName;
    }

    public boolean checkPin(String inputPin) {
        if (isBlocked) return false;

        String hashedInput = Security.hashPin(inputPin);
        if (hashedInput.equals(hashedPin)) {
            failedAttempts = 0;
            return true;
        } else {
            failedAttempts++;
            if (failedAttempts >= 3) {
                setBlocked(true);
            }
            return false;
        }

    }

    public void setPin(String newPin) {
        this.hashedPin = Security.hashPin(newPin);
        this.failedAttempts = 0;
        this.isBlocked = false;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        BigDecimal depositAmount = BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
        balance = balance.add(depositAmount);
        lastDepositAt = LocalDateTime.now();
    }

    public boolean withdraw(double amount) {
        BigDecimal withdrawAmount = BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (balance.compareTo(withdrawAmount) >= 0) {
            balance = balance.subtract(withdrawAmount);
            lastWithdrawAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void delete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.isDeleted = false;
        this.deletedAt = null;
    }

    public boolean isBlocked() {
        return isBlocked;
    }


    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;
        if (blocked) {
            blockedAt = LocalDateTime.now();
        } else {
            blockedAt = null;
        }
    }

    public void unblock() {
        isBlocked = false;
        failedAttempts = 0;
        blockedAt = null;
    }

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }

    public String getFormattedBlockedAt() {
        return blockedAt != null ? blockedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "No blocked yet";
    }

    public String getFormattedDeletedAt() {
        return deletedAt != null ? deletedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "No delete yet";
    }

    public String getFormattedLastDepositAt() {
        return lastDepositAt != null ? lastDepositAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "No deposit yet";
    }

    public String getFormattedLastWithdrawAt() {
        return lastWithdrawAt != null ? lastWithdrawAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "No withdraw yet";
    }

}
