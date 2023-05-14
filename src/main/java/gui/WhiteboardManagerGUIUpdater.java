package gui;

import constant.PopUpDialog;
import models.MyShape;
import models.MyText;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class WhiteboardManagerGUIUpdater extends UnicastRemoteObject implements IWhiteboardGUIUpdater{

    private final WhiteboardManagerGUI whiteboardManagerGUI;

    protected WhiteboardManagerGUIUpdater(WhiteboardManagerGUI whiteboardManagerGUI) throws RemoteException {
        super();
        this.whiteboardManagerGUI = whiteboardManagerGUI;
    }

    @Override
    public void clearCanvas() {

    }

    @Override
    public void updateNewMessage(String text) {
        whiteboardManagerGUI.updateChatArea(text);
    }

    @Override
    public void updateNewUserList(ArrayList<String> userList) throws RemoteException {
        System.out.println("manager updating its user list" + userList);
        whiteboardManagerGUI.updateUserList(userList);
    }

    @Override
    public void updateShapes(ArrayList<MyShape> shapes) throws RemoteException {
        whiteboardManagerGUI.updateShapeList(shapes);
    }

    @Override
    public void updateTexts(ArrayList<MyText> texts) throws RemoteException {
        whiteboardManagerGUI.updateTextList(texts);
    }

    @Override
    public boolean popJoinDialog(String username) throws RemoteException {
        return whiteboardManagerGUI.popJoinDialog(username);
    }

    @Override
    public void notifyRoomClose() throws RemoteException {

    }

    @Override
    public void notifyUserBeenKicked() throws RemoteException {

    }

    @Override
    public void notifyServerClosing() throws RemoteException {
        PopUpDialog.showErrorMessageDialog("Server has been closed, all room has been closed, closing application now",whiteboardManagerGUI.getFrame());
    }
}
