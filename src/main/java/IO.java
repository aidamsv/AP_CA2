/**
 * Created by aida on 11/17/15.
 */
import com.google.gson.Gson;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Scanner;


public class IO {


    public CoreData readCoreData() throws Exception{
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader (new FileReader("core.json"));
        return gson.fromJson(br, CoreData.class);
    }

    public int remove_commas(String s) {
        Scanner scan = new Scanner(s).useDelimiter(",");
        String removed_commas="";
        while (scan.hasNext())
            removed_commas = removed_commas.concat(scan.next());
        scan.close();
        return Integer.parseInt(removed_commas);
    }

    public TerminalData readTerminalData(String filaName) {
        TerminalData terminalData = new TerminalData();
        try {

            File fXmlFile = new File(filaName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            // System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            Element eElement = (Element) doc.getElementsByTagName("terminal").item(0);
            terminalData.setId(eElement.getAttribute("id"));
            terminalData.setType(eElement.getAttribute("type"));

            eElement = (Element) doc.getElementsByTagName("server").item(0);
            terminalData.setServerIP(eElement.getAttribute("ip"));
            terminalData.setServerPort(Integer.parseInt(eElement.getAttribute("port")));


            eElement = (Element) doc.getElementsByTagName("outLog").item(0);
            terminalData.setOutLogPath(eElement.getAttribute("path"));

            NodeList transactionList = doc.getElementsByTagName("transaction");

            for (int i=0; i<transactionList.getLength(); i++) {

                Node nNode = transactionList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    eElement = (Element) nNode;
                    terminalData.addTransaction(new Transaction(
                            eElement.getAttribute("id"),
                            eElement.getAttribute("type"),
                            this.remove_commas(eElement.getAttribute("amount")),
                            eElement.getAttribute("deposit")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terminalData;
    }
    public static void main(String[] args) throws Exception {
        IO io = new IO();
        String s=io.readCoreData().getDeposits().get(0).getInitialBalance();
        System.out.println(s);
        System.out.println(io.remove_commas(s));
        TerminalData terminalData = io.readTerminalData("terminal.xml");
        System.out.println(terminalData.getId());
        System.out.println(terminalData.getOutLogPath());
        System.out.println(terminalData.getServerIP());
        System.out.println(terminalData.getType());
        System.out.println(terminalData.getServerPort());
        System.out.println(terminalData.getTransactions().get(0).getType());
        System.out.println(terminalData.getTransactions().get(0).getDepositId());
        System.out.println(terminalData.getTransactions().get(0).getTransactionId());
        System.out.println(terminalData.getTransactions().get(0).getAmount());

    }
}
