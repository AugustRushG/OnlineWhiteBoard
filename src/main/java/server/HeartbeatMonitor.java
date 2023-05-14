package server;

import server.remoteObject.IRemoteManager;
import server.remoteObject.RemoteServer;

import java.rmi.RemoteException;
import java.util.Map;

public class HeartbeatMonitor implements Runnable{
    private final Map<IRemoteManager, Long> heartbeatMap;
    private final Map<Integer, IRemoteManager> managerHashMap;
    private final long maxHeartbeatInterval;
    private final RemoteServer remoteServer;

    public HeartbeatMonitor(Map<IRemoteManager, Long> heartbeatMap, Map<Integer, IRemoteManager> managerHashMap
            , long maxHeartbeatInterval, RemoteServer remoteServer) {
        this.heartbeatMap = heartbeatMap;
        this.managerHashMap = managerHashMap;
        this.maxHeartbeatInterval = maxHeartbeatInterval;
        this.remoteServer = remoteServer;
    }
    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<IRemoteManager, Long> entry : heartbeatMap.entrySet()) {
            IRemoteManager manager = entry.getKey();
            Long lastHeartbeatTime = entry.getValue();
            if (currentTime - lastHeartbeatTime > maxHeartbeatInterval) {
                // Manager has not sent a heartbeat in a while, remove it from the system
                try {
                    remoteServer.unRegisterManager(manager.getRoomID());
                    heartbeatMap.remove(manager);
                    managerHashMap.values().removeIf(m -> m.equals(manager));
                    System.out.println("Manager " + manager.getUsername() + " has disconnected");
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
