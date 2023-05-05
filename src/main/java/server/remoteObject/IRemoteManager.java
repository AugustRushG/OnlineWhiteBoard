package server.remoteObject;

import gui.MyShape;
import gui.MyText;
import jdk.jshell.execution.Util;
import models.ChatMessage;
import models.WhiteboardManager;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface IRemoteManager extends IRemoteObserver {
    void createRoom(IRemoteManager manager, String name) throws IOException, NotBoundException;
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
}
