package client;

import server.remoteObject.IRemoteDraw;
import server.remoteObject.IRemoteMessage;
import server.remoteObject.IRemoteRoom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;

public class WhiteboardManager extends WhiteboardClient{

    private IRemoteRoom remoteRoom;
    public WhiteboardManager(String username,String hostname, int port, IRemoteDraw remoteDraw, IRemoteMessage remoteMessage,
                             IRemoteRoom remoteRoom) throws IOException, NotBoundException {
        super(username,hostname, port, remoteDraw, remoteMessage);
        this.remoteRoom = remoteRoom;
    }


}
