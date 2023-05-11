package models;

public class WhiteboardClient {

    private final String username;
    private int roomID;

    public WhiteboardClient(String username) {
        this.username = username;
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
