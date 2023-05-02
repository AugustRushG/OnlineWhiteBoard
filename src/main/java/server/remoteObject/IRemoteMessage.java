package server.remoteObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteMessage extends Remote {
    public void sendMessage(String message) throws RemoteException;
}
