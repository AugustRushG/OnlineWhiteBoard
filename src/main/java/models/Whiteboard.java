package models;

import gui.MyShape;
import gui.MyText;
import gui.WhiteboardManagerGUI;

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

    public void addShape(MyShape shape){
        shapes.add(shape);
    }

    public void setShapes(ArrayList<MyShape> shapes) {
        this.shapes = shapes;
    }

    public void setTexts(ArrayList<MyText> texts) {
        this.texts = texts;
    }

    public void addText(MyText text){
        texts.add(text);
    }



}
