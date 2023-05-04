package gui;

import application.WhiteboardApp;
import models.ChatMessage;
import models.WhiteboardClient;
import server.remoteObject.IRemoteManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;


public class WhiteboardGUI implements  ActionListener, ChangeListener {
    private final JButton colorButton;
    private final JSlider sizeSlider;
    private final JButton lineButton;
    private final JButton circleButton;
    private final JButton ovalButton;
    private final JButton rectangleButton;
    private final JButton freeHandButton;
    private final JButton fontButton;
    private String currentShape = "None";
    private Color currentColor = Color.BLACK;
    private int penSize = 5;
    private Font currentFont = new Font("Arial", Font.PLAIN, 24);
    private WhiteboardApp whiteboardApp;
    public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


    public WhiteboardGUI(WhiteboardApp whiteboardApp) {
        this.whiteboardApp = whiteboardApp;
        JFrame frame = new JFrame();
        frame.setTitle("Whiteboard");

        // Set the size of the window
        frame.setSize(800, 600);

        // Set the default close operation of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the size slider
        sizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, 5);
        sizeSlider.addChangeListener(this);
        sizeSlider.setMajorTickSpacing(5);
        sizeSlider.setMinorTickSpacing(1);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);

        // Create the color button
        colorButton = new JButton("Color");
        colorButton.addActionListener(this);

        // Create the shape buttons
        lineButton = new JButton("Line");
        lineButton.addActionListener(this);

        circleButton = new JButton("Circle");
        circleButton.addActionListener(this);

        ovalButton = new JButton("Oval");
        ovalButton.addActionListener(this);

        rectangleButton = new JButton("Rectangle");
        rectangleButton.addActionListener(this);

        freeHandButton = new JButton("FreeHand");
        freeHandButton.addActionListener(this);

        fontButton = new JButton("Font");
        fontButton.addActionListener(this);


        // Add components to the content pane
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new PaintSurface(), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(colorButton);
        buttonPanel.add(sizeSlider);
        buttonPanel.add(lineButton);
        buttonPanel.add(circleButton);
        buttonPanel.add(ovalButton);
        buttonPanel.add(rectangleButton);
        buttonPanel.add(freeHandButton);
        buttonPanel.add(fontButton);

        contentPane.add(buttonPanel, BorderLayout.NORTH);

        // add chat area
        JPanel chatPanel = new JPanel(new BorderLayout());
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setPreferredSize(new Dimension(800,100));
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        JTextField chatInput = new JTextField();
        chatInput.setPreferredSize(new Dimension(800,50));
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = chatInput.getText();
                System.out.println("sending message to server: " + message);
                // Do something with the message, e.g. send to server
                try {
                    whiteboardApp.sendMessage(message);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                chatInput.setText("");

                // Append new message to chat area
                try{
                    ArrayList<ChatMessage> chatBoard = whiteboardApp.getChatMessages();
                    System.out.println("received messages "+chatBoard);
                    chatArea.setText("");
                    for (ChatMessage chatMessage : chatBoard) {
                        String text = chatMessage.getTimeStamp()+" "+chatMessage.getSender() + ": " + chatMessage.getContent() + "\n";
                        chatArea.append(text);
                    }
                }
                catch (Exception exception){
                    throw new RuntimeException("Updating messages failed, check server status");
                }


            }
        });
        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.add(chatInput, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);
        contentPane.add(chatPanel, BorderLayout.SOUTH);


        // Show the window
        frame.setVisible(true);

    }

    private class PaintSurface extends JComponent {
        ArrayList<MyShape> shapes = new ArrayList<>();
        ArrayList<MyText> texts = new ArrayList<>();
        Point startDrag, endDrag;
        Path2D.Float path = new Path2D.Float();

        public PaintSurface() {
            setLayout(new GridBagLayout());
            // Create the text field


            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    startDrag = new Point(e.getX(), e.getY());
                    endDrag = startDrag;

                    if(Objects.equals(currentShape,"None")){
                        path.moveTo(e.getX(),e.getY());
                    }

                    repaint();
                }

                public void mouseReleased(MouseEvent e) {
                    if (Objects.equals(currentShape, "Line")){
                        Line2D.Float line = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
                        shapes.add(new MyShape(line,currentColor,penSize));
                    } else if (Objects.equals(currentShape, "Circle") || Objects.equals(currentShape, "Oval")) {
                        Ellipse2D.Float circle = makeEllipse(startDrag.x, startDrag.y, e.getX(), e.getY());
                        shapes.add(new MyShape(circle,currentColor,penSize));
                    }
                    else if (Objects.equals(currentShape,"Rectangle")){
                        Shape r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
                        shapes.add(new MyShape(r,currentColor,penSize));
                    }else {
                        shapes.add(new MyShape(path,currentColor,penSize));
                        path=new Path2D.Float();
                    }

                    startDrag = null;
                    endDrag = null;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // check if mouse was clicked twice
                        System.out.println("clicked twice"+e.getX()+" "+e.getY());
                        int x1= e.getX();
                        int y1=e.getY();
                        JDialog dialog = new JDialog();
                        dialog.setTitle("Enter Text");
                        dialog.setSize(300, 150);
                        dialog.setLayout(new BorderLayout());
                        // Add a text field to the JDialog
                        JTextField textField = new JTextField();
                        dialog.add(textField, BorderLayout.CENTER);
                        // Add an OK button to the JDialog
                        JButton okButton = new JButton("OK");
                        okButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // Get the text from the text field and do something with it
                                String text = textField.getText();
                                System.out.println("User entered: " + text);
                                System.out.println(penSize);
                                addText(text,currentColor,penSize,x1,y1);
                                dialog.dispose(); // Close the dialog
                                repaint();
                            }
                        });

                        okButton.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyPressed(KeyEvent e) {
                                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                                    String text = textField.getText();
                                    System.out.println("User entered: " + text);
                                    System.out.println(penSize);
                                    addText(text,currentColor,penSize,x1,y1);
                                    dialog.dispose(); // Close the dialog
                                    repaint();

                                }
                            }
                        });
                        dialog.add(okButton, BorderLayout.SOUTH);

                        // Set the location of the dialog to where the mouse was clicked
                        dialog.setLocation(e.getX(), e.getY());

                        // Show the dialog
                        dialog.setVisible(true);

                    }
                }
            });






            this.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    if (Objects.equals(currentShape, "None")) {
                        // Add a line segment from the previous point to the current point
                        path.lineTo(e.getX(),e.getY());
                    } else {
                        endDrag = new Point(e.getX(), e.getY());
                    }
                    repaint();
                }
            });
        }
        private void paintBackground(Graphics2D g2){
            g2.setPaint(Color.LIGHT_GRAY);
            for (int i = 0; i < getSize().width; i += 10) {
                Shape line = new Line2D.Float(i, 0, i, getSize().height);
                g2.draw(line);
            }

            for (int i = 0; i < getSize().height; i += 10) {
                Shape line = new Line2D.Float(0, i, getSize().width, i);
                g2.draw(line);
            }


        }
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paintBackground(g2);

            g2.setStroke(new BasicStroke(penSize));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

            for (MyShape s : shapes) {
                g2.setPaint(currentColor);
                if (s.getShape() instanceof Rectangle2D.Float) {
                    g2.setPaint(s.getColor());
                    g2.setStroke(new BasicStroke(s.getPenSize()));
                    g2.draw(s.getShape());

                } else if (s.getShape() instanceof Ellipse2D.Float) {
                    g2.setPaint(s.getColor());
                    g2.setStroke(new BasicStroke(s.getPenSize()));
                    g2.draw(s.getShape());

                    //g2.fill(s);
                }
                else if (s.getShape() instanceof Line2D.Float){
                    g2.setPaint(s.getColor());
                    g2.setStroke(new BasicStroke(s.getPenSize()));
                    g2.draw(s.getShape());

                }
                else{
                    g2.setPaint(s.getColor());
                    g2.setStroke(new BasicStroke(s.getPenSize()));
                    g2.draw(s.getShape());
                }
            }
            for (MyText t : texts){
                g2.setPaint(t.getColor());
                g2.setFont(t.getFont());
//                System.out.println("text now is "+t.getText()+"location is "+t.getX1()+""+t.getY1());
                g2.drawString(t.getText(),t.getX1(),t.getY1());
            }

            if (startDrag != null && endDrag != null) {
                g2.setStroke(new BasicStroke(penSize));
                g2.setPaint(currentColor);
                if (Objects.equals(currentShape, "Line")){
                    Shape line = makeLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                    g2.draw(line);
                }
                else if (currentShape == "Circle"||currentShape=="Oval") {
                    Shape r = makeEllipse(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                    g2.draw(r);
                }
                else if (Objects.equals(currentShape,"Rectangle")){
                    Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                    g2.draw(r);
                }else{
                    g2.draw(path);
                }



            }
        }

        private Ellipse2D.Float makeEllipse(int x1, int y1, int x2, int y2) {
            Ellipse2D.Float ellipse = new Ellipse2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
            return ellipse;
        }

        private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
            Rectangle2D.Float rect = new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
            return rect;
        }

        private Line2D.Float makeLine(int x1, int y1, int x2, int y2) {
            Line2D.Float line = new Line2D.Float(x1, y1, x2, y2);
            return line;
        }

        private void addText(String text, Color color, int penSize, int x1, int y1){
            System.out.println(text+" "+x1+" "+y1);
            texts.add(new MyText(text,color,penSize,x1,y1,currentFont));
        }



    }



    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == colorButton) {
            currentColor = JColorChooser.showDialog(null, "Choose a color", currentColor);
            System.out.println(currentColor);
        } else if (source == lineButton) {
            currentShape = "Line";
        } else if (source == circleButton) {
            currentShape = "Circle";
        } else if (source == ovalButton) {
            currentShape = "Oval";
        } else if (source == rectangleButton) {
            currentShape = "Rectangle";
        } else if(source==freeHandButton){
            currentShape = "None";
        } else if (source == fontButton) {
            currentFont = FontChooser.showFontChooser();
            System.out.println(currentFont);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        penSize = sizeSlider.getValue();
    }


}
