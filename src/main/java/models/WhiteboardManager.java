package models;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;

public class WhiteboardManager extends  WhiteboardClient{


    public WhiteboardManager(String username) throws IOException, NotBoundException {
        super(username);

    }


}
