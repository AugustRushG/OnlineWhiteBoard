package server.remoteObject;

import gui.IWhiteboardGUIUpdater;
import models.ChatMessage;
import models.MyShape;
import models.MyText;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class RemoteObserver implements IRemoteObserver, Serializable {

    private final IWhiteboardGUIUpdater whiteboardGUIUpdater;
    public RemoteObserver(IWhiteboardGUIUpdater whiteboardGUIUpdater) {
        this.whiteboardGUIUpdater = whiteboardGUIUpdater;
    }
    @Override
    public void notifyNewMessage(ChatMessage message) throws RemoteException {
        String text = message.getTimeStamp()+" "+message.getSender() + ": " + message.getContent() + "\n";
        whiteboardGUIUpdater.updateNewMessage(text);
        System.out.println("getting new texts");
    }
    @Override
    public void notifyUserChange(ArrayList<String> users) throws RemoteException {
        whiteboardGUIUpdater.updateNewUserList(users);
    }
    @Override
    public void notifyShapeChange(ArrayList<MyShape> shapes) throws RemoteException {
        whiteboardGUIUpdater.updateShapes(shapes);
    }
    @Override
    public void notifyTextsChange(ArrayList<MyText> texts) throws RemoteException {
        whiteboardGUIUpdater.updateTexts(texts);
    }
    @Override
    public boolean notifyJoinRequest(String username) throws RemoteException {
        return whiteboardGUIUpdater.popJoinDialog(username);
    }
    @Override
    public void notifyRoomClose() throws RemoteException {
        whiteboardGUIUpdater.notifyRoomClose();
    }

    @Override
    public void notifyUserBeenKicker() throws IOException, NotBoundException {
        whiteboardGUIUpdater.notifyUserBeenKicked();
    }

    @Override
    public void notifyServerClosing() throws RemoteException {
        whiteboardGUIUpdater.notifyServerClosing();
    }
}