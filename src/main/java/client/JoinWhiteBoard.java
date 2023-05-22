package client;

import application.WhiteboardApp;
import constant.PopUpDialog;
import constant.RegistryConstant;
import server.remoteObject.IRemoteClient;
import server.remoteObject.IRemoteServer;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class JoinWhiteBoard {
    public static final long HEARTBEAT_INTERVAL_MS = 10000;
    public static int serverPort;
    public static String serverAddress;
    public static String userName;
    public static int roomID;

    public static void main(String[] args) throws UnknownHostException {

        commandLineParser(args);

        try {
            Registry registry = LocateRegistry.getRegistry(serverAddress,serverPort);
            IRemoteServer remoteServer = (IRemoteServer) registry.lookup(RegistryConstant.REMOTE_SERVER);

            boolean roomExisted = remoteServer.checkRoomExisted(roomID);
            if (!roomExisted){
                PopUpDialog.showErrorMessageDialog("room doesnt exist please check then try again",null);
            }
            boolean usernameExisted = remoteServer.checkUsernameExisted(userName,roomID);
            if (usernameExisted){
                PopUpDialog.showErrorMessageDialog("Username has already existed in this room please use another one",null);
            }else {
                boolean confirmed = remoteServer.confirmClientJoin(userName,roomID);
                if (confirmed){
                    try {
                        // try register to the manager
                        IRemoteClient remoteClient = remoteServer.registerClientToRoom(roomID,userName);
//                        start(remoteServer);
                        // create the app
                        WhiteboardApp whiteboardApp = new WhiteboardApp(remoteClient,remoteServer, userName);
                        // start whiteboard app
                        whiteboardApp.createWhiteboard();

                    }catch (Exception e){
                        PopUpDialog.showErrorMessageDialog("Join room failed, please check server status and try again ",null);
                    }
                }
                else {
                    PopUpDialog.showErrorMessageDialog("The manager refused you to join.",null);
                }
            }

        } catch (NotBoundException | RemoteException e) {
            PopUpDialog.showErrorMessageDialog("Connection failed, Please check that you have entered correct server address and portNumber",null);
        }
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

        // Prompt user for missing arguments
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
        if (roomID == 0) {
            do {
                String roomIDInput = JOptionPane.showInputDialog(null, "Enter room number:");
                try {
                    roomID = Integer.parseInt(roomIDInput);
                } catch (NumberFormatException e) {
                    roomID = 0;
                }
            } while (roomID == 0);
        }

        Thread connectionThread = new Thread(() -> {
            // Use the parsed values
            JOptionPane.showMessageDialog(null,
                    "Server address: " + serverAddress + "\n" +
                            "Port number: " + serverPort + "\n" +
                            "Username: " + userName + "\n" +
                            "Room ID: " + roomID + "\n" +
                            "Connecting to host now, application will appear once connection is established");
        });

        connectionThread.start();
    }

    public static void start(IRemoteServer server) throws RemoteException {
        new Thread(() -> {
            while (true) {
                try {
                    server.clientHeartbeat(userName,roomID);
                    Thread.sleep(HEARTBEAT_INTERVAL_MS);
                } catch (RemoteException | InterruptedException e) {
                    PopUpDialog.showErrorMessageDialog_noExit("connection failed exiting now",null);
                }
            }
        }).start();
    }
}
