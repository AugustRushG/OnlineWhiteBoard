package gui;

import javax.swing.*;
import java.awt.*;

public class ServerGUI {
    private final JFrame frame;
    public ServerGUI(){
        this.frame = new JFrame("Server Panel");
        frame.setLayout(new FlowLayout());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
