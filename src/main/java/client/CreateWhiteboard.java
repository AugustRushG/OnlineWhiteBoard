package client;

import constant.PopUpDialog;
import constant.RegistryConstant;
import server.remoteObject.*;
import application.WhiteboardManagerApp;

import javax.swing.*;
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
        // Declare variables
        serverAddress = null;
        serverPort = 0;
        userName = null;

        // Process command-line arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p") && i < args.length - 1) {
                // Parse port number
                try {
                    serverPort = Integer.parseInt(args[i+1]);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid port number: " + args[i+1]);
                    System.exit(1);
                }
            } else if (args[i].equals("-u") && i < args.length - 1) {
                // Parse username
                userName = args[i+1];
            } else if (args[i].equals("-i") && i < args.length - 1) {
                serverAddress = args[i+1];
            }
        }


        if (userName == null || userName.trim().isEmpty()) {
            do {
                userName = JOptionPane.showInputDialog(null, "Please enter your username:");
            } while (userName == null || userName.trim().isEmpty());
        }
        if (serverPort == 0) {
            String portInput = JOptionPane.showInputDialog(null, "Enter server port number (default 1099):");
            if (portInput != null && !portInput.isEmpty()) {
                try {
                    serverPort = Integer.parseInt(portInput);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid port number: " + portInput);
                    System.exit(1);
                }
            } else {
                serverPort = 1099;
            }
        }
        if (serverAddress == null) {
            String addressInput = JOptionPane.showInputDialog(null, "Enter server address (default local host):");
            if (addressInput != null && !addressInput.isEmpty()) {
                serverAddress = addressInput;
            } else {
                InetAddress ip = InetAddress.getLocalHost();
                serverAddress = ip.getHostAddress();
            }
        }


        // Use the parsed values
        JOptionPane.showMessageDialog(null,
                "Server address: " + serverAddress + "\n" +
                        "Port number: " + serverPort + "\n" +
                        "Username: " + userName + "\n" +
                        "Connecting to host now, application will appear once connection is established");

        // Connect to host and perform further actions
    }

    public static void createWhiteboardApp(){
        try{
            // bind remote objects
            Registry registry = LocateRegistry.getRegistry(serverAddress,serverPort);
            IRemoteServer remoteServer = (IRemoteServer) registry.lookup(RegistryConstant.REMOTE_SERVER);
            try {
                // create room in server
                IRemoteManager remoteManager = remoteServer.registerManager(userName);
                start(remoteServer,remoteManager.getRoomID());
                // create the app
                WhiteboardManagerApp whiteboardManagerApp = new WhiteboardManagerApp(remoteManager);
                // start whiteboard app
                whiteboardManagerApp.createWhiteboardManager();

            }catch (Exception e){
                PopUpDialog.showErrorMessageDialog("creating room failed, please check server status and try again",null);
            }
        } catch (IOException | NotBoundException e) {
            PopUpDialog.showErrorMessageDialog("connecting to server failed, please check server status such as port number " +
                    "and ip address then try again ",null);
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
