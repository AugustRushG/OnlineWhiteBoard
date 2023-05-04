package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

public class DrawingComponent extends JComponent implements MouseListener, MouseMotionListener {

    private List<Shape> shapes = new ArrayList<Shape>();
    private Shape currentShape = null;
    private Point startPoint = null;
    private Point endPoint = null;
    private Color currentColor = Color.BLACK;
    private int currentSize = 1;

    public DrawingComponent() {
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setCurrentColor(Color color) {
        currentColor = color;
    }

    public void setCurrentSize(int size) {
        currentSize = size;
    }

    public void clear() {
        shapes.clear();
        currentShape = null;
        startPoint = null;
        endPoint = null;
        repaint();
    }

    public void undo() {
        if (shapes.size() > 0) {
            shapes.remove(shapes.size() - 1);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(currentSize));

        for (Shape shape : shapes) {
            g2.setPaint(Color.BLACK);
            g2.draw(shape);
        }

        if (currentShape != null) {
            g2.setPaint(currentColor);
            g2.draw(currentShape);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
        endPoint = e.getPoint();
//        switch (currentShape) {
//            case LINE:
//                currentShape = new Line2D.Double(startPoint, endPoint);
//                break;
//            case RECTANGLE:
//                currentShape = new Rectangle2D.Double(startPoint.x, startPoint.y, 0, 0);
//                break;
//            case CIRCLE:
//                currentShape = new Ellipse2D.Double(startPoint.x, startPoint.y, 0, 0);
//                break;
//        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Shape r = makeRectangle(startPoint.x, startPoint.y, e.getX(), e.getY());
        currentShape = null;
        startPoint = null;
        endPoint = null;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        endPoint = e.getPoint();
//        switch (currentShape) {
//            case LINE:
//                ((Line2D) currentShape).setLine(startPoint, endPoint);
//                break;
//            case RECTANGLE:
//                double width = Math.abs(endPoint.x - startPoint.x);
//                double height = Math.abs(endPoint.y - startPoint.y);
//                double x = Math.min(startPoint.x, endPoint.x);
//                double y = Math.min(startPoint.y, endPoint.y);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
}
