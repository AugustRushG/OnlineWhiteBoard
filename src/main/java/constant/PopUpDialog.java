package constant;

import javax.swing.*;

public class PopUpDialog {
    public static void showErrorMessageDialog(String message, JFrame parent) {
        JOptionPane.showMessageDialog(parent, message, "Message", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
