package server.remoteObject;

import client.WhiteboardManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteRoom extends Remote {

    public void createRoom(WhiteboardManager whiteboardManager) throws RemoteException;
}
