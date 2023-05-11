package models;

import gui.MyShape;
import gui.MyText;

import java.util.ArrayList;

public class Whiteboard {
    public ArrayList<MyShape> shapes;
    public ArrayList<MyText> texts;

    public Whiteboard(){
        shapes = new ArrayList<>();
        texts = new ArrayList<>();
    }

    public ArrayList<MyShape> getShapes() {
        return shapes;
    }

    public ArrayList<MyText> getTexts() {
        return texts;
    }

    public synchronized void setShapes(ArrayList<MyShape> shapes) {
        this.shapes = shapes;
    }

    public synchronized void setTexts(ArrayList<MyText> texts) {
        this.texts = texts;
    }


}
