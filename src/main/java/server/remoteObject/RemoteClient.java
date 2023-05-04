package server.remoteObject;

import server.Server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class RemoteClient extends UnicastRemoteObject implements IRemoteClient{
    private Server server;
    private String username;
    public RemoteClient() throws RemoteException {
    }

    public void setServer(Server server) {
        this.server = server;
    }
    @Override
    public String getUsername() throws RemoteException {
        // return the username associated with this client
        return this.username;
    }




}
