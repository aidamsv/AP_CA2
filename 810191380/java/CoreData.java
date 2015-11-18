import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aida on 11/17/15.
 */
public class CoreData {

    class Deposit {
        private String customer;
        private String id;
        private String initialBalance;
        private String upperBound;

        public String getCustomer() {return customer; }
        public String getId() {
            return id;
        }
        public String getInitialBalance() {
            return initialBalance;
        }
        public String getUpperBound() {
            return upperBound;
        }

        public Deposit(String customer, String id, String initialBalance, String upperBound) {
            this.customer = customer;
            this.id = id;
            this.initialBalance = initialBalance;
            this.upperBound = upperBound;
        }
    }

    private int port;
    private List<Deposit> deposits;
    private String outLog;

    public int getPort() {
        return port;
    }
    public List<Deposit> getDeposits() {
        return deposits;
    }
    public String getOutLog() {
        return outLog;
    }

    public void setPort(int port) {
        this.port = port;
    }

    void addDeposit(String customer, String id, int initialBalance, int upperBound) {
        deposits.add(new Deposit(customer, id, Integer.toString(initialBalance), Integer.toString(upperBound)));
    }

    void clearDeposits() {
        deposits.clear();
    }
    public void setDeposits(List<Deposit> deposits) {
        this.deposits = deposits;
    }
    //    public void setDeposits(List<Deposit> depositList) {
//        deposits = new ArrayList<Deposit>();
//        for(int i=0; i<depositList.size(); i++) {
//            Deposit deposit = depositList.get(i);
//            depositList.add(new CoreData.Deposit(deposit.getCustomer(), deposit.getId(),
//                                    Integer.toString(deposit.getInitialBalance()), Integer.toString(deposit.getUpperBound())));
//        }
//    }

    public void setOutLog(String outLog) {
        this.outLog = outLog;
    }
}

