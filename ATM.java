package ATMBankManager;

import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        // Init Scanner.
        Scanner in = new Scanner(System.in);

        // Init Bank.
        Bank theBank = new Bank("Bank of Tom");

        // Add a new User, called 'aUser', to the Bank, and also create a savings account for User.
        User aUser = theBank.addUser("John", "Doe", "1234");

        // Add a new "Checking" Account for our User.
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {
            // Stay inside login prompt until succesffuly login. Hence indefinite loop.
            curUser = ATM.mainMenuPrompt(theBank, in);

            // Stay inside main menu until user quits. Hence indefinite loop.
            ATM.printUserMenu(curUser, in);
        }
    }
    /**
     * 
     * @param theBank
     * @param in
     * @return
     */
    // Interface.
    public static User mainMenuPrompt(Bank theBank, Scanner in) { // Static keyword can be used here, because there's no data in this (ATM) class?
        // Inits.
        String userID;
        String pin;
        User authUser;

        // Prompt user for userID and Pin combo, until a set is resolved.
        do {
            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userID = in.nextLine();
            System.out.print("Enter pin: ");
            pin = in.nextLine();

            // Try to get a match for UserID and Pin combo entered.
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user ID and/or Pin entered. Try Again.");
            }

        } while(authUser == null); // Loop until 'authUser' has found a successful User login, i.e. Now has a value, (so isn't null).

        return authUser; // Return the User that has been stored in authUser, after being validated.
    }

    public static void printUserMenu(User theUser, Scanner in) { // "Static" methods allow for self reference, i.e. 'ATM.<something>();'.
        // Print a summary of the user's accounts.
        theUser.printAccountsSummary();

        // Init.
        int choice;

        // User menu.
        do {
            System.out.printf("Welcome %s, what action would you like to take?\nPress the corresponding number to choose an item from the menu below.\n", theUser.getFirstName());
            System.out.println(" 1) Show Account Transaction history.");
            System.out.println(" 2) Make a Withdrawel.");
            System.out.println(" 3) Make a Deposit.");
            System.out.println(" 4) Transfer.");
            System.out.println(" 5) QUIT.");
            System.out.println();
            System.out.print("Enter your choice (1-5): ");
            choice = in.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid input. Please input 1-5.");
            }
        } while(choice < 1 || choice > 5);

        // Process the choice.
        switch(choice) {
            case 1:
                ATM.showTransactionHistory(theUser, in);
                break;
            case 2:
                ATM.withdrawFunds(theUser, in);
                break;
            case 3:
                ATM.depositFunds(theUser, in);
                break;
            case 4:
                ATM.transferFunds(theUser, in);
                break;
            case 5:
                // Grab/or/Remove rest of the previous input line. - (Gobble up rest of previous input?). Prevents bad formatting.
                in.nextLine();
                //System.exit(1); // Exit app.
                break;
            default:
                System.out.println("Invalid input. Please input 1-5.");
                ATM.printUserMenu(theUser, in); // Recursive call (Recursion). Brings up menu again.
        }

        // Redisplay this menu unless the user quits.
        if (choice !=5) {
            ATM.printUserMenu(theUser, in); // Recursive call (Recursion). Brings up menu again.
        }
    }

    /**
     * 
     * @param theUser
     * @param in
     */
    // Show the Transaction history for an Account.
    public static void showTransactionHistory(User theUser, Scanner in) {
        int theAcct;

        // Choose Account transaction history to view.
        do {
            System.out.printf("Enter the number (1-%d) of the account " + "for which the transactions you would like to view: ", theUser.numAccounts());
            theAcct = in.nextInt()-1;
            if (theAcct < 0 || theAcct <= theUser.numAccounts()) {
                System.out.println("Invalid Account. Please try again.");
            }
        } while(theAcct < 0 || theAcct >= theUser.numAccounts());

        // Print the transaction history.
        theUser.printAcctTransHistory(theAcct);
    }

    /**
     * 
     * @param theUser
     * @param in
     */
    // Process the transferring of funds from one Account to another Account.
    public static void transferFunds(User theUser, Scanner in) {
        // Inits.
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        // Get the account to transfer from.
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer from: ", theUser.numAccounts());
            fromAcct = in.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid Account. Please try again.");
            }
        } while(fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        // Get account to transfer to.
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer to: ", theUser.numAccounts());
            toAcct = in.nextInt()-1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid Account. Please try again.");
            }
        } while(toAcct < 0 || toAcct >= theUser.numAccounts());

        // Get the amount to transfer.
        do {
            System.out.printf("Enter the amount to Transfer (max. £%.02f): £", acctBal);
            amount = in.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" + "balance of £%.02f.\n", acctBal);
            }
        } while(amount < 0 || amount > acctBal);
    
        // Perform the Transfer.
        theUser.addAcctTransaction(fromAcct, -1*amount, String.format("Transfer to Account %s", theUser.getAcctUUID(toAcct))); // Subtract Transfer amount fromAcct.
        theUser.addAcctTransaction(toAcct, amount, String.format("Transfer to Account %s", theUser.getAcctUUID(fromAcct))); // Add Transfer amount toAcct.

    }

    /**
     * 
     * @param theUser
     * @param in
     */
    // Process a withdrawel of funds from an Account.
    public static void withdrawFunds(User theUser, Scanner in) {

        // Inits.
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        // Get the account to withdraw from.
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to Withdraw from: ", theUser.numAccounts());
            fromAcct = in.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid Account. Please try again.");
            }
        } while(fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        // Get the amount to withdraw.
        do {
            System.out.printf("Enter the amount to Withdraw (max. £%.02f): £", acctBal);
            amount = in.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" + "balance of £%.02f.\n", acctBal);
            }
        } while(amount < 0 || amount > acctBal);

        // Grab/or/Remove rest of the previous input line. - (Gobble up rest of previous input?). Prevents bad formatting.
        in.nextLine();

        // Get a Memo.
        System.out.print("Enter a memo: ");
        memo = in.nextLine();

        // Do the Withdrawel.
        theUser.addAcctTransaction(fromAcct, -1*amount, memo); // Decrease amount being withdrawn.
    }

    /**
     * 
     * @param theUser
     * @param in
    */
    // Process a deposit of funds into an Account.
    public static void depositFunds(User theUser, Scanner in) {
        // Inits.
        int toAcct;
        double amount;
        String memo;

        // Get the account to depsosit into.
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "to deposit into: ", theUser.numAccounts());
            toAcct = in.nextInt()-1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid Account. Please try again.");
            }
        } while(toAcct < 0 || toAcct >= theUser.numAccounts());

        // Get the amount to deposit.
        do {
            System.out.print("Enter the amount to Deposit: £");
            amount = in.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            }
        } while(amount < 0);

        // Grab/or/Remove rest of the previous input line. - (Gobble up rest of previous input?). Prevents bad formatting.
        in.nextLine();

        // Get a Memo.
        System.out.print("Enter a memo: ");
        memo = in.nextLine();

        // Do the Deposit.
        theUser.addAcctTransaction(toAcct, amount, memo); // Increase amount being deposited.
    }
    
}