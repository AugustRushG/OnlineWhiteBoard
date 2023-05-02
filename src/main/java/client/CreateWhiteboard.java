package client;

import constant.RegistryConstant;
import server.remoteObject.*;
import application.WhiteboardApp;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteboard {
    private static int serverPort = 1234;
    private static String serverAddress = "localhost";
    private static String userName = "august";


    public static void main(String[] args) {

        try{
            // bind remote objects
            Registry registry = LocateRegistry.getRegistry("localhost");
            IRemoteDraw remoteDraw = (IRemoteDraw) registry.lookup(RegistryConstant.REMOTE_DRAW);
            IRemoteMessage remoteMessage = (IRemoteMessage) registry.lookup(RegistryConstant.REMOTE_MESSAGE);
            IRemoteRoom remoteRoom = (IRemoteRoom) registry.lookup(RegistryConstant.REMOTE_ROOM);
            WhiteboardManager whiteboardManager = new WhiteboardManager(userName,serverAddress,serverPort,remoteDraw,remoteMessage,remoteRoom);

            // create the app
            WhiteboardApp whiteboardApp = new WhiteboardApp(true);
            // start whiteboard app
            whiteboardApp.createWhiteboard();



        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
