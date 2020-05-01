package ATMBankManager;

import java.util.ArrayList;

public class Account {
    // Account details.
    private String name;
    private String uuid; // Account's uuid (ID). Different from user's.
    private User holder; // Account holder (user who owns account).
    private ArrayList<Transaction> transactions; // List of transactions. ArrayList (imported).
    
    /**
     * 
     * @param name
     * @param holder
     * @param theBank
     */
    // Create a new Account. @param name (String Name of Account), @param holder (User object that owns/hold this account), @param theBank (bank that issues this Account).
    public Account(String name, User holder, Bank theBank) {
        
        // Set account name and holder.
        this.name = name;
        this.holder = holder;

        // Get new account uuid.
        this.uuid = theBank.getNewAccountUUID();

        // init transactions.
        this.transactions = new ArrayList<Transaction>();
    }

    /**
     * 
     * @return
     */
    // "String" is returned. @return uuid (account's uuid). Public method available to get access to a Private value.
    public String getUUID() {
        return this.uuid;
    }

    /**
     * 
     * @return
     */
    // Get Summary line for the account
    public String getSummaryLine() {
        // Get balance.
        double balance = this.getBalance();

        // Format the sumamry line, dependant on negative or positive balance.
        if (balance >= 0) {
            return String.format("%s : £%.02f : %s", this.uuid, balance, this.name); // Similar to 'printf()'. "%.02f" Wildcard for a double with 2 decimal places. 
        } else {
            return String.format("%s : £(%.02f) : %s", this.uuid, balance, this.name); // Similar to 'printf()'. "%.02f" Wildcard for a double with 2 decimal places. 
        }
    }

    /**
     * 
     * @return
     */
    // Get and return balance amount of Account.
    public double getBalance() {
        double balance = 0;
        for (Transaction t: this.transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    // Print the Transaction history for the Account.
    public void printAcctTransHistory() {
        System.out.printf("\nTransaction history for account %s\n", this.uuid);
        for (int i = this.transactions.size()-1; i >= 0; i--) {
            System.out.println(this.transactions.get(i).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * 
     * @param amount
     * @param memo
     */
    // Create a new Transaction object and add it to our list.
    public void addTransaction(double amount, String memo) {
        Transaction newTrans = new Transaction(amount, memo, this); //
        this.transactions.add(newTrans);
    }
}