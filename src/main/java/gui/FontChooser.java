package gui;
import java.awt.*;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class FontChooser {

    public static Font showFontChooser() {
        JComboBox<String> fontNameComboBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        JComboBox<Integer> fontSizeComboBox = new JComboBox<>(new Integer[] { 8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 48, 72 });
        Object[] message = {"Font:", fontNameComboBox, "Size:", fontSizeComboBox};
        int option = JOptionPane.showConfirmDialog(null, message, "Font Chooser", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String fontName = (String)fontNameComboBox.getSelectedItem();
            int fontSize = (Integer)fontSizeComboBox.getSelectedItem();
            return new Font(fontName, Font.PLAIN, fontSize);
        } else {
            return null;
        }
    }
}