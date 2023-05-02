package client;

import constant.RegistryConstant;
import server.remoteObject.IRemoteDraw;
import server.remoteObject.IRemoteMessage;
import server.remoteObject.IRemoteRoom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteboardClient {

    private String username;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private IRemoteDraw remoteDraw;
    private IRemoteMessage remoteMessage;

    public WhiteboardClient(String username, String hostname, int port, IRemoteDraw remoteDraw,
                            IRemoteMessage remoteMessage) throws IOException, NotBoundException {
        this.username = username;
        this.socket = new Socket(hostname,port);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        this.remoteDraw = remoteDraw;
        this.remoteMessage = remoteMessage;
    }

}
