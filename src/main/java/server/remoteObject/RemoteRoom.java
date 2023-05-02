package server.remoteObject;

import client.WhiteboardManager;
import server.Server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class RemoteRoom extends UnicastRemoteObject implements IRemoteRoom{
    private Server server;

    public RemoteRoom() throws RemoteException {
    }

    protected RemoteRoom(int port) throws RemoteException {
        super(port);
    }

    protected RemoteRoom(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void createRoom(WhiteboardManager whiteboardManager) {

    }
}
