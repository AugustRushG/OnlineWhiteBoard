package client;

import constant.PopUpDialog;
import constant.RegistryConstant;
import server.remoteObject.*;
import application.WhiteboardManagerApp;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
                WhiteboardManagerApp whiteboardManagerApp = new WhiteboardManagerApp(remoteManager);
                // start whiteboard app
                whiteboardManagerApp.createWhiteboardManager();

            }catch (Exception e){
                PopUpDialog.showErrorMessageDialog("creating room failed, please check server status and try again");
                throw new RuntimeException(e);
            }



        } catch (IOException | NotBoundException e) {
            PopUpDialog.showErrorMessageDialog("connecting to server failed, please check server status such as port number " +
                    "and ip address then try again ");
            throw new RuntimeException(e);
        }

    }


}
