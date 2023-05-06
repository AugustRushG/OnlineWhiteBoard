package server.remoteObject;

import gui.MyShape;
import gui.MyText;
import models.ChatMessage;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteObserver extends Remote {
    void notifyNewMessage(ChatMessage message) throws RemoteException;
    void notifyUserChange(ArrayList<String> users) throws RemoteException;
    void notifyShapeChange(ArrayList<MyShape> shapes) throws RemoteException;
    void notifyTextsChange(ArrayList<MyText> texts) throws RemoteException;
    boolean notifyJoinRequest(String username) throws RemoteException;
    void notifyRoomClose() throws RemoteException;
    void notifyUserBeenKicker() throws RemoteException;
    void notifyServerClosing() throws RemoteException;
}
