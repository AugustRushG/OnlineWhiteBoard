package application;

import gui.MyShape;
import gui.MyText;
import gui.WhiteboardGUI;
import models.ChatMessage;
import server.remoteObject.IRemoteClient;
import server.remoteObject.IRemoteManager;
import server.remoteObject.IRemoteObserver;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class WhiteboardApp{
    private IRemoteClient remoteClient;
    private IRemoteManager remoteManager;
    private String username;
    public WhiteboardApp(IRemoteClient remoteClient, IRemoteManager remoteManager,String username){
        this.remoteClient = remoteClient;
        this.remoteManager = remoteManager;
        this.username = username;
    }
    public void createWhiteboard() throws RemoteException {
        WhiteboardGUI whiteboardGUI = new WhiteboardGUI(this);
    }
    public void sendMessage(String text) throws RemoteException {
        remoteClient.sendMessage(text);
    }
    public ArrayList<ChatMessage> getChatMessages() throws RemoteException {
        return remoteClient.receiveMessage();
    }
    public ArrayList<String> getUserInRoom() throws RemoteException{
        return remoteClient.getUserInRoom();
    }
    public int getRoomId() throws RemoteException{
        return remoteClient.getRoomId();
    }
    public void registerObserver(IRemoteObserver remoteObserver) throws RemoteException{
        remoteClient.registerObserver(remoteObserver);
    }
    public void sendShape(ArrayList<MyShape> shapes) throws RemoteException{
        remoteClient.sendDrawing(shapes);
    }
    public void sendText(ArrayList<MyText> texts) throws RemoteException{
        remoteClient.sendText(texts);
    }
    public ArrayList<MyShape> getAllShapes() throws RemoteException{
        return remoteClient.getAllShapes();
    }
    public ArrayList<MyText> getAllTexts() throws RemoteException{
        return remoteClient.getAllTexts();
    }

    public void unRegisterClient() throws IOException, NotBoundException {
        remoteManager.unRegisterClient(remoteClient,username);
    }
}
