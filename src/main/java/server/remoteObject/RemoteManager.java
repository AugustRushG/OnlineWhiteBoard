package server.remoteObject;

import models.ChatMessage;
import models.WhiteboardManager;
import server.Server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RemoteManager extends UnicastRemoteObject implements IRemoteManager {
    private Server server;
    private List<IRemoteClient> clients;
    private String username;
    private int roomID;

    public void setServer(Server server) {
        this.server = server;
    }
    public RemoteManager() throws RemoteException {
    }


    @Override
    public void createRoom(IRemoteManager manager, String username) throws IOException, NotBoundException {
        this.roomID = server.createRoomID();
        this.username = username;
        WhiteboardManager manger = new WhiteboardManager(username,roomID);
        server.createRoom(manger,roomID);
        System.out.println("creating room manger id is "+username+" room id is "+roomID);
    }

    @Override
    public void sendMessage(String message, String username, int roomID) throws RemoteException {
        server.updateChatBoard(username,message,roomID);
    }

    @Override
    public ArrayList<ChatMessage> receiveMessage(int roomID) throws RemoteException {
        System.out.println("lastest is "+ server.loadLatestChatBoard(roomID).toString());
        return server.loadLatestChatBoard(roomID);
    }

    @Override
    public void registerClient(IRemoteClient client) throws RemoteException {
        clients.add(client);
        System.out.println("Client " + client.getUsername() + " registered.");
    }

    @Override
    public String getUsername() throws RemoteException{
        return username;
    }

    @Override
    public int getRoomID() throws RemoteException {
        return roomID;
    }
}
