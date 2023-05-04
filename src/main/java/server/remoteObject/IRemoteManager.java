package server.remoteObject;

import models.ChatMessage;
import models.WhiteboardManager;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteManager extends Remote {
    void createRoom(IRemoteManager manager, String name) throws IOException, NotBoundException;
    void sendMessage(String message,String name, int roomID) throws RemoteException;
    ArrayList<ChatMessage> receiveMessage(int roomID) throws RemoteException;

    void registerClient(IRemoteClient client) throws RemoteException;

    String getUsername() throws RemoteException;
    int getRoomID() throws RemoteException;
}
