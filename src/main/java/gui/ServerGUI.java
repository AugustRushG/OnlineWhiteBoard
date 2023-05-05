package gui;

import server.Server;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ServerGUI {
    private final JFrame frame;
    private final Server server;
    private JLabel roomLabel;
    private final JTextArea roomListArea;

    public ServerGUI(Server server){
        this.server = server;
        this.frame = new JFrame("Server Panel");
        frame.setLayout(new BorderLayout());

        // Create a panel for the room list
        JPanel roomListPanel = new JPanel(new BorderLayout());
        JLabel roomListLabel = new JLabel("Rooms:");
        roomListPanel.add(roomListLabel, BorderLayout.NORTH);
        roomListArea = new JTextArea(10, 20);
        JScrollPane roomListScrollPane = new JScrollPane(roomListArea);
        roomListPanel.add(roomListScrollPane, BorderLayout.CENTER);

        // Add the room list panel to the frame
        frame.add(roomListPanel, BorderLayout.CENTER);

        // Add a button to refresh the room list
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> updateRoomList());
        frame.add(refreshButton, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void updateRoomList() {
        ArrayList<Integer> roomIds = server.getRoomIDs();
        StringBuilder sb = new StringBuilder();
        for (Integer id : roomIds) {
            sb.append(id).append("\n");
        }
        roomListArea.setText(sb.toString());
    }
}
