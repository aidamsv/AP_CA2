/**
 * Created by aida on 11/18/15.
 */
public class Transaction {
    private String transactionId;
    private String type;
    private int amount;
    private String depositId;

    public Transaction(String transactionId, String type, int amount, String depositId) throws Exception {
        this.transactionId = transactionId;

        if("withdraw".equals(type) || "deposit".equals(type))
            this.type = type;
        else
            throw new Exception("Invalid transaction type using in building transaction "+transactionId);

        if(amount < 0)
            throw new Exception("Negetive amount using in building transaction "+transactionId);
        this.amount = amount;

        this.depositId = depositId;
    }

//    public Transaction(String s) throws Exception {
//        String[] tokens =  s.split(",");
//        transactionId = tokens[0];
//        type = tokens[1];
//        amount = Integer.parseInt(tokens[2]);
//        if(amount<0) throw new Exception("Negetive amount");
//        depositId = tokens[2];
//
//    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getDepositId() {
        return depositId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", depositId='" + depositId + '\'' +
                '}';
    }

//    @Override
//    public String toString() {
//        return transactionId +","+type+","+amount+","+depositId;
//    }
}
