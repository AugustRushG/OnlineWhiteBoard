package models;

import gui.MyShape;
import gui.MyText;

import java.util.ArrayList;
import java.util.HashMap;

public class Room {

    public final int roomID;
    private final WhiteboardManager whiteboardManager;
    private final HashMap<String, WhiteboardClient> clientHashMap;
    private final Whiteboard whiteboard;
    private final ArrayList<ChatMessage> chatBoard;
    public Room(WhiteboardManager whiteboardManager, HashMap<String, WhiteboardClient> clientHashMap, int roomID){
        this.whiteboardManager = whiteboardManager;
        this.clientHashMap = clientHashMap;
        this.whiteboard = new Whiteboard();
        this.chatBoard = new ArrayList<>();
        this.roomID = roomID;
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

        return new ArrayList<>(clientHashMap.keySet());
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
        return clientHashMap.get(username) != null;
    }
    public WhiteboardManager getWhiteboardManager(){
        return whiteboardManager;
    }
    public int getNumberOfClient(){
        return clientHashMap.size();
    }
}
