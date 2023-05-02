package server;

import constant.RegistryConstant;
import gui.ServerGUI;
import server.remoteObject.RemoteDraw;
import server.remoteObject.RemoteMessage;
import server.remoteObject.RemoteRoom;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Server {

    // add in try and catch for port number and address
    static int portNumber = 1234;
    private List<Room> roomList ;
    private ServerGUI serverGUI;
    public Server(){
        roomList = new ArrayList<>();
        serverGUI = new ServerGUI();
    }

    public static void main(String[] args) {
        try{
            LocateRegistry.createRegistry(1099);

            RemoteDraw remoteDraw = new RemoteDraw();
            RemoteMessage remoteMessage = new RemoteMessage();
            RemoteRoom remoteRoom = new RemoteRoom();

            Registry registry = LocateRegistry.getRegistry();
            // provide service
            registry.bind(RegistryConstant.REMOTE_DRAW, remoteDraw);
            registry.bind(RegistryConstant.REMOTE_MESSAGE, remoteMessage);
            registry.bind(RegistryConstant.REMOTE_ROOM, remoteRoom);
            System.out.println("All binds on registry");

            ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();

            ServerSocket serverSocket = serverSocketFactory.createServerSocket(portNumber);
            // create Server
            Server server = new Server();
            remoteRoom.setServer(server);

            System.out.println("server socket ready for connection");
            // thread per connection to accept connection
            while (true){
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                new Thread(clientHandler).start();
            }
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void createRoom(Room room){
        roomList.add(room);
    }

}
