package client;

import application.WhiteboardApp;
import constant.PopUpDialog;
import constant.RegistryConstant;
import server.remoteObject.IRemoteClient;
import server.remoteObject.IRemoteServer;

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
                        e.printStackTrace();
                        throw new RuntimeException("creating room failed, please check server status and try again " + e);

                    }
                }
                else {
                    PopUpDialog.showErrorMessageDialog("The manager refused you to join.",null);

                }
            }


        } catch (AccessException | NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            System.err.println("Connection failed, Please check that you have entered correct server address and portNumber");
//            throw new RuntimeException(e);
        }
    }

    public static void commandLineParser(String[] args) throws UnknownHostException {
        // Parse command line arguments
        if (args.length<1){
            System.err.println("Usage: java -jar JoinWhiteboard.jar -i <ip address> -p <port number> -u <username> -r <room number>");
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
            else if(args[i].equals("-r") && i< args.length-1){
                roomID = Integer.parseInt(args[i+1]);
            }
        }

        // Check if all required arguments were provided
        if (userName == null) {
            System.err.println("Usage: java MyProgram -u <username>\n you have to specify your user name before connecting");
            System.exit(1);
        }
        if (roomID == 0){
            System.err.println("Usage: java MyProgram -r <roomID>\n you have to specify the ID of the room you want to connect to before connecting");
            System.exit(1);
        }
        if (serverPort==0){
            System.out.println("No port number input, using default 1099");
            serverPort = 1099;
        }
        if (serverAddress==null){
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("No ip address input, using default " + ip.getHostAddress());
            serverAddress = ip.getHostAddress();
        }

        // Use the parsed values
        System.out.println("Server address: " + serverAddress);
        System.out.println("Port number: " + serverPort);
        System.out.println("Username: " + userName);
        System.out.println("roomID: "+roomID);
        System.out.println("connecting to host now");
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
