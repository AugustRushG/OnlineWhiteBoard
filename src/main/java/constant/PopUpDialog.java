package constant;

import javax.swing.*;

public class PopUpDialog {
    public static void showErrorMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
