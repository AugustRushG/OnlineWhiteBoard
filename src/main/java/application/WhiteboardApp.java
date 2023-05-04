package application;

import gui.WhiteboardGUI;
import models.ChatMessage;
import models.WhiteboardClient;
import server.remoteObject.IRemoteManager;
import server.remoteObject.RemoteManager;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class WhiteboardApp {

    private IRemoteManager remoteManager;

    public WhiteboardApp(IRemoteManager remoteManager){
        this.remoteManager = remoteManager;

    }
    public void createWhiteboard(){
        WhiteboardGUI whiteboardGUI = new WhiteboardGUI(this);
    }

    public void sendMessage(String text) throws RemoteException {
        remoteManager.sendMessage(text,remoteManager.getUsername(), remoteManager.getRoomID());
    }

    public ArrayList<ChatMessage> getChatMessages() throws RemoteException {
        return remoteManager.receiveMessage(remoteManager.getRoomID());
    }

}