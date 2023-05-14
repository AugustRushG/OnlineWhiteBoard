package constant;

import javax.swing.*;

public class PopUpDialog {
    public static void showErrorMessageDialog(String message, JFrame parent) {
        JOptionPane.showMessageDialog(parent, message, "Message", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
    public static void showErrorMessageDialog_noExit(String message, JFrame parent) {
        JOptionPane.showMessageDialog(parent, message, "Message", JOptionPane.ERROR_MESSAGE);
    }

    public static void showConfirmMessage(String message, JFrame parent){
        JOptionPane.showConfirmDialog(parent,message,"Message",JOptionPane.INFORMATION_MESSAGE);
    }
}
