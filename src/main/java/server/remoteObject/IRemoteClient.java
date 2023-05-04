package server.remoteObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteClient extends Remote {
    public String getUsername() throws RemoteException;

}
