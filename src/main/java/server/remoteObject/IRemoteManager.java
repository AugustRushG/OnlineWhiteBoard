package server.remoteObject;

import models.MyShape;
import models.MyText;
import models.ChatMessage;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;


public interface IRemoteManager extends IRemoteObserver {
    void sendMessage(String message,String name, int roomID) throws RemoteException;
    void sendDrawing(ArrayList<MyShape> shapes) throws RemoteException;
    void sendText(ArrayList<MyText> texts) throws RemoteException;
    ArrayList<ChatMessage> receiveMessage() throws RemoteException;
    void registerClient(IRemoteClient client, String username) throws IOException, NotBoundException;
    void unRegisterClient(IRemoteClient client, String username) throws IOException, NotBoundException;
    String getUsername() throws RemoteException;
    int getRoomID() throws RemoteException;
    ArrayList<String> getUsersInRoom() throws RemoteException;
    IRemoteClient getClient(String username) throws RemoteException;
    void registerObserver(IRemoteObserver observer) throws RemoteException;
    void removeObserver(IRemoteObserver observer) throws RemoteException;
    HashMap<String, IRemoteClient> getClientMap() throws RemoteException;
    boolean confirmClientJoin(String username) throws RemoteException;
    boolean checkUsernameExisted(String username) throws RemoteException;
    void kickUser(String username) throws IOException, NotBoundException;
    void setUsername(String username) throws RemoteException;
    void setRoomID(int roomID) throws RemoteException;
}
