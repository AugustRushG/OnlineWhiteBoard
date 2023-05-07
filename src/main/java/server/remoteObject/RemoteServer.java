package server.remoteObject;

import server.Server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RemoteServer extends UnicastRemoteObject implements IRemoteServer {
    public Server server;
    private volatile HashMap<Integer,IRemoteManager> managerHashMap;

    public RemoteServer() throws RemoteException {
        managerHashMap = new HashMap<>();
    }

    public void setServer(Server server) {
        this.server = server;
    }
    @Override
    public synchronized IRemoteClient registerClientToRoom(int roomID, String username) throws IOException, NotBoundException {
        IRemoteManager remoteManager = server.getManager(roomID);
        RemoteClient remoteClient = new RemoteClient();
        remoteClient.setUsername(username);
        remoteClient.setServer(server);
        remoteClient.setRoomID(roomID);
        remoteManager.registerClient(remoteClient,username);
        System.out.println("client "+username+"registering in room "+roomID);
        return remoteClient;
    }

    @Override
    public boolean checkUsernameExisted(String username, int roomID) throws RemoteException {
        return server.checkUsernameExisted(username,roomID);
    }

    @Override
    public boolean confirmClientJoin(String username, int roomID) throws RemoteException {
        return server.getManager(roomID).notifyJoinRequest(username);
    }

    @Override
    public void unRegisterClient(IRemoteClient client, String username, int roomID) throws IOException, NotBoundException {
        server.getManager(roomID).unRegisterClient(client,username);
        System.out.println("Client " + client.getUsername() + " removed on manager.");
    }

    @Override
    public synchronized IRemoteManager registerManager(String username) throws NotBoundException, IOException {
        int roomID = server.createRoomID();
        RemoteManager remoteManager = new RemoteManager();
        remoteManager.setUsername(username);
        remoteManager.setRoomID(roomID);
        remoteManager.setServer(server);
        managerHashMap.put(roomID,remoteManager);
        server.createRoom(remoteManager, roomID);
        System.out.println("creating room manger id is "+username+" room id is "+roomID);
        return remoteManager;
    }

    public void notifyRoomClosing(int roomID) throws RemoteException{
        managerHashMap.get(roomID).notifyRoomClose();
    }
}
