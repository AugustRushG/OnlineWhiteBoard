package gui;

import constant.PopUpDialog;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class WhiteboardGUIUpdater extends UnicastRemoteObject implements IWhiteboardGUIUpdater{

    private WhiteboardGUI whiteboardGUI;

    protected WhiteboardGUIUpdater(WhiteboardGUI whiteboardGUI) throws RemoteException {
        this.whiteboardGUI = whiteboardGUI;
    }

    @Override
    public void clearCanvas() throws RemoteException {

    }

    @Override
    public void updateNewMessage(String text) throws RemoteException {
        whiteboardGUI.updateChatArea(text);
    }

    @Override
    public void updateNewUserList(ArrayList<String> userList) throws RemoteException {
        whiteboardGUI.updateUserList(userList);
    }

    @Override
    public void updateShapes(ArrayList<MyShape> shapes) throws RemoteException {
        whiteboardGUI.updateShapeList(shapes);
    }

    @Override
    public void updateTexts(ArrayList<MyText> texts) throws RemoteException {
        whiteboardGUI.updateTextList(texts);
    }

    @Override
    public boolean popJoinDialog(String username) throws RemoteException {
        return false;
    }

    @Override
    public void notifyRoomClose() throws RemoteException {
        PopUpDialog.showErrorMessageDialog("The manager has closed the room, closing the application now");
    }

    @Override
    public void notifyUserBeenKicked() throws RemoteException {
        PopUpDialog.showErrorMessageDialog("You have been kicked out by the manager, closing the application now");
    }

    @Override
    public void notifyServerClosing() throws RemoteException {
        PopUpDialog.showErrorMessageDialog("Server is closing, all room disconnecting, closing the application now");
    }

}
