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
    private String outLogFile;
    String logs;
    //private CoreData coreData;
    Thread cmdHandler;

    public Server() throws Exception {
        IO io = new IO();
        CoreData coreData =  io.readCoreData();
        //System.out.println("here");
        cmdHandler = new CMDHandler(coreData);
        System.out.println(coreData.getPort());

        serverSocket = new ServerSocket(coreData.getPort()); //TODO bind on specific ip
        outLogFile = coreData.getOutLog();
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
        cmdHandler.start();
        while(true)
        {
            try
            {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                MySocket clinetSocket = new MySocket(serverSocket.accept());
                ClientResponder clientResponder = new ClientResponder(clinetSocket);
                clientResponder.start();
                //clientResponder.join();
            } catch(SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e) {
                e.printStackTrace();
                break;
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
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
                    //System.out.println(input);
                    //clientSocket.sendData("Thank you for connecting to " + clientSocket.getSocket().getLocalSocketAddress() + "\nGoodbye!");
                    String[] tokens = input.split(",");
                    if("TRNS".equals(tokens[0])) {
                        String terminalID = tokens[1];
                        String terminalType = tokens[2];
                        String log="";
                        try {
                            Transaction transaction = new Transaction(tokens[3],tokens[4],Integer.parseInt(tokens[5]),tokens[6]);
                            log="transaction "+transaction+" from terminal id = "+terminalID+" and type = "+terminalType;
                            String depositID=transaction.getDepositId();
                            getDeposit(depositID).runTransaction(transaction);
                            clientSocket.sendData("success");
                            log=log+" result success";
                            logs=logs+log;
                            System.out.println(log);
                            IO io = new IO();
                            io.appendLog(outLogFile, log);

                        } catch (Exception e) {
                            log = log+e.getMessage()+" because result fail";
                            logs = logs +log;
                            System.out.println(log);
                            IO io = new IO();
                            io.appendLog(outLogFile, log);
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

    public class CMDHandler extends Thread{

        private CoreData coreData;
        public CMDHandler(CoreData coreData) {
            this.coreData = coreData;
            System.out.printf("build");
        }
        public void run() {
            Scanner sc = new Scanner(System.in);
            while(true) {
                if(sc.hasNext()) {
                    String cmd=sc.next();
                    if(cmd.equals("sync")) {
                        coreData.clearDeposits();
                        for (int i = 0; i < depositList.size(); i++) {
                            Deposit deposit = depositList.get(i);
                            coreData.addDeposit(deposit.getCustomer(),
                                    deposit.getId(), deposit.getInitialBalance(), deposit.getUpperBound());
                        }

                        try {
                            IO io=new IO();
                            io.writeCoreData(coreData);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.println("update done");
                    }
                    else {
                        System.out.println("Invalid cmd");
                    }
                }
            }
        }
    }


    public static void main(String [] args)
    {
        try
        {
            Thread serverThread = new Server();
            serverThread.start();
           // serverThread.join();

        } catch(IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}