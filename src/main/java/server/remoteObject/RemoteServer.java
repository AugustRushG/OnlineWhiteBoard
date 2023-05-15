package server.remoteObject;

import server.ClientHeartbeatMonitor;
import server.HeartbeatMonitor;
import server.Server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RemoteServer extends UnicastRemoteObject implements IRemoteServer {
    public Server server;
    private final Map<Integer,IRemoteManager> managerHashMap;
    private final Map<IRemoteManager, Long> heartbeatMap;
    private final Map<IRemoteClient, Long> clientHeartbeatMap;

    public RemoteServer(ScheduledExecutorService executorService) throws RemoteException {
        managerHashMap = new ConcurrentHashMap<>();
        heartbeatMap = new ConcurrentHashMap<>();
        clientHeartbeatMap = new ConcurrentHashMap<>();
        long maxHeartbeatInterval = 12 * 1000L; // 11 seconds
        executorService.scheduleAtFixedRate(new HeartbeatMonitor(heartbeatMap,managerHashMap,maxHeartbeatInterval,this),
                maxHeartbeatInterval, maxHeartbeatInterval, TimeUnit.MILLISECONDS);
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
    public synchronized IRemoteManager registerManager(String username) throws IOException {
        int roomID = server.createRoomID();
        RemoteManager remoteManager = new RemoteManager();
        remoteManager.setUsername(username);
        remoteManager.setRoomID(roomID);
        remoteManager.setServer(server);
        managerHashMap.put(roomID,remoteManager);
        server.createRoom(remoteManager, roomID);
        heartbeatMap.put(remoteManager,System.currentTimeMillis());
        System.out.println("creating room manger id is "+username+" room id is "+roomID);
        return remoteManager;
    }

    @Override
    public void unRegisterManager(Integer roomID) throws RemoteException {
        managerHashMap.get(roomID).notifyRoomClose();
    }

    @Override
    public synchronized void heartbeat(Integer roomID) throws RemoteException {
        IRemoteManager manager = managerHashMap.get(roomID);
        Long thisHeartbeatTime = System.currentTimeMillis();
        heartbeatMap.putIfAbsent(manager, thisHeartbeatTime);
        Long lastHeartbeatTime = heartbeatMap.get(manager);
        heartbeatMap.put(manager,thisHeartbeatTime);
//        System.out.println("received heartbeat from " + manager.getUsername()+ " time is "+lastHeartbeatTime);

    }

    @Override
    public void clientHeartbeat(String username, Integer roomID) throws RemoteException {
        IRemoteClient client = managerHashMap.get(roomID).getClient(username);
        Long thisHeartbeatTime = System.currentTimeMillis();
        clientHeartbeatMap.putIfAbsent(client,thisHeartbeatTime);
        Long lastHeartbeatTime = clientHeartbeatMap.get(client);
        clientHeartbeatMap.put(client,thisHeartbeatTime);
//        System.out.println("received heartbeat from "+ client.getUsername());
    }


    public void notifyRoomClosing(int roomID) throws RemoteException{
        managerHashMap.get(roomID).notifyRoomClose();
    }



}
