/**
 * Created by aida on 11/17/15.
 */
import com.google.gson.Gson;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class IO {


    public CoreData readCoreData() throws Exception{
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader (new FileReader("core.json"));
        return gson.fromJson(br, CoreData.class);
    }

    public void writeCoreData(CoreData coreData) throws FileNotFoundException {
        Gson gson = new Gson();
        PrintWriter printWriter = new PrintWriter ("core.json");
        printWriter.println (gson.toJson(coreData));
        printWriter.close();
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

    public void writeResponseData(String terminalID, List<Transaction> transactions, List<String> results) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("transaction");
            doc.appendChild(rootElement);

            // transaction elements
            Element transactionElement = doc.createElement("transaction");
            rootElement.appendChild(transactionElement);


            for(int i=0; i<transactions.size(); i++) {
                Transaction transaction = transactions.get(i);

                Element id = doc.createElement("id");
                id.appendChild(doc.createTextNode(transaction.getTransactionId()));
                transactionElement.appendChild(id);

                Element type = doc.createElement("type");
                type.appendChild(doc.createTextNode(transaction.getType()));
                transactionElement.appendChild(type);

                Element amount = doc.createElement("amount");
                amount.appendChild(doc.createTextNode(Integer.toString(transaction.getAmount())));
                transactionElement.appendChild(amount);

                Element deposit = doc.createElement("deposit");
                deposit.appendChild(doc.createTextNode(transaction.getDepositId()));
                transactionElement.appendChild(deposit);

                Element result = doc.createElement("result");
                result.appendChild(doc.createTextNode(results.get(i)));
                transactionElement.appendChild(result);
            }


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(terminalID+"_response.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public void appendLog(String logFile, String data) throws IOException {
        File file =new File(logFile);

        if(!file.exists()){
            file.createNewFile();
        }

        //true = append file
        FileWriter fileWritter = new FileWriter(file.getName(),true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(data);
        bufferWritter.write("\n\n");
        bufferWritter.close();

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
