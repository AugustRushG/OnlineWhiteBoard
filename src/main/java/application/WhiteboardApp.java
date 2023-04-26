package application;

import gui.WhiteboardGUI;

public class WhiteboardApp {

    private boolean isManger;
    public WhiteboardApp(boolean isManger){
        this.isManger = isManger;
    }
    public void createWhiteboard(){
        WhiteboardGUI whiteboardGUI = new WhiteboardGUI();
    }

}