package server.remoteObject;

import models.MyShape;
import models.MyText;
import models.ChatMessage;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteClient extends IRemoteObserver {
    String getUsername() throws RemoteException;
    void sendMessage(String text) throws RemoteException;
    void sendDrawing(ArrayList<MyShape> shapes) throws RemoteException;
    void sendText(ArrayList<MyText> texts) throws RemoteException;
    ArrayList<ChatMessage> receiveMessage() throws RemoteException;
    ArrayList<String> getUserInRoom() throws RemoteException;
    int getRoomId() throws RemoteException;
    void setRoomID(int roomID) throws RemoteException;
    void setUsername(String username) throws RemoteException;
    void registerObserver(IRemoteObserver observer) throws RemoteException;
    void removeObserver(IRemoteObserver observer) throws RemoteException;
    ArrayList<MyShape> getAllShapes() throws RemoteException;
    ArrayList<MyText> getAllTexts() throws RemoteException;

}
