import java.io.*;
import java.net.Socket;

/**
 * Created by aida on 11/18/15.
 */
public class MySocket {
    private Socket socket;
    MySocket (Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void sendData(String data) throws IOException {
        OutputStream outToServer = socket.getOutputStream();
        DataOutputStream out = new DataOutputStream(outToServer);
        out.writeUTF(data);
    }

    public String receiveData() throws IOException {
        InputStream inFromServer = socket.getInputStream();
        DataInputStream in = new DataInputStream(inFromServer);
        return in.readUTF();
    }
}
