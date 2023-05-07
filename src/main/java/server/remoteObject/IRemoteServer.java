package server.remoteObject;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteServer extends Remote {
    IRemoteClient registerClientToRoom(int roomID, String username) throws IOException, NotBoundException;

    boolean checkUsernameExisted(String username, int roomID) throws RemoteException;

    boolean confirmClientJoin(String username, int roomID) throws RemoteException;

    void unRegisterClient(IRemoteClient client, String username, int roomID) throws IOException, NotBoundException;

    IRemoteManager registerManager( String username) throws NotBoundException, IOException;
}
