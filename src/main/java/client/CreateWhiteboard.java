package client;

import constant.RegistryConstant;
import server.IRemoteDraw;
import application.WhiteboardApp;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteboard {
    private static int serverPort;
    private static String serverAddress;
    private static String userName;


    public static void main(String[] args) {
        // create the app
        WhiteboardApp whiteboardApp = new WhiteboardApp(true);

        try{
            // get remote method
            Registry registry = LocateRegistry.getRegistry("localhost");
            IRemoteDraw remoteDraw = (IRemoteDraw) registry.lookup(RegistryConstant.REMOTE_DRAW);


            // create a communication socket for the manager. So other users can join adn communicate
            // connect to the socket

            // start whiteboard app
            whiteboardApp.createWhiteboard();



        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
