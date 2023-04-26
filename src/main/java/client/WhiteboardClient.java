package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class WhiteboardClient {

    private String username;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public WhiteboardClient(String hostname, int port) throws IOException {
        this.socket = new Socket(hostname,port);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }

}
