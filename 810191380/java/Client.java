/**
 * Created by aida on 11/17/15.
 */
// File Name GreetingServer.java

import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private TerminalData terminalData;
    private MySocket clientSocket;
    String terminalFileName;

    public Client(String terminalFileName) throws IOException {
        IO io = new IO();
        terminalData = io.readTerminalData(terminalFileName);
        System.out.println("Connecting to " + terminalData.getServerIP() + " on port " + terminalData.getServerPort());
        clientSocket = new MySocket(new Socket(terminalData.getServerIP(), terminalData.getServerPort()));
        System.out.println("Just connected to " + clientSocket.getSocket().getRemoteSocketAddress());
    }



    public void run() {
        try {
            List<Transaction> transactions = terminalData.getTransactions();
            List<String> results = new ArrayList<String>();
            for(int i=0; i<transactions.size(); i++) {
              //  System.out.println(i);
                Transaction transaction = transactions.get(i);
                clientSocket.sendData("TRNS,"+terminalData.getId()+","+terminalData.getType()+
                        ","+ transaction.getTransactionId() +","+transaction.getType()+
                        ","+transaction.getAmount()+","+transaction.getDepositId());
                String result=clientSocket.receiveData();
                results.add(result);
                System.out.println(result+" for transaction "+transaction);
            }
            clientSocket.sendData("END");
            clientSocket.getSocket().close();
            IO io = new IO();
            io.writeResponseData(terminalData.getId() ,terminalData.getTransactions(), results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Enter terminal.xml file name");
            Scanner sc = new Scanner(System.in);
            String terminalFileName = sc.next();
            Client client = new Client(terminalFileName);
            client.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
