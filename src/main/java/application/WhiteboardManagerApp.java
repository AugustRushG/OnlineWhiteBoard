package application;

import gui.MyShape;
import gui.MyText;
import gui.WhiteboardManagerGUI;
import models.ChatMessage;
import server.remoteObject.IRemoteManager;
import server.remoteObject.IRemoteObserver;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class WhiteboardManagerApp{
    private IRemoteManager remoteManager;
    public WhiteboardManagerApp(IRemoteManager remoteManager){
        this.remoteManager = remoteManager;
    }
    public void createWhiteboardManager() throws RemoteException {
        WhiteboardManagerGUI whiteboardManagerGUI = new WhiteboardManagerGUI(this);
    }
    public void sendMessage(String text) throws RemoteException {
        remoteManager.sendMessage(text,remoteManager.getUsername(), remoteManager.getRoomID());
    }
    public ArrayList<ChatMessage> getChatMessages() throws RemoteException {
        return remoteManager.receiveMessage();
    }
    public ArrayList<String> getUserInRoom() throws RemoteException{
        return remoteManager.getUsersInRoom();
    }
    public int getRoomId() throws RemoteException{
        return remoteManager.getRoomID();
    }
    public void registerObserver(IRemoteObserver remoteObserver) throws RemoteException{
        remoteManager.registerObserver(remoteObserver);
    }
    public void sendShape(ArrayList<MyShape> shapes) throws RemoteException{
        remoteManager.sendDrawing(shapes);
    }
    public void sendText(ArrayList<MyText> textS) throws RemoteException{
        remoteManager.sendText(textS);
    }
    public void closeRoom() throws RemoteException{
        remoteManager.notifyRoomClose();
    }
    public void kickUser(String username) throws RemoteException{
        remoteManager.kickUser(username);
    }

}