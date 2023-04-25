package server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class RemoteDraw extends UnicastRemoteObject implements IRemoteDraw {

    protected RemoteDraw() throws RemoteException {
    }

    protected RemoteDraw(int port) throws RemoteException {
        super(port);
    }

    protected RemoteDraw(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    public void stop() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
    }

    @Override
    public void draw() throws RemoteException {

    }
}
