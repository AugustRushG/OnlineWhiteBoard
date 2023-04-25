package server;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteboardServer {
    public static void main(String[] args) {
        int port = 6302;
        try{
            LocateRegistry.createRegistry(port);
            RemoteDraw remoteDraw = new RemoteDraw();
            Registry registry = LocateRegistry.getRegistry(port);
            registry.bind("Draw", remoteDraw);
            System.out.println("WhiteBoard server ready");
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}