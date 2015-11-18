/**
 * Created by aida on 11/17/15.
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server extends Thread
{

    private ServerSocket serverSocket;
    private List<Deposit> depositList = new ArrayList<Deposit>();
    private PrintWriter outlog;

    public Server() throws Exception {
        IO io = new IO();
        CoreData coreData =  io.readCoreData();
        System.out.println(coreData.getPort());

        serverSocket = new ServerSocket(coreData.getPort()); //TODO bind on specific ip
        outlog = new PrintWriter(coreData.getOutLog(), "UTF-8");
        List<CoreData.Deposit> coreDataDeposits = coreData.getDeposits();
        for(int i=0; i<coreDataDeposits.size(); i++) {
            CoreData.Deposit d=coreDataDeposits.get(i);
            depositList.add(new Deposit(d.getCustomer(),
                    d.getId(),
                    io.remove_commas(d.getInitialBalance()),
                    io.remove_commas(d.getUpperBound())));
        }
        //serverSocket.setSoTimeout(10000);
    }

    public void run() {
        while(true)
        {
            try
            {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                MySocket clinetSocket = new MySocket(serverSocket.accept());
                ClientResponder clientResponder = new ClientResponder(clinetSocket);
                clientResponder.start();
            } catch(SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private Deposit getDeposit(String depositID) throws Exception {
        for (int i=0; i<depositList.size(); i++) {
            if(depositList.get(i).getId().equals(depositID))
                return depositList.get(i);
        }
        throw new Exception("There is no deposit with id "+depositID);
    }

    public class ClientResponder extends Thread{
        private MySocket clientSocket;
        public ClientResponder(MySocket  clientSocket) {
            this.clientSocket = clientSocket;
        }
        public void run() {
            try {
                System.out.println("Just connected to " + clientSocket.getSocket().getRemoteSocketAddress());
                String input = clientSocket.receiveData();
                while (! "END".equals(input)) {
                    System.out.println(input);
                    //clientSocket.sendData("Thank you for connecting to " + clientSocket.getSocket().getLocalSocketAddress() + "\nGoodbye!");
                    String[] tokens = input.split(",");
                    //clientSocket.sendData("TRNS,"+terminalData.getId()+","+terminalData.getType()+","+transaction);
                    System.out.printf(tokens[0]);
                    if("TRNS".equals(tokens[0])) {
                        String terminalID = tokens[1];
                        String terminalType = tokens[2];
                        try {
                            Transaction transaction = new Transaction(tokens[3],tokens[4],Integer.parseInt(tokens[5]),tokens[6]);
                            String depositID=transaction.getDepositId();
                            getDeposit(depositID).runTransaction(transaction);
                            clientSocket.sendData("success");
                            System.out.printf("success");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            clientSocket.sendData("fail");
                        }

                    }
                    input = clientSocket.receiveData();
                }
                clientSocket.getSocket().close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void main(String [] args)
    {
        try
        {
            Thread t = new Server();
            t.start();
        } catch(IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}