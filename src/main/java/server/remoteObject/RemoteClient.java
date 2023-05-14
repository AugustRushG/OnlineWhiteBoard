package server.remoteObject;

import models.MyShape;
import models.MyText;
import models.ChatMessage;
import server.Server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RemoteClient extends UnicastRemoteObject implements IRemoteClient{
    private Server server;
    private String username;
    private int roomID;
    private IRemoteObserver remoteObserver;

    public RemoteClient()throws RemoteException{

    }
    public void setServer(Server server) {
        this.server = server;
    }
    @Override
    public String getUsername() throws RemoteException {
        // return the username associated with this client
        return this.username;
    }
    @Override
    public void sendMessage(String text) throws RemoteException {
        System.out.println("client "+username+" send message "+text);
        server.updateChatBoard(username,text,roomID);
    }

    @Override
    public void sendDrawing(ArrayList<MyShape> shapes) throws RemoteException {
        System.out.println("getting drawing from "+username);
        server.updateWhiteBoardShape(shapes, roomID);

    }

    @Override
    public void sendText(ArrayList<MyText> texts) throws RemoteException {
        System.out.println("getting text from " + username);
        server.updateWhiteboardText(texts,roomID);
    }

    @Override
    public ArrayList<ChatMessage> receiveMessage() throws RemoteException {
        return server.loadLatestChatBoard(roomID);
    }
    @Override
    public ArrayList<String> getUserInRoom() throws RemoteException {
        System.out.println(server.getUserInRoom(roomID));
        return server.getUserInRoom(roomID);
    }
    @Override
    public int getRoomId() throws RemoteException {
        return roomID;
    }
    @Override
    public void setRoomID(int roomID) throws RemoteException{
        this.roomID = roomID;
    }
    @Override
    public void setUsername(String username){
        this.username = username;
    }

    @Override
    public void registerObserver(IRemoteObserver observer) throws RemoteException {
        this.remoteObserver = observer;
    }

    @Override
    public void removeObserver(IRemoteObserver observer) throws RemoteException {
        this.remoteObserver = null;
    }

    @Override
    public ArrayList<MyShape> getAllShapes() throws RemoteException {
        return server.getRoomShapes(roomID);
    }

    @Override
    public ArrayList<MyText> getAllTexts() throws RemoteException {
        return server.getRoomTexts(roomID);
    }

    @Override
    public void notifyNewMessage(ChatMessage message) throws RemoteException {
        System.out.println("notifying message to client "+username);
        remoteObserver.notifyNewMessage(message);
    }

    @Override
    public void notifyUserChange(ArrayList<String> users) throws RemoteException {
        System.out.println("notifying client new user change " + users);
        if (remoteObserver == null){
        }
        else {
            remoteObserver.notifyUserChange(users);
        }

    }

    @Override
    public void notifyShapeChange(ArrayList<MyShape> shapes) throws RemoteException {
        remoteObserver.notifyShapeChange(shapes);
    }

    @Override
    public void notifyTextsChange(ArrayList<MyText> texts) throws RemoteException {
        remoteObserver.notifyTextsChange(texts);
    }

    @Override
    public boolean notifyJoinRequest(String username) throws RemoteException {
        return false;
    }

    @Override
    public void notifyRoomClose() throws RemoteException {
        remoteObserver.notifyRoomClose();
    }

    @Override
    public void notifyUserBeenKicker() throws IOException, NotBoundException {
        remoteObserver.notifyUserBeenKicker();
    }

    @Override
    public void notifyServerClosing() throws RemoteException {
        remoteObserver.notifyServerClosing();
    }
}
