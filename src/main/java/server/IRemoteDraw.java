package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteDraw extends Remote {
    public void draw() throws RemoteException;
}
