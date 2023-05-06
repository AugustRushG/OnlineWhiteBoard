package models;

import gui.MyShape;
import gui.MyText;
import gui.WhiteboardManagerGUI;
import server.remoteObject.IRemoteClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private int roomID;
    private WhiteboardManager whiteboardManager;
    private volatile HashMap<String, WhiteboardClient> clientHashMap;
    private volatile Whiteboard whiteboard;
    private volatile ArrayList<ChatMessage> chatBoard;
    public Room(WhiteboardManager whiteboardManager, HashMap<String, WhiteboardClient> clientHashMap, int roomID){
        this.whiteboardManager = whiteboardManager;
        this.clientHashMap = clientHashMap;
        this.roomID = roomID;
        this.whiteboard = new Whiteboard();
        this.chatBoard = new ArrayList<>();
        clientHashMap.put(whiteboardManager.getUsername(),whiteboardManager);
    }
    public synchronized void addChatMessage(ChatMessage message){
        chatBoard.add(message);
    }

    public synchronized void setShapes(ArrayList<MyShape> shapes){
        whiteboard.setShapes(shapes);
    }

    public synchronized void setTexts(ArrayList<MyText> texts){
        whiteboard.setTexts(texts);
    }
    public ArrayList<MyShape> getShapeList(){
        return whiteboard.getShapes();
    }
    public ArrayList<MyText> getTextList(){
        return whiteboard.getTexts();
    }
    public ArrayList<ChatMessage> getChatBoard(){
        System.out.println(chatBoard);
        return  chatBoard;
    }
    public ArrayList<String> getUsersInRoom(){

        ArrayList<String> usernames = new ArrayList<>(clientHashMap.keySet());
        return usernames;
    }
    public void addClientInRoom(WhiteboardClient client){
        clientHashMap.put(client.getUsername(),client);
    }
    public void removeClientInRoom(String username){
        clientHashMap.remove(username);
    }
    public WhiteboardClient getSpecificClient(String username){
        return clientHashMap.get(username);
    }

    public boolean checkUsernameExist(String username){
       if (clientHashMap.get(username)!=null){
           return true;
       }
       return false;
    }
    public WhiteboardManager getWhiteboardManager(){
        return whiteboardManager;
    }

    public int getNumberOfClient(){
        return clientHashMap.size();
    }
}
