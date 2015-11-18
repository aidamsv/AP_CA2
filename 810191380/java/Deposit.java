/**
 * Created by aida on 11/17/15.
 */
public class Deposit {
    private String customer;
    private String id;
    private int initialBalance;
    private int upperBound;

    Deposit(String customer, String id, int initialBalance, int upperBound) {
        this.customer = customer;
        this.id = id;
        this.initialBalance = initialBalance;
        this.upperBound = upperBound;
    }


    public String getCustomer() {
        return customer;
    }
    public String getId() {
        return id;
    }
    public int getInitialBalance() {
        return initialBalance;
    }
    public int getUpperBound() {
        return upperBound;
    }

    private void deposit(int amount) throws Exception {
        synchronized (id) {
            if (amount + initialBalance <= upperBound)
                initialBalance += amount;
            else
                throw new Exception("error of upperbound in running transaction on deposit " + id);

        }
    }
    private void withdraw(int amount) throws Exception {
        synchronized (id) {
            if (initialBalance - amount >= 0)
                initialBalance -= amount;
            else
                throw new Exception("limited initialbalance running transaction on deposit " + id);
        }
    }

    public void runTransaction(Transaction transaction) throws Exception {
        String depositId = transaction.getDepositId();
        if(! depositId.equals(id)) {
            throw new Exception ("Invalid deposit id");
        }
        int amount = transaction.getAmount();
        String transactionType = transaction.getType();
        if("withdraw".equals(transactionType))
            this.withdraw(amount);
        else if("deposit".equals(transactionType))
            this.deposit(amount);
        else
            throw new Exception("Invalid transaction type");
    }

}
