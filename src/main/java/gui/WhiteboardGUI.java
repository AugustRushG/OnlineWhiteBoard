package gui;

import javax.swing.*;

public class WhiteboardGUI {
    private JFrame frame = new JFrame();
    public WhiteboardGUI() {
        // Set the title of the window
        frame.setTitle("Whiteboard");

        // Set the size of the window
        frame.setSize(800, 600);

        // Set the default close operation of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Show the window
        frame.setVisible(true);
    }


}
