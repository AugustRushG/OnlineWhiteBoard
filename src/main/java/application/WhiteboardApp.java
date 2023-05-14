package application;

import models.MyShape;
import models.MyText;
import gui.WhiteboardGUI;
import models.ChatMessage;
import server.remoteObject.IRemoteClient;
import server.remoteObject.IRemoteObserver;
import server.remoteObject.IRemoteServer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class WhiteboardApp{
    private final IRemoteClient remoteClient;
    private final IRemoteServer remoteServer;
    private final String username;
    public WhiteboardApp(IRemoteClient remoteClient, IRemoteServer remoteServer,String username){
        this.remoteClient = remoteClient;
        this.remoteServer = remoteServer;
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
        remoteServer.unRegisterClient(remoteClient,username, remoteClient.getRoomId());
    }

    public String getUsername(){
        return username;
    }
}
