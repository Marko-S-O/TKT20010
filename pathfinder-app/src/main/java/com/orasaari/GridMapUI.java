package com.orasaari;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** 
 * The main class to run pathfinding algorithms.
 * The entry point to the application.
*/
public class GridMapUI extends JFrame implements ActionListener, DocumentListener {
  
    private GridMapView view;
    private GridMap map;
    private JButton selectFileButton, loadMapButton, findPathButton;
    private JTextField fileNameTextfield, startXTexField, startYTextField, finishXTextField, finishYTextField;

    GridMapUI() {
        setLayout(new BorderLayout());
        view = new GridMapView(new GridMap(1024, 1024));

        JScrollPane scp = new JScrollPane(view);
        Dimension d = new Dimension(2000, 1200);
        scp.setPreferredSize(d);
        scp.setMinimumSize(d);
        add(scp, BorderLayout.CENTER);        
        add(getControlPanel(), BorderLayout.SOUTH);

        setTitle("Pathfinder UI v. 0.1");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocation(250,10);
        setVisible(true);
    }

    /**
     * Construct the UI panel that contains all other components but the actual map.
    */
    private JPanel getControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(3,1));

        JPanel coordinatePanel = new JPanel();
        coordinatePanel.add(new JLabel("Lähtöpiste X:"));
        coordinatePanel.add(startXTexField = new JTextField(4));
        coordinatePanel.add(new JLabel("  Y:"));
        coordinatePanel.add(startYTextField = new JTextField(4));
        coordinatePanel.add(new JLabel("     Päätepiste X:"));
        coordinatePanel.add(finishXTextField = new JTextField(4));
        coordinatePanel.add(new JLabel("  Y:"));
        coordinatePanel.add(finishYTextField = new JTextField(4));
        //startXTexField.getDocument().addDocumentListener(this);
        //startYTextField.getDocument().addDocumentListener(this);
        //finishXTextField.getDocument().addDocumentListener(this);
        //finishYTextField.getDocument().addDocumentListener(this);

        controlPanel.add(getFilePanel()); 
        controlPanel.add(coordinatePanel);
        controlPanel.add(getRunPanel());

        return controlPanel;
    }

    private JPanel getRunPanel() {
        JPanel runPanel = new JPanel();
        JRadioButton jbDijstra, jbAstra, jbJps;
        runPanel.add(jbDijstra = new JRadioButton("Dijstra", false));
        runPanel.add(jbAstra = new JRadioButton("A-Star", false));
        runPanel.add(jbJps = new JRadioButton("JPS", false));
        ButtonGroup bg = new ButtonGroup();
        bg.add(jbDijstra);
        bg.add(jbAstra);
        bg.add(jbJps);
        runPanel.add(new JLabel("        "));
        runPanel.add(new JCheckBox("Visualisoi edistyminen    "));
        runPanel.add(findPathButton = new JButton("           Etsi reitti           "));
        findPathButton.addActionListener(this);
        return runPanel;

    }

    /** Construct a sub-panel that allows user to load files */
    private JPanel getFilePanel() {
        JPanel filePanel = new JPanel();
        filePanel.add(new JLabel("Tiedostonimi: "));
        filePanel.add(fileNameTextfield = new JTextField(20));
        filePanel.add(selectFileButton = new JButton("Valitse tiedosto"));
        selectFileButton.addActionListener(this);
        filePanel.add(loadMapButton = new JButton("Lataa kartta"));
        loadMapButton.addActionListener(this);
        return filePanel;
    }

    void setMap(GridMap map) {
        this.map = map;
        view.setMap(map);
    }

    /** Action method to select a map file */  
    private void selectFile() {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Select a MAP file");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setCurrentDirectory(new File("./pathfinder-app/data"));
        int result = jfc.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            String filename = jfc.getSelectedFile().getPath();
            fileNameTextfield.setText(filename);
        }
    }

    private void loadMap() {
        String filename = fileNameTextfield.getText();
        GridMap map = MapUtil.loadMap(filename);
        setMap(map);
    }

    private void findPath() {
        // Temporarily work with Dijkstra only while developing the algorithm
        Pathfinder finder = new DijkstraPathfinder();
        try {
            int startX = Integer.parseInt(startXTexField.getText().trim());
            int startY = Integer.parseInt(startYTextField.getText().trim());
            int finishX = Integer.parseInt(finishXTextField.getText().trim());
            int finishY = Integer.parseInt(finishYTextField.getText().trim());
            Point starPoint = new Point(startX, startY);
            Point finishPoint = new Point(finishX, finishY);
            System.out.println("findPath: ()" + startX + ", " + startY + ") -> (" + finishX + ", " + finishY);
            List<Node> route = finder.navigate(this.map, starPoint, finishPoint);
            view.paintRoute(route);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == selectFileButton) {
            selectFile();
        } else if(e.getSource() == loadMapButton) {
            loadMap();
        } else if(e.getSource() == findPathButton) {
            findPath();

        }
    }

    private void checkCoordinates() {
        try {
            int startX = Integer.parseInt(startXTexField.getText().trim());
            int startY = Integer.parseInt(startYTextField.getText().trim());
            if(startX < map.getWidth() && startY < map.getHeight()) {
                view.paintPixel(startX, startY, GridMap.PIXEL_STATUS_ENDPOINT);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            int finishX = Integer.parseInt(finishXTextField.getText().trim());
            int finishY = Integer.parseInt(finishYTextField.getText().trim());
            if(finishX < map.getWidth() && finishY < map.getHeight()) {
                view.paintPixel(finishX, finishY, GridMap.PIXEL_STATUS_ENDPOINT);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }        
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        checkCoordinates();        
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkCoordinates();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        checkCoordinates();
    }

    public static void main(String[] args) {
        GridMap map = new GridMap(256, 256);
        map.randomize(0.2);
        
        GridMapUI ui = new GridMapUI();
        ui.setMap(map);
        Edge[][][] edges = map.getAdjancencyList();
        System.out.println(edges[0]);
    }


}