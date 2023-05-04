package models;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.util.ArrayList;

public class WhiteboardClient implements Serializable {

    private String username;
    private int roomID;


    private ArrayList<ChatMessage> chatMessages;

    public WhiteboardClient(String username, int roomID) throws IOException, NotBoundException {
        this.username = username;
        this.roomID = roomID;
        this.chatMessages = new ArrayList<>();

    }

    @Override
    public String toString() {
        return "WhiteboardClient{" +
                "username='" + username + '\'' +
                '}';
    }

    public int getRoomID() {
        return roomID;
    }

    public String getUsername() {
        return username;
    }

    public void updateChatBoard(ChatMessage message){
        chatMessages.add(message);
        System.out.println(chatMessages);
        System.out.println("got message from server "+message);
    }

    public ArrayList<ChatMessage> getChatMessages(){
        System.out.println(chatMessages);
        return chatMessages;
    }
}
