package gui;

import java.awt.*;
import java.io.Serializable;

public class MyText implements Serializable {
    private String text;
    private Color color;
    private final Font font;

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    private int penSize;

    private final int x1;
    private final int y1;
    public MyText(String text, Color color, int penSize, int x1, int y1, Font font){
        this.text = text;
        this.color = color;
        this.penSize = penSize;
        this.x1 = x1;
        this.y1 = y1;
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public Font getFont(){
        return font;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPenSize() {
        return penSize;
    }

    public void setPenSize(int penSize) {
        this.penSize = penSize;
    }
}
