package client;

import constant.PopUpDialog;
import constant.RegistryConstant;
import server.remoteObject.*;
import application.WhiteboardManagerApp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static client.JoinWhiteBoard.HEARTBEAT_INTERVAL_MS;

public class CreateWhiteboard {
    private static int serverPort;
    private static String serverAddress;
    private static String userName;

    public CreateWhiteboard(int serverPort, String serverAddress, String userName){
        CreateWhiteboard.serverPort = serverPort;
        CreateWhiteboard.serverAddress = serverAddress;
        CreateWhiteboard.userName = userName;
    }

    public static void main(String[] args) throws UnknownHostException {
        if (serverPort==0&&serverAddress==null&&userName==null){
            commandLineParser(args);
        }

        createWhiteboardApp();


    }

    public static void commandLineParser(String[] args) throws UnknownHostException {
        // Parse command line arguments
        if (args.length<1){
            System.err.println("Usage: java -jar JoinWhiteboard.jar -i <ip address> -p <port number> -u <username>");
            System.exit(1);
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p") && i < args.length - 1) {
                // Parse port number
                try {
                    serverPort = Integer.parseInt(args[i+1]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid port number: " + args[i+1]);
                    System.exit(1);
                }
            } else if (args[i].equals("-u") && i < args.length - 1) {
                // Parse username
                userName = args[i+1];
            } else if (args[i].equals("-i") && i <args.length - 1) {
                serverAddress = args[i+1];
            }

        }

        // Check if all required arguments were provided
        if (userName == null) {
            System.err.println("Usage: java MyProgram -u <username>\n you have to specify your user name before connecting");
            System.exit(1);
        }
        if (serverPort==0){
            System.out.println("No port number input, using default 1099");
            serverPort = 1099;
        }
        if (serverAddress==null){
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("No ip address input, using default " + ip);
            serverAddress = String.valueOf(ip);
        }

        // Use the parsed values
        System.out.println("Server address: " + serverAddress);
        System.out.println("Port number: " + serverPort);
        System.out.println("Username: " + userName);
        System.out.println("connecting to host now");
    }

    public static void createWhiteboardApp(){
        try{
            // bind remote objects
            Registry registry = LocateRegistry.getRegistry(serverAddress,serverPort);
            IRemoteServer remoteServer = (IRemoteServer) registry.lookup(RegistryConstant.REMOTE_SERVER);
            try {
                // create room in server
//                RemoteManager manager = new RemoteManager();
//                remoteManager.createRoom(manager,userName);
                IRemoteManager remoteManager = remoteServer.registerManager(userName);
                start(remoteServer,remoteManager.getRoomID());
                // create the app
                WhiteboardManagerApp whiteboardManagerApp = new WhiteboardManagerApp(remoteManager);
                // start whiteboard app
                whiteboardManagerApp.createWhiteboardManager();

            }catch (Exception e){
                e.printStackTrace();
                PopUpDialog.showErrorMessageDialog("creating room failed, please check server status and try again",null);
                throw new RuntimeException(e);
            }



        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
            PopUpDialog.showErrorMessageDialog("connecting to server failed, please check server status such as port number " +
                    "and ip address then try again ",null);

            throw new RuntimeException(e);
        }
    }

    public static void start(IRemoteServer server, Integer roomID) throws RemoteException {
        new Thread(() -> {
            while (true) {
                try {
                    server.heartbeat(roomID);
                    Thread.sleep(HEARTBEAT_INTERVAL_MS);
                } catch (RemoteException | InterruptedException e) {
                    PopUpDialog.showErrorMessageDialog("Connection failed to server exiting now ",null);
                }
            }
        }).start();
    }


}
