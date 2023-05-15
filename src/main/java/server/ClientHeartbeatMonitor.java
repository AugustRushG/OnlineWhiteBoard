package server;

import server.remoteObject.IRemoteClient;
import server.remoteObject.IRemoteManager;
import server.remoteObject.RemoteServer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

public class ClientHeartbeatMonitor implements Runnable{
    private final Map<IRemoteClient, Long> clientHeartbeatMap;
    private final Map<String, IRemoteClient> clientHashMap;

    private final long maxHeartbeatInterval;
    private final RemoteServer remoteServer;

    public ClientHeartbeatMonitor(Map<IRemoteClient, Long> clientHeartbeatMap, Map<String, IRemoteClient> clientHashMap
            , long maxHeartbeatInterval, RemoteServer remoteServer) {
        this.clientHashMap = clientHashMap;
        this.clientHeartbeatMap =  clientHeartbeatMap;
        this.maxHeartbeatInterval = maxHeartbeatInterval;
        this.remoteServer = remoteServer;
    }
    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<IRemoteClient, Long> entry : clientHeartbeatMap.entrySet()) {
            IRemoteClient client = entry.getKey();
            Long lastHeartbeatTime = entry.getValue();
            if (currentTime - lastHeartbeatTime > maxHeartbeatInterval) {
                // Manager has not sent a heartbeat in a while, remove it from the system
                try {
                    remoteServer.unRegisterClient(client,client.getUsername(),client.getRoomId());
                    clientHeartbeatMap.remove(client);
                    clientHashMap.values().removeIf(m -> m.equals(client));
                    System.out.println("Client " + client.getUsername() + " has disconnected");
                } catch (NotBoundException | IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}


