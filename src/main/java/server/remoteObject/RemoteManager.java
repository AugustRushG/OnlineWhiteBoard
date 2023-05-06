package server.remoteObject;

import gui.MyShape;
import gui.MyText;
import models.ChatMessage;
import models.WhiteboardClient;
import server.Server;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemoteManager extends UnicastRemoteObject implements IRemoteManager, IRemoteObserver {
    private Server server;
    private volatile HashMap<String,IRemoteClient> clientMap;
    private String username;
    private int roomID;
    private IRemoteObserver observer;

    public void setServer(Server server) {
        this.server = server;
    }
    public RemoteManager() throws RemoteException {
        clientMap = new HashMap<>();
    }

    @Override
    public void createRoom(IRemoteManager manager, String username) throws IOException, NotBoundException {
        this.roomID = server.createRoomID();
        this.username = username;
        server.createRoom(this,roomID);
        System.out.println("creating room manger id is "+username+" room id is "+roomID);
    }

    @Override
    public void sendMessage(String message, String username, int roomID) throws RemoteException {
        System.out.println("getting message from "+username+" message is "+message);
        server.updateChatBoard(username,message,roomID);

//        notifyObserver(new ChatMessage(username,message));
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
        System.out.println("latest is "+ server.loadLatestChatBoard(roomID).toString());
        return server.loadLatestChatBoard(roomID);
    }

    @Override
    public synchronized void registerClient(IRemoteClient client, String username) throws IOException, NotBoundException {
        client.setUsername(username);
        client.setRoomID(roomID);
        clientMap.put(username,client);
        System.out.println("Client " + client.getUsername() + " registered on manager.");
        WhiteboardClient whiteboardClient = new WhiteboardClient(username);
        server.addClientToRoom(whiteboardClient,roomID);
        System.out.println("Client " + client.getUsername() + " added into room ."+ roomID);
    }

    @Override
    public void unRegisterClient(IRemoteClient client, String username) throws IOException, NotBoundException {
        clientMap.remove(username);
        server.removeClientInRoom(username,roomID);
        System.out.println("Client " + client.getUsername() + " removed on manager.");
    }

    @Override
    public String getUsername() throws RemoteException{
        return username;
    }

    @Override
    public int getRoomID() throws RemoteException {
        return roomID;
    }

    @Override
    public ArrayList<String> getUsersInRoom() throws RemoteException {
        return server.getUserInRoom(roomID);
    }

    @Override
    public IRemoteClient getClient(String username) throws RemoteException {
        System.out.println("getting client "+clientMap.get(username).getRoomId());
        return clientMap.get(username);
    }

    @Override
    public void registerObserver(IRemoteObserver observer) throws RemoteException {
        this.observer = observer;
    }

    @Override
    public void removeObserver(IRemoteObserver observer) throws RemoteException {

    }

    @Override
    public HashMap<String, IRemoteClient> getClientMap() throws RemoteException{
        System.out.println("manager  "+username+"clients are "+clientMap);
        return clientMap;
    }

    @Override
    public boolean confirmClientJoin(String username) throws RemoteException {
        return notifyJoinRequest(username);
    }

    @Override
    public boolean checkUsernameExisted(String username) throws RemoteException {
        return server.checkUsernameExisted(username,roomID);
    }

    @Override
    public void kickUser(String username) throws IOException, NotBoundException {
        System.out.println("user "+username+" been kicked");
        IRemoteClient remoteClient = clientMap.get(username);
        remoteClient.notifyUserBeenKicker();
        clientMap.remove(username);
        server.removeClientInRoom(username,roomID);
        System.out.println("Client " + username + " being kicked by manager.");
    }


    @Override
    public void notifyNewMessage(ChatMessage message) throws RemoteException {
        System.out.println("notifying message to manager" + username);
        observer.notifyNewMessage(message);
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            // Do something with the clientName and client
            client.notifyNewMessage(message);
        }
    }

    @Override
    public void notifyUserChange(ArrayList<String> users) throws RemoteException {
        System.out.println("manager user are " + users);
        observer.notifyUserChange(users);
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            // Do something with the clientName and client
            client.notifyUserChange(users);
        }
    }

    @Override
    public void notifyShapeChange(ArrayList<MyShape> shapes) throws RemoteException {
        observer.notifyShapeChange(shapes);
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            // Do something with the clientName and client
            client.notifyShapeChange(shapes);
        }
    }

    @Override
    public void notifyTextsChange(ArrayList<MyText> texts) throws RemoteException {
        observer.notifyTextsChange(texts);
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            // Do something with the clientName and client
            client.notifyTextsChange(texts);
        }
    }

    @Override
    public boolean notifyJoinRequest(String username) throws RemoteException {
        return observer.notifyJoinRequest(username);
    }

    @Override
    public void notifyRoomClose() throws RemoteException {
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            // Do something with the clientName and client
            client.notifyRoomClose();
        }
        server.closeRoom(roomID);
    }

    @Override
    public void notifyUserBeenKicker() throws RemoteException {

    }

    @Override
    public void notifyServerClosing() throws RemoteException {
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            // Do something with the clientName and client
            client.notifyServerClosing();
        }
        observer.notifyServerClosing();
    }
}
