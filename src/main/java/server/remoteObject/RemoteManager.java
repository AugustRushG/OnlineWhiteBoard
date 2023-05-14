package server.remoteObject;

import models.ChatMessage;
import models.MyShape;
import models.MyText;
import models.WhiteboardClient;
import server.Server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoteManager extends UnicastRemoteObject implements IRemoteManager, IRemoteObserver {
    private Server server;
    private final HashMap<String,IRemoteClient> clientMap;
    private String username;
    private int roomID;
    private IRemoteObserver observer;

    ExecutorService executorServiceThreadPool = Executors.newFixedThreadPool(10);
    public void setServer(Server server) {
        this.server = server;
    }
    public RemoteManager() throws RemoteException {
        clientMap = new HashMap<>();
    }
    @Override
    public void sendMessage(String message, String username, int roomID) throws RemoteException {
        System.out.println("getting message from "+username+" message is "+message);
        server.updateChatBoard(username,message,roomID);

//        notifyObserver(new ChatMessage(username,message));
    }
    @Override
    public void sendDrawing(ArrayList<MyShape> shapes) throws RemoteException {
        System.out.println("getting drawing from "+username + "in room "+roomID);
        server.updateWhiteBoardShape(shapes, roomID);

    }
    @Override
    public void sendText(ArrayList<MyText> texts) throws RemoteException {
        System.out.println("getting text from " + username);
        server.updateWhiteboardText(texts,roomID);
    }
    @Override
    public ArrayList<ChatMessage> receiveMessage() throws RemoteException {
        System.out.println("latest is "+ server.loadLatestChatBoard(roomID).toString());
        return server.loadLatestChatBoard(roomID);
    }
    @Override
    public synchronized void registerClient(IRemoteClient client, String username) throws IOException {
        client.setUsername(username);
        client.setRoomID(roomID);
        clientMap.put(username,client);
        System.out.println("Client " + client.getUsername() + " registered on manager.");
        WhiteboardClient whiteboardClient = new WhiteboardClient(username);
        server.addClientToRoom(whiteboardClient,roomID);
        System.out.println("Client " + client.getUsername() + " added into room ."+ roomID);
    }
    @Override
    public void unRegisterClient(IRemoteClient client, String username) throws IOException {
        clientMap.remove(username);
        server.removeClientInRoom(username,roomID);
        System.out.println("Client " + client.getUsername() + " removed on manager.");
    }
    @Override
    public String getUsername() throws RemoteException{
        return username;
    }
    @Override
    public int getRoomID() throws RemoteException {
        return roomID;
    }
    @Override
    public ArrayList<String> getUsersInRoom() throws RemoteException {
        return server.getUserInRoom(roomID);
    }
    @Override
    public IRemoteClient getClient(String username) throws RemoteException {
        System.out.println("getting client "+clientMap.get(username).getRoomId());
        return clientMap.get(username);
    }
    @Override
    public void registerObserver(IRemoteObserver observer) throws RemoteException {
        this.observer = observer;
    }
    @Override
    public void removeObserver(IRemoteObserver observer) throws RemoteException {

    }

    @Override
    public HashMap<String, IRemoteClient> getClientMap() throws RemoteException{
        System.out.println("manager  "+username+"clients are "+clientMap);
        return clientMap;
    }

    @Override
    public boolean confirmClientJoin(String username) throws RemoteException {
        return notifyJoinRequest(username);
    }

    @Override
    public boolean checkUsernameExisted(String username) throws RemoteException {
        return server.checkUsernameExisted(username,roomID);
    }

    @Override
    public void kickUser(String username) throws IOException {
        System.out.println("user "+username+" been kicked");
        IRemoteClient remoteClient = clientMap.get(username);
        executorServiceThreadPool.submit(()->{
            try {
                remoteClient.notifyUserBeenKicker();
            } catch (IOException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        });

        clientMap.remove(username);
        server.removeClientInRoom(username,roomID);
        System.out.println("Client " + username + " being kicked by manager.");
    }


    @Override
    public void notifyNewMessage(ChatMessage message) throws RemoteException {
        System.out.println("notifying message to manager" + username);
        observer.notifyNewMessage(message);
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            // Do something with the clientName and client
            try {
                client.notifyNewMessage(message);
            }catch (RuntimeException e){
                try {
                    unRegisterClient(client,client.getUsername());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    @Override
    public void notifyUserChange(ArrayList<String> users) throws RemoteException {
        System.out.println("manager user are " + users);
        observer.notifyUserChange(users);
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            // Do something with the clientName and client
            try {
                client.notifyUserChange(users);
            }catch (RuntimeException e){
                try {
                    unRegisterClient(client,client.getUsername());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    @Override
    public void notifyShapeChange(ArrayList<MyShape> shapes) throws RemoteException{
        observer.notifyShapeChange(shapes);
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            // Do something with the clientName and client
            try {
                client.notifyShapeChange(shapes);
            }
            catch (IOException e){
                try {
                    unRegisterClient(client,client.getUsername());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    @Override
    public void notifyTextsChange(ArrayList<MyText> texts) throws RemoteException {
        observer.notifyTextsChange(texts);
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            // Do something with the clientName and client
            try {
                client.notifyTextsChange(texts);
            }catch (RuntimeException e){
                try {
                    unRegisterClient(client,client.getUsername());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    @Override
    public boolean notifyJoinRequest(String username) throws RemoteException {
        return observer.notifyJoinRequest(username);
    }

    @Override
    public void notifyRoomClose() throws RemoteException {
        System.out.println(clientMap.keySet());
        for (IRemoteClient client : clientMap.values()) {
            System.out.println("telling "+client.getUsername()+ "room is closing");
            executorServiceThreadPool.submit(()->{
                try {
                    client.notifyRoomClose();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });

        }
        server.closeRoom(roomID);
        System.out.println("room officially closed ");
    }

    @Override
    public void notifyUserBeenKicker() throws RemoteException {

    }

    @Override
    public void notifyServerClosing() throws RemoteException {
        for (Map.Entry<String, IRemoteClient> entry : clientMap.entrySet()) {
            IRemoteClient client = entry.getValue();
            executorServiceThreadPool.submit(()->{
                try {
                    client.notifyServerClosing();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });

        }
        observer.notifyServerClosing();
    }
    @Override
    public void setUsername(String username) throws RemoteException {
        this.username = username;
    }
    @Override
    public void setRoomID(int roomID) throws RemoteException {
        this.roomID = roomID;
    }


}
