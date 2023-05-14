package gui;

import models.Room;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerGUI {
    private final Server server;
    private final JList<String> roomList;

    public ServerGUI(Server server) {
        this.server = server;
        JFrame frame = new JFrame("Server Panel");
        frame.setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    server.broadCastAllRoomsServerClosing();
                } catch (IOException ignored) {
                    System.exit(0);
                }
            }
        });

        // Create a panel for the room list
        JPanel roomListPanel = new JPanel(new BorderLayout());
        JLabel roomListLabel = new JLabel("Rooms:");
        roomListPanel.add(roomListLabel, BorderLayout.NORTH);

        // Create the room list
        DefaultListModel<String> roomListModel = new DefaultListModel<>();
        roomList = new JList<>(roomListModel);
        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane roomListScrollPane = new JScrollPane(roomList);
        roomListPanel.add(roomListScrollPane, BorderLayout.CENTER);

        // Add the room list panel to the frame
        frame.add(roomListPanel, BorderLayout.CENTER);

        // Add a button to refresh the room list
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> updateRoomList());
        frame.add(refreshButton, BorderLayout.SOUTH);

        // Add a listener to the room list to handle clicks
        roomList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Handle the click event here
                String selectedRoom = roomList.getSelectedValue();
                if (selectedRoom != null) {
                    int roomID = Integer.parseInt(selectedRoom.split(" ")[1]);
                    handleRoomClick(roomID);
                }
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void updateRoomList() {
        HashMap<Integer, Room> roomHashMap = (HashMap<Integer, Room>) server.getRoomMap();
        DefaultListModel<String> roomListModel = (DefaultListModel<String>) roomList.getModel();
        roomListModel.clear();
        for (Map.Entry<Integer,Room> entry : roomHashMap.entrySet()){
            int roomID = entry.getKey();
            Room room = entry.getValue();
            String roomInfo = "Room " + roomID + " - Manager: " + room.getWhiteboardManager().getUsername() + ", Players: " + room.getNumberOfClient();
            roomListModel.addElement(roomInfo);
        }
    }

    public void handleRoomClick(int roomID) {
        // Handle the room click event here
        System.out.println("Clicked on room " + roomID);
    }
}