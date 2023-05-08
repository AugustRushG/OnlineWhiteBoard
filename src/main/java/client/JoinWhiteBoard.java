package client;

import application.WhiteboardApp;
import application.WhiteboardManagerApp;
import constant.PopUpDialog;
import constant.RegistryConstant;
import server.remoteObject.IRemoteClient;
import server.remoteObject.IRemoteManager;
import server.remoteObject.RemoteClient;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

public class JoinWhiteBoard {
    private static int serverPort = 1234;
    private static String serverAddress = "localhost";
    private static String userName = "Serena";

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("192.168.20.12");
            IRemoteManager remoteManager = (IRemoteManager) registry.lookup(RegistryConstant.REMOTE_MANAGER);
            IRemoteClient remoteClient = (IRemoteClient) registry.lookup(RegistryConstant.REMOTE_CLIENT);


            boolean usernameExisted = remoteManager.checkUsernameExisted(userName);
            if (usernameExisted){
                PopUpDialog.showErrorMessageDialog("Username has already existed in this room please use another one");
            }else {
                boolean confirmed = remoteManager.confirmClientJoin(userName);
                if (confirmed){
                    try {
                        // try register to the manager

                        remoteManager.registerClient(remoteClient,userName);
                        System.out.println(remoteManager.getClient(userName).getRoomId());

                        // create the app
                        WhiteboardApp whiteboardApp = new WhiteboardApp(remoteClient,remoteManager, userName);
                        // start whiteboard app
                        whiteboardApp.createWhiteboard();

                    }catch (Exception e){
                        e.printStackTrace();
                        throw new RuntimeException("creating room failed, please check server status and try again " + e);

                    }
                }
                else {
                    PopUpDialog.showErrorMessageDialog("The manager refused you to join.");

                }
            }


        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
