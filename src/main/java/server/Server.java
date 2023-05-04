package server;

import constant.RegistryConstant;
import gui.ServerGUI;
import models.ChatMessage;
import models.Room;
import models.WhiteboardManager;
import server.remoteObject.*;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    // add in try and catch for port number and address
    static int portNumber = 1234;

    private Map<Integer,Room> roomMap;
    private ServerGUI serverGUI;
    public Server(){
        roomMap = new HashMap<Integer,Room>();
        serverGUI = new ServerGUI();
    }

    public static void main(String[] args) {
        try{
            LocateRegistry.createRegistry(1099);

            RemoteManager remoteManager = new RemoteManager();
            RemoteClient remoteClient = new RemoteClient();

            Registry registry = LocateRegistry.getRegistry();
            // provide service

            registry.bind(RegistryConstant.REMOTE_CLIENT, remoteClient);
            registry.bind(RegistryConstant.REMOTE_MANAGER,remoteManager);
            System.out.println("All binds on registry");

            // create Server
            Server server = new Server();
            remoteManager.setServer(server);
            remoteClient.setServer(server);
            System.out.println("server up and running");


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

    public void createRoom(WhiteboardManager manager, int roomID){
        Room room = new Room(manager,new ArrayList<>(),roomID);
        roomMap.put(roomID,room);
        System.out.println("new rood created and added to the map");
    }

    public void broadCastAllRooms(){

    }

    public void updateChatBoard(String sender, String text, int roomID){
        roomMap.get(roomID).addChatMessage(sender,text);
    }

    public int createRoomID(){
        return roomMap.size()+1;
    }

    public ArrayList<ChatMessage> loadLatestChatBoard(int roomID){
        return roomMap.get(roomID).getChatBoard();
    }

}
