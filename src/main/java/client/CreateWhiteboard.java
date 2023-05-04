package client;

import constant.RegistryConstant;
import models.WhiteboardManager;
import server.remoteObject.*;
import application.WhiteboardApp;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CreateWhiteboard {
    private static int serverPort = 1234;
    private static String serverAddress = "localhost";
    private static String userName = "august";


    public static void main(String[] args) {

        try{
            // bind remote objects
            Registry registry = LocateRegistry.getRegistry(serverAddress);
            IRemoteManager remoteManager = (IRemoteManager) registry.lookup(RegistryConstant.REMOTE_MANAGER);

            try {
                // create room in server
                RemoteManager manager = new RemoteManager();
                remoteManager.createRoom(manager,userName);
                // create the app
                WhiteboardApp whiteboardApp = new WhiteboardApp(remoteManager);
                // start whiteboard app
                whiteboardApp.createWhiteboard();

            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("creating room failed, please check server status and try again " + e);

            }



        } catch (IOException | NotBoundException e) {
            throw new RuntimeException("connecting to server failed, please check server status such as port number " +
                    "and ip address then try again " + e);
        }


    }
}
