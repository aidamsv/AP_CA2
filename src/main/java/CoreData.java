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
}
