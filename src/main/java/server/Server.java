package server;

import constant.RegistryConstant;
import gui.MyShape;
import gui.MyText;
import gui.ServerGUI;
import models.ChatMessage;
import models.Room;
import models.WhiteboardClient;
import models.WhiteboardManager;
import server.remoteObject.IRemoteManager;
import server.remoteObject.RemoteClient;
import server.remoteObject.RemoteManager;
import server.remoteObject.RemoteServer;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final Map<Integer,Room> roomMap;
    private final ServerGUI serverGUI;
    private final Map<Integer, IRemoteManager> remoteManagerMap;
    public Server(){
        roomMap = new HashMap<>();
        serverGUI = new ServerGUI(this);
        remoteManagerMap =  new HashMap<>();
    }
    public static void main(String[] args) {
        try{
            InetAddress ip = InetAddress.getLocalHost();
            LocateRegistry.createRegistry(1099);

            RemoteManager remoteManager = new RemoteManager();
            RemoteClient remoteClient = new RemoteClient();
            RemoteServer remoteServer = new RemoteServer();

            Registry registry = LocateRegistry.getRegistry();
            // provide service

            registry.bind(RegistryConstant.REMOTE_CLIENT, remoteClient);
            registry.bind(RegistryConstant.REMOTE_MANAGER,remoteManager);
            registry.bind(RegistryConstant.REMOTE_SERVER,remoteServer);
            System.out.println("Registry running at " + ip + ":1099");

            // create Server
            Server server = new Server();
            remoteManager.setServer(server);
            remoteClient.setServer(server);
            remoteServer.setServer(server);
            System.out.println("server up and running");


        } catch (AlreadyBoundException | IOException e) {
            throw new RuntimeException(e);
        }


    }
    public void createRoom(IRemoteManager remoteManager, int roomID) throws IOException {
        WhiteboardManager manager = new WhiteboardManager(remoteManager.getUsername());
        Room room = new Room(manager,new HashMap<>(),roomID);
        roomMap.put(roomID,room);
        remoteManagerMap.put(roomID,remoteManager);
        System.out.println(remoteManager.getRoomID());
        System.out.println("new room created and added to the map room id is "+roomID + " manager is " +remoteManager.getUsername());
        serverGUI.updateRoomList();
    }
    public void addClientToRoom(WhiteboardClient client, int roomID) throws RemoteException {
        Room room = roomMap.get(roomID);
        room.addClientInRoom(client);
        IRemoteManager roomManager = remoteManagerMap.get(roomID);
        roomManager.notifyUserChange(room.getUsersInRoom());
        serverGUI.updateRoomList();
    }
    public void removeClientInRoom(String username, int roomID) throws RemoteException{
        Room room = roomMap.get(roomID);
        room.removeClientInRoom(username);
        IRemoteManager roomManager = remoteManagerMap.get(roomID);
        roomManager.notifyUserChange(room.getUsersInRoom());
        serverGUI.updateRoomList();
    }
    public boolean checkUsernameExisted(String username, int roomID){
        Room room = roomMap.get(roomID);
        return room.checkUsernameExist(username);
    }
    public void broadCastAllRoomsServerClosing() throws RemoteException {
        for (Map.Entry<Integer, IRemoteManager> entry : remoteManagerMap.entrySet()) {
            IRemoteManager manager = entry.getValue();
            // Do something with the clientName and client
            try {
                manager.notifyServerClosing();
            }catch (RemoteException ignored){

            }

        }
    }
    public void updateChatBoard(String sender, String text, int roomID) throws RemoteException {
        ChatMessage message = new ChatMessage(sender,text);
        roomMap.get(roomID).addChatMessage(message);
        IRemoteManager roomManager = remoteManagerMap.get(roomID);

        System.out.println("getting manager name");
        System.out.println(roomManager.getUsername());
        System.out.println(remoteManagerMap.get(roomID).getClientMap());
        roomManager.notifyNewMessage(message);
    }
    public void updateWhiteBoardShape(ArrayList<MyShape> shapes, int roomID) throws RemoteException{
        roomMap.get(roomID).setShapes(shapes);
        IRemoteManager roomManager = remoteManagerMap.get(roomID);
        Room room = roomMap.get(roomID);
        System.out.println("room "+roomID + " is getting new shape");
        roomManager.notifyShapeChange(room.getShapeList());

    }
    public void updateWhiteboardText(ArrayList<MyText> texts, int roomID) throws RemoteException{
        roomMap.get(roomID).setTexts(texts);
        IRemoteManager roomManager = remoteManagerMap.get(roomID);
        Room room = roomMap.get(roomID);
        roomManager.notifyTextsChange(room.getTextList());

    }
    public ArrayList<MyShape> getRoomShapes(int roomID){
        return roomMap.get(roomID).getShapeList();
    }
    public ArrayList<MyText> getRoomTexts(int roomID){
        return roomMap.get(roomID).getTextList();
    }
    public int createRoomID(){
        return roomMap.size()+1;
    }
    public ArrayList<ChatMessage> loadLatestChatBoard(int roomID){
        return roomMap.get(roomID).getChatBoard();
    }
    public ArrayList<String> getUserInRoom(int roomID){
        System.out.println("user in room are "+roomMap.get(roomID).getUsersInRoom());
        return roomMap.get(roomID).getUsersInRoom();
    }
    public void closeRoom(int roomID){
        roomMap.remove(roomID);
        serverGUI.updateRoomList();
    }
    public Map<Integer,Room> getRoomMap(){
        return roomMap;
    }
    public IRemoteManager getManager(int roomID){
        return remoteManagerMap.get(roomID);
    }
}
