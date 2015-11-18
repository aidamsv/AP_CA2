import java.util.ArrayList;
import java.util.List;

/**
 * Created by aida on 11/18/15.
 */

public class TerminalData {
    private String id;
    private String type;
    private String serverIP;
    private int serverPort;
    private String outLogPath;
    private List<Transaction> transactions;

    public TerminalData() {transactions = new ArrayList<Transaction>();}

    public String getId() { return id; }

    public String getType() { return type; }

    public String getServerIP() { return serverIP; }

    public int getServerPort() { return serverPort; }

    public String getOutLogPath() { return outLogPath; }

    public List<Transaction> getTransactions() { return transactions; }

    public void setOutLogPath(String outLogPath) { this.outLogPath = outLogPath; }

    public void setId(String id) { this.id = id; }

    public void setType(String type) { this.type = type; }

    public void setServerIP(String serverIP) { this.serverIP = serverIP; }

    public void setServerPort(int serverPort) { this.serverPort = serverPort; }

    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public void addTransaction(Transaction transaction) { transactions.add(transaction); }
}
