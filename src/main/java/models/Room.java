package models;

import server.remoteObject.IRemoteClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Room {

    private int roomID;
    private WhiteboardManager whiteboardManager;
    private List<WhiteboardClient> roomClients;
    private Whiteboard whiteboard;
    private ArrayList<ChatMessage> chatBoard;
    public Room(WhiteboardManager whiteboardManager, List<WhiteboardClient> roomClients, int roomID){
        this.whiteboardManager = whiteboardManager;
        this.roomClients = roomClients;
        this.roomID = roomID;
        this.whiteboard = new Whiteboard();
        this.chatBoard = new ArrayList<>();
        roomClients.add(whiteboardManager);
    }

    public void addChatMessage(String sender,String message){
        ChatMessage chatMessage = new ChatMessage(sender,message);
        chatBoard.add(chatMessage);
        System.out.println("notifying all clients in this room");
        notifyClients(chatMessage);
    }

    public ArrayList<ChatMessage> getChatBoard(){
        System.out.println(chatBoard);
        return  chatBoard;
    }

    private void notifyClients(ChatMessage chatMessage){
        for (WhiteboardClient client : roomClients){
            client.updateChatBoard(chatMessage);
        }
    }
}
