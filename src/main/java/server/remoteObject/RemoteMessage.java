package server.remoteObject;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class RemoteMessage extends UnicastRemoteObject implements IRemoteMessage{
    public RemoteMessage() throws RemoteException {
    }

    protected RemoteMessage(int port) throws RemoteException {
        super(port);
    }

    protected RemoteMessage(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public void sendMessage(String message) {

    }
}
