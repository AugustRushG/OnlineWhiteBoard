package gui;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IWhiteboardGUIUpdater extends Remote {
    void clearCanvas() throws RemoteException;
    void updateNewMessage(String text) throws RemoteException;
    void updateNewUserList(ArrayList<String> userList) throws RemoteException;
    void updateShapes(ArrayList<MyShape> shapes) throws RemoteException;
    void updateTexts(ArrayList<MyText> texts) throws RemoteException;
    boolean popJoinDialog(String username) throws RemoteException;
    void notifyRoomClose() throws RemoteException;
    void notifyUserBeenKicked() throws RemoteException;
    void notifyServerClosing() throws RemoteException;
}
