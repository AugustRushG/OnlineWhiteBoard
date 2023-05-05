package gui;

import java.awt.*;
import java.io.Serializable;

public class MyShape implements Serializable {
    private Shape shape;
    private Color color;
    private int penSize;

    public MyShape(Shape shape, Color color,int penSize) {
        this.shape = shape;
        this.color = color;
        this.penSize = penSize;
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public int getPenSize(){
        return penSize;
    }
}
