package ATMBankManager;

import java.util.Date;

public class Transaction {
    private double amount;
    private Date timestamp;
    private String memo; // A note attached to a transaction, explaining nature of transaction.
    private Account inAccount; // Account transaction was performed.

    // Overloading contructors: Two constructors for a new Transaction. The correct one is deduced by the arguments passed with a call, done by Java/JVM.

    /**
     * 
     * @param amount
     * @param inAccount
     */
    // Transaction constructor in case of a transaction with No optional Memo attached to it. Set all properties, and Memo to "".
    // Create a new Transaction. @param amount (amount involved in transaction), @param inAccount (the account the transaction belongs to).
    public Transaction(double amount, Account inAccount) {
        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date(); // Current date.
        this.memo = ""; // There is no memo, so set to empty string.
    }

    /**
     * 
     * @param amount
     * @param memo
     * @param inAccount
     */
    // Transaction constructor in case of a transaction WITH an optional memo attached. Set the attached Memo.
    // Create a new Transaction. @param amount (amount involved in transaction), @param memo (memo for the transaction), @param inAccount (the account the transaction belongs to).
    public Transaction(double amount, String memo, Account inAccount) {
        
        // Call the other constructor first. (The two argument Contructor, where NO Memo is in argument or with the Transaction).
        this(amount, inAccount);

        // With all the properties now set (by calling prev. constructor), but without the Memo (set as ""), we can set the Memo here, (from "", to the argument value), in this Constructor.
        this.memo = memo;
    }

    /**
     * 
     * @return
     */
    // Get the amount of a transaction.
    public double getAmount() {
        return this.amount;
    }

    /**
     * 
     * @return
     */
    // Get a string with the summary of a Transaction.
    public String getSummaryLine() {
        if(this.amount >= 0) {
            return String.format("%s : £%.02f : %s", this.timestamp.toString(), this.amount, this.memo);
        } else {
            return String.format("%s : £%.02f : %s", this.timestamp.toString(), -this.amount, this.memo);
        }
    }
}