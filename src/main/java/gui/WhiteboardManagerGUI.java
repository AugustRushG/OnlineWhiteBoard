package gui;

import application.WhiteboardManagerApp;
import constant.PopUpDialog;
import models.Whiteboard;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;


public class WhiteboardManagerGUI implements ActionListener, ChangeListener{
    private final JFrame frame;
    private final JButton colorButton;
    private final JSlider sizeSlider;
    private final JButton rectangleButton,ovalButton,circleButton,lineButton;
    private final JButton clearButton,fileButton,eraserButton,fontButton,freeHandButton;
    private String currentShape = "None";
    private Color currentColor = Color.BLACK;
    private int penSize = 5;
    private Font currentFont = new Font("Arial", Font.PLAIN, 24);
    private final PaintSurface paintSurface;
    private final JList<String> userList;
    private final JTextArea chatArea;
    private final WhiteboardManagerApp whiteboardManagerApp;

    public WhiteboardManagerGUI(WhiteboardManagerApp whiteboardManagerApp) throws RemoteException {
        this.whiteboardManagerApp = whiteboardManagerApp;


        frame = new JFrame();
        frame.setTitle("Whiteboard");

        // Set the size of the window
        frame.setSize(1000, 800);

        // Set the default close operation of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    whiteboardManagerApp.closeRoom();
                } catch (IOException ignored) {
                }
                finally {
                    System.exit(0);
                }
            }
        });

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

        eraserButton = new JButton("Eraser");
        eraserButton.addActionListener(this);

        clearButton = new JButton("Clear All");
        clearButton.addActionListener(this);
        fileButton = new JButton("File");
        fileButton.addActionListener(this);

        // Add components to the content pane
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        paintSurface = new PaintSurface();
        contentPane.add(paintSurface, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(colorButton);
        buttonPanel.add(sizeSlider);
        buttonPanel.add(lineButton);
        buttonPanel.add(circleButton);
        buttonPanel.add(ovalButton);
        buttonPanel.add(rectangleButton);
        buttonPanel.add(freeHandButton);
        buttonPanel.add(fontButton);
        buttonPanel.add(eraserButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(fileButton);

        contentPane.add(buttonPanel, BorderLayout.NORTH);

        // add chat area
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setPreferredSize(new Dimension(800,100));
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        JTextField chatInput = new JTextField();
        chatInput.setPreferredSize(new Dimension(800,50));
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            String message = chatInput.getText();
            System.out.println("sending message to server: " + message);
            // Do something with the message, e.g. send to server
            try {
                whiteboardManagerApp.sendMessage(message);
            } catch (RemoteException ex) {
                popConnectionDialog();
                throw new RuntimeException(ex);
            }
            chatInput.setText("");

        });
        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.add(chatInput, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);
        contentPane.add(chatPanel, BorderLayout.SOUTH);

        try {
            IWhiteboardGUIUpdater whiteboardGUIUpdater = new WhiteboardManagerGUIUpdater(this);
            RemoteObserver remoteObserver = new RemoteObserver(whiteboardGUIUpdater);
            whiteboardManagerApp.registerObserver(remoteObserver);
        } catch (RemoteException e) {
            PopUpDialog.showErrorMessageDialog("Connecting to manager failed, please restart the app then try again",frame);
            throw new RuntimeException(e);
        }


        // user list panel
        JPanel userPanel;
        try{
            userPanel = new JPanel(new BorderLayout());
            JLabel roomLabel = new JLabel("Room" + whiteboardManagerApp.getRoomId());
            roomLabel.setHorizontalAlignment(JLabel.CENTER);
            userPanel.add(roomLabel, BorderLayout.NORTH);
            userList = new JList<>();
            updateUserList(whiteboardManagerApp.getUserInRoom());
            String[] options = {"Kick User"," "};
            userList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        // Double-click detected
                        String selectedUser = (String) userList.getSelectedValue();
                        int result = JOptionPane.showOptionDialog(frame,"Select action to user","Actions",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, options, options[0]);
                        if (result ==0) {
                            try {
                                kickUser(selectedUser);
                            } catch (IOException | NotBoundException e) {
                            }
                        }
                    }
                }
            });
            JScrollPane scrollPane = new JScrollPane(userList); // wrap the JList in a JScrollPane
            userPanel.add(scrollPane, BorderLayout.CENTER);
            userPanel.setPreferredSize(new Dimension(200, 800));
            JPanel userListPanel = new JPanel();
            userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
            userPanel.add(userListPanel, BorderLayout.SOUTH); // add the userListPanel to the SOUTH of userPanel
        } catch (RemoteException e) {
            PopUpDialog.showErrorMessageDialog("create room user list failed please check application status, server status and restart",frame);
            throw new RuntimeException(e);
        }

        contentPane.add(userPanel,BorderLayout.EAST);
        frame.setLocationRelativeTo(null);
        // Show the window
        frame.setVisible(true);
    }

    private class PaintSurface extends JComponent {
        protected ArrayList<MyShape> shapes = new ArrayList<>();
        protected ArrayList<MyText> texts = new ArrayList<>();
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
                        MyShape myShape = new MyShape(line,currentColor,penSize);
                        shapes.add(myShape);

                    }
                    else if (Objects.equals(currentShape, "Circle") || Objects.equals(currentShape, "Oval")) {
                        Ellipse2D.Float circle = makeEllipse(startDrag.x, startDrag.y, e.getX(), e.getY());
                        MyShape myShape = new MyShape(circle,currentColor,penSize);
                        shapes.add(myShape);

                    }
                    else if (Objects.equals(currentShape,"Rectangle")){
                        Shape r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
                        MyShape myShape = new MyShape(r,currentColor,penSize);
                        shapes.add(myShape);

                    }
                    else {
                        MyShape myShape = new MyShape(path,currentColor,penSize);
                        shapes.add(myShape);
                        path=new Path2D.Float();
                    }
                    try {
                        whiteboardManagerApp.sendShape(shapes);
                    } catch (RemoteException ex) {
                        popConnectionDialog();
                        throw new RuntimeException(ex);
                    }
                    startDrag = null;
                    endDrag = null;
                    repaint();
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // check if mouse was clicked twice
                        System.out.println("clicked twice "+e.getX()+" "+e.getY());
                        int x1 = e.getX();
                        int y1 = e.getY();
                        Point parentLocation = frame.getLocationOnScreen();
                        JDialog dialog = new JDialog(frame,"Enter Text",false);
                        dialog.pack();
                        dialog.setSize(300, 150);
//                        dialog.setLayout(new BorderLayout());
                        // Add a text field to the JDialog
                        JTextField textField = new JTextField();
                        dialog.add(textField, BorderLayout.CENTER);
                        // Add an OK button to the JDialog
                        JButton okButton = new JButton("OK");
                        okButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // Get the text from the text field and do something with it
                                String text = textField.getText();
                                if (text.length()>0){
                                    System.out.println("User entered: " + text);
                                    System.out.println(penSize);
                                    try {
                                        addText(text,currentColor,penSize,x1,y1);
                                    } catch (RemoteException ex) {
                                        popConnectionDialog();
                                        throw new RuntimeException(ex);
                                    }
                                    dialog.dispose(); // Close the dialog
                                    repaint();
                                }

                            }
                        });

                        dialog.add(okButton, BorderLayout.SOUTH);

                        // Set the location of the dialog to where the mouse was clicked
                        dialog.setLocation(e.getX()+parentLocation.x, e.getY()+parentLocation.y);
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
                Shape line1 = new Line2D.Float(0, i, getSize().width, i);
                g2.draw(line);
                g2.draw(line1);
            }

        }
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paintBackground(g2);

            g2.setStroke(new BasicStroke(penSize));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

            // iterate over the shapes and remove the ones that intersect with the eraser
            if (startDrag != null && endDrag != null && Objects.equals(currentShape, "Eraser")) {
                Area eraser = new Area(makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y));
                for (int i = shapes.size() - 1; i >= 0; i--) {
                    MyShape s = shapes.get(i);
                    if (s.shape() instanceof Rectangle2D.Float) {
                        if (eraser.intersects(s.shape().getBounds2D())) {
                            shapes.remove(i);
                        }
                    } else if (s.shape() instanceof Ellipse2D.Float) {
                        if (eraser.intersects(s.shape().getBounds2D())) {
                            shapes.remove(i);
                        }
                    } else if (s.shape() instanceof Line2D.Float) {
                        if (eraser.intersects(s.shape().getBounds2D())) {
                            shapes.remove(i);
                        }
                    }
                    else {
                        Area shapeArea = new Area(s.shape());
                        shapeArea.intersect(eraser);
                        if (!shapeArea.isEmpty()) {
                            shapes.remove(i);
                        }
                    }
                }
                try {
                    whiteboardManagerApp.sendShape(shapes);
                } catch (RemoteException e) {
                    popConnectionDialog();
                    throw new RuntimeException(e);
                }
            }
            for (MyShape s : shapes) {
                g2.setPaint(currentColor);
                if (s.shape() instanceof Rectangle2D.Float) {
                    g2.setPaint(s.color());
                    g2.setStroke(new BasicStroke(s.penSize()));
                    g2.draw(s.shape());

                } else if (s.shape() instanceof Ellipse2D.Float) {
                    g2.setPaint(s.color());
                    g2.setStroke(new BasicStroke(s.penSize()));
                    g2.draw(s.shape());

                    //g2.fill(s);
                }
                else if (s.shape() instanceof Line2D.Float){
                    g2.setPaint(s.color());
                    g2.setStroke(new BasicStroke(s.penSize()));
                    g2.draw(s.shape());

                }
                else{
                    g2.setPaint(s.color());
                    g2.setStroke(new BasicStroke(s.penSize()));
                    g2.draw(s.shape());
                }
            }
            for (MyText t : texts){
                g2.setPaint(t.getColor());
                g2.setFont(t.getFont());
                g2.drawString(t.getText(),t.getX1(),t.getY1());
            }

            if (startDrag != null && endDrag != null) {
                g2.setStroke(new BasicStroke(penSize));
                g2.setPaint(currentColor);
                if (Objects.equals(currentShape, "Line")){
                    Shape line = makeLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                    g2.draw(line);
                }
                else if (Objects.equals(currentShape, "Circle") || Objects.equals(currentShape, "Oval")) {
                    Shape r = makeEllipse(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                    g2.draw(r);
                }
                else if (Objects.equals(currentShape,"Rectangle")){
                    Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                    g2.draw(r);
                }
                else{
//                    MyShape myShape = new MyShape(path,currentColor,penSize);
//                    shapes.add(myShape);
//                    try {
//                        whiteboardManagerApp.sendShape(shapes);
//                    } catch (RemoteException e) {
//                        popConnectionDialog();
//                        throw new RuntimeException(e);
//                    }
                    g2.draw(path);
                }
            }

        }
        private Ellipse2D.Float makeEllipse(int x1, int y1, int x2, int y2) {
            if (currentShape.equals("Circle")){
                int diameter = Math.min(Math.abs(x1 - x2), Math.abs(y1 - y2));
                int centerX = Math.min(x1, x2) + diameter / 2;
                int centerY = Math.min(y1, y2) + diameter / 2;
                return new Ellipse2D.Float(centerX - diameter / 2, centerY - diameter / 2, diameter, diameter);
            }
            return new Ellipse2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
        }

        private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
            return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
        }
        private Line2D.Float makeLine(int x1, int y1, int x2, int y2) {
            return new Line2D.Float(x1, y1, x2, y2);
        }
        private void addText(String text, Color color, int penSize, int x1, int y1) throws RemoteException {
            System.out.println(text+" "+x1+" "+y1);
            MyText myText = new MyText(text,color,penSize,x1,y1,currentFont);
            texts.add(myText);
            whiteboardManagerApp.sendText(texts);
        }
        private void clearAll() throws RemoteException {
            shapes = new ArrayList<>();
            texts = new ArrayList<>();
            whiteboardManagerApp.sendShape(shapes);
            whiteboardManagerApp.sendText(texts);
            repaint();
        }
        private void changeShape(ArrayList<MyShape> myShapes){
            shapes = myShapes;
            repaint();
        }
        private void changeText(ArrayList<MyText> myTexts){
            texts = myTexts;
            repaint();
        }
        private ArrayList<MyShape> getShapes(){
            return shapes;
        }
        private ArrayList<MyText> getTexts(){
            return texts;
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
        } else if(source == eraserButton){
            currentShape = "Eraser";
            penSize = 10;
            paintSurface.repaint();
            System.out.println("erasing");
        }else if(source == clearButton) {
            try {
                paintSurface.clearAll();
            } catch (RemoteException ex) {
                popConnectionDialog();
                throw new RuntimeException(ex);
            }
        }else if (source == fileButton){
            int action = popFileDialog();
            if(action == 3){
                try {
                    whiteboardManagerApp.closeRoom();
                    frame.dispose();
                    System.exit(0);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (action == 0) {
                Whiteboard whiteboard = new Whiteboard();
                whiteboard.setTexts(paintSurface.getTexts());
                whiteboard.setShapes(paintSurface.getShapes());
                // Create an ObjectOutputStream with a FileOutputStream to write the whiteboard to a file
                saveWhiteboard();
            } else if (action == 1) {
                loadWhiteboard();
            }
        }
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        penSize = sizeSlider.getValue();
    }
    public void updateChatArea(String text){
        chatArea.append(text);
    }
    public void updateUserList(ArrayList<String> newUserList){
        String[] userArray = newUserList.toArray(new String[0]);
        userList.setListData(userArray);
    }
    public void updateShapeList(ArrayList<MyShape> myShapes){
        paintSurface.changeShape(myShapes);
    }
    public void updateTextList(ArrayList<MyText> myTexts){
        paintSurface.changeText(myTexts);
    }
    public boolean popJoinDialog(String username){
        System.out.println("User "+username +" trying to join the room");
        int option = JOptionPane.showConfirmDialog(frame,
                username + " wants to join room" + ". Do you want to allow this?",
                "Confirm Join Request", JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }
    public void popConnectionDialog(){
        JOptionPane.showMessageDialog(frame,
                "Connection lost. Check Server Status! Closing the application.",
                "Connection Lost",
                JOptionPane.ERROR_MESSAGE);

        // Close the frame
        frame.dispose();
        // exit the program
        System.exit(1);
    }
    public int popFileDialog(){
        String[] options = {"Save Current Whiteboard", "Load Previous Whiteboard", "Close Room"};
        return JOptionPane.showOptionDialog(frame, "Select an option", "Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
    }
    public void kickUser(String username) throws IOException, NotBoundException {
        whiteboardManagerApp.kickUser(username);
    }
    private void saveWhiteboard() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Whiteboard");

        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().endsWith(".ser")) {
                JOptionPane.showMessageDialog(frame, "File name must end with \".ser\".");
                return;
            }

            Whiteboard whiteboard = new Whiteboard();
            whiteboard.setTexts(paintSurface.getTexts());
            whiteboard.setShapes(paintSurface.getShapes());

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                outputStream.writeObject(whiteboard);
                JOptionPane.showMessageDialog(null, "File name saved at "+ fileToSave.getAbsolutePath());
                System.out.println("Whiteboard saved successfully to " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("Error saving whiteboard: " + ex.getMessage());
            }
        }
    }
    private void loadWhiteboard() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Serialized Objects", "ser");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Create an ObjectInputStream with a FileInputStream to read the whiteboard from a file
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(selectedFile));
                Whiteboard whiteboard = (Whiteboard) inputStream.readObject();
                // Close the ObjectInputStream
                inputStream.close();
                // Set the whiteboard on the paint surface
                paintSurface.changeText(whiteboard.getTexts());
                paintSurface.changeShape(whiteboard.getShapes());
                System.out.println("Whiteboard loaded successfully from " + selectedFile.getAbsolutePath());
                try {
                    whiteboardManagerApp.sendShape(whiteboard.getShapes());
                    whiteboardManagerApp.sendText(whiteboard.getTexts());
                } catch (RemoteException ex) {
                    popConnectionDialog();
                    throw new RuntimeException(ex);
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                System.err.println("Error loading whiteboard: " + ex.getMessage());
            }
        }
    }

    public JFrame getFrame(){
        return frame;
    }


}
