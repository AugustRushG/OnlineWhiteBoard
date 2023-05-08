package gui;

import java.awt.*;
import java.io.Serializable;

public record MyShape(Shape shape, Color color, int penSize) implements Serializable {
}
