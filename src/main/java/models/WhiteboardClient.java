package models;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.util.ArrayList;

public class WhiteboardClient implements Serializable {

    private String username;
    private int roomID;
    private ArrayList<ChatMessage> chatMessages;

    public WhiteboardClient(String username) throws IOException, NotBoundException {
        this.username = username;
        this.chatMessages = new ArrayList<>();

    }

    @Override
    public String toString() {
        return "WhiteboardClient{" +
                "username='" + username + '\'' +
                '}';
    }

    public void setRoomID(int roomID){
        this.roomID = roomID;
    }
    public int getRoomID() {
        return roomID;
    }

    public String getUsername() {
        return username;
    }

}
