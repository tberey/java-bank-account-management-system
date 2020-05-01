package ATMBankManager;

import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    // User's bank account details.
    private String firstName;
    private String lastName;
    private String uuid; // ID.
    private byte pinHash[]; // Stored Pin code, in a byte Array called pinHash (empty). Will be stored hashed in MD5.
    
    // An array with a layer that adds easy list-like functions, or easier in adding new elements etc. Instead of Array.
    private ArrayList<Account> accounts; // List of user's accounts. ArrayList (imported).

    /**
     * 
     * @param firstName
     * @param lastName
     * @param pin
     * @param theBank
     */
    // @param firstName, @param lastName, @param pin, @param theBank.
    public User(String firstName, String lastName, String pin, Bank theBank) {

        // Set user's name.
        this.firstName = firstName;
        this.lastName = lastName;

        // Store the original pin as MD5 hash. A Try Catch, to catch any errors in an unrecognised getInstance argument string, (if "MD5" was invalid, it would throw the error).
        try {
            // pinHash is a byte array. Use digest function ' getBytes()', from our md MessageDigest hashing object, and use on user's pin, to get the memeory/bytes of the pin object to digest the bytes through our MD5 algorith, to return pin hash bytes. In short, we are taking pin string and returning a MD5 hashed value.

            MessageDigest md = MessageDigest.getInstance("MD5"); // MD5 Hashing algorithm.
            this.pinHash = md.digest(pin.getBytes()); // Pass pin through algo, to hash.
            
        } catch (NoSuchAlgorithmException e) {
            
            System.err.println("error: caught NoSuchAlgorithmException");
            e.printStackTrace(); //
            System.exit(1); // Exit app.
        }

        // Generate a new uuid for user.
        this.uuid = theBank.getNewUserUUID();

        // Create empty list of accounts. ArrayList constructor.
        this.accounts = new ArrayList<Account>();

        // Print log message.
        System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName, this.uuid); // Print user information.
    }

    /**
     * 
     * @param anAcct
     */
    // "void" keyword signifies no return values. Encapsulation - this public encapsulated (self-contained) method allows an account to be added to the private ArrayList 'accounts', form an outside class (using this method). There are two of these public methods for each list, which are mirrors (a user's list and bank's list).
    // @param anAcct (the account to add to User's list).
    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }

    /**
     * 
     * @return
     */
    // "String" is returned. @return uuid (user's uuid). Public method made available, to get access to a Private value.
    public String getUUID() {
        return this.uuid;
    }

    /**
     * 
     * @param aPin
     * @return
     */
    // Check whether given pin matches the particular User's pin. @param aPin (inputted pin to check). @return boolean (whether pin is valid or not).
    public boolean validatePin(String aPin) {
        try {
            // pinHash is a byte array. Use digest function ' getBytes()', from our md MessageDigest hashing object, and use on user's pin, to get the memeory/bytes of the pin object to digest the bytes through our MD5 algorith, to return pin hash bytes. In short, we are taking pin string and returning a MD5 hashed value.

            MessageDigest md = MessageDigest.getInstance("MD5"); // MD5 Hashing algorithm.
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash); // Static method 'isDigest()', to return a boolean value, after comparing this inputted pin (hashed) with our targetted User's pin. True if a matching hash is found, else false.

        } catch (NoSuchAlgorithmException e) {

            System.err.println("error: caught NoSuchAlgorithmException");
            e.printStackTrace(); //
            System.exit(1); // Exit app.
        }
        return false; // If somehow the execution stream gets here, by getInstance error catch, then we still need to return a boolean value, so we dont get a Java error.
    }

    /**
     * 
     * @return firstName (the user's first name)
     */
    // Method to access private firstName, to return the User's firstName.
    public String getFirstName() {
        return this.firstName;
    }

    // Print summaries for the accounts of the user.
    public void printAccountsSummary() {
        System.out.printf("\n\n%s's accounts summary\n", this.firstName);
        for (int i = 0; i < this.accounts.size(); i++) {
            System.out.printf(" %d) %s\n", i+1, this.accounts.get(i).getSummaryLine()); // "%d" Wildcard for integers. "%s" Wildcard for strings.
        }
        System.out.println();
    }

    /**
     * 
     * @return
     */
    // Get and return number of accounts User has.
    public int numAccounts() {
        return this.accounts.size();
    }

    /**
     * 
     * @param acctIdx
     */
    // Print the Transaction history for a particular Account.
    public void printAcctTransHistory(int acctIdx) {
        this.accounts.get(acctIdx).printAcctTransHistory();
    }

    /**
     * 
     * @param acctIdx
     * @return
     */
    // Get the balance of a particular account.
    public double getAcctBalance(int acctIdx) {
        return this.accounts.get(acctIdx).getBalance();
    }

    /**
     * 
     * @param acctIdx
     * @return
     */
    // Get the UUID of a particular Account.
    public String getAcctUUID(int acctIdx) {
        return this.accounts.get(acctIdx).getUUID();
    }

    /**
     * 
     * @param acctIdx
     * @param amount
     * @param memo
     */
    // Add a Transaction to a particular Account
    public void addAcctTransaction(int acctIdx, double amount, String memo) {
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }
}