package com.orasaari;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
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

/** 
 * The main class to run pathfinding algorithms.
 * The entry point to the application.
*/
public class GridMapUI extends JFrame implements ActionListener {
  
    private static final Font HEADER_FONT = new Font("Verdana", Font.BOLD, 20);
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private GridMapView view;
    private GridMap map;
    private JButton btnSelectFile, btnLoadMap, btnFindPath;
    private JTextField tfFilename, tfStartX, tfStartY, tfFinishX, tfFinishY;
    private JLabel lblResultHeader, lblStatus, lblAlgorithm, lblStartTime, lblFinishTime, lblDuration, lblPathLenght, lblNodesEvaluated;

    GridMapUI() {
        setLayout(new BorderLayout());
        view = new GridMapView(new GridMap(1024, 1024));

        JScrollPane scp = new JScrollPane(view);
        Dimension d = new Dimension(2200, 1250);
        scp.setPreferredSize(d);
        scp.setMinimumSize(d);
        add(scp, BorderLayout.CENTER);        
        add(getControlPanel(), BorderLayout.SOUTH);
        add(getResultPanel(), BorderLayout.EAST);

        setTitle("Pathfinder UI v. 0.1");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocation(50,5);
        setVisible(true);
    }

    /**
     * Construct the UI panel that contains all other components but the actual map.
    */
    private JPanel getControlPanel() {
        JPanel pnlControl = new JPanel(new GridLayout(3,1));

        JPanel pnlCoordinates = new JPanel();
        pnlCoordinates.add(new JLabel("Start X:"));
        pnlCoordinates.add(tfStartX = new JTextField(4));
        pnlCoordinates.add(new JLabel("  Y:"));
        pnlCoordinates.add(tfStartY = new JTextField(4));
        pnlCoordinates.add(new JLabel("     Finish X:"));
        pnlCoordinates.add(tfFinishX = new JTextField(4));
        pnlCoordinates.add(new JLabel("  Y:"));
        pnlCoordinates.add(tfFinishY = new JTextField(4));

        pnlControl.add(getFilePanel()); 
        pnlControl.add(pnlCoordinates);
        pnlControl.add(getRunPanel());

        return pnlControl;
    }

    /** 
     * Construct the panel with components to set run parameters and execute pathfinding.
    */
    private JPanel getRunPanel() {
        JPanel pnlRun = new JPanel();
        JRadioButton jbDijstra, jbAstra, jbJps;
        pnlRun.add(jbDijstra = new JRadioButton("Dijstra", false));
        pnlRun.add(jbAstra = new JRadioButton("A-Star", false));
        pnlRun.add(jbJps = new JRadioButton("JPS", false));
        ButtonGroup bg = new ButtonGroup();
        bg.add(jbDijstra);
        bg.add(jbAstra);
        bg.add(jbJps);
        pnlRun.add(new JLabel("        "));
        pnlRun.add(new JCheckBox("Visualize Progress    "));
        pnlRun.add(btnFindPath = new JButton("           Find Path           "));
        btnFindPath.addActionListener(this);
        return pnlRun;

    }

    /** 
     * Construct the sub-panel that allows user to select and load a map file
     */
    private JPanel getFilePanel() {
        JPanel pnlFile = new JPanel();
        pnlFile.add(btnSelectFile = new JButton("Select File"));
        btnSelectFile.addActionListener(this);
        pnlFile.add(new JLabel("Filename: "));
        pnlFile.add(tfFilename = new JTextField(20));
        pnlFile.add(btnLoadMap = new JButton("Load Map"));
        btnLoadMap.addActionListener(this);
        return pnlFile;
    }

    /** 
     * Construct the panel showing the result after pathfinding.
    */
    private JPanel getResultPanel() {
        JPanel pnlResult = new JPanel(new GridBagLayout());
        JPanel pnlInner = new JPanel();
        pnlInner.setLayout(new BoxLayout(pnlInner, BoxLayout.Y_AXIS));
        JPanel pnl1 = new JPanel();
        pnl1.add(lblResultHeader = new JLabel(" Pahtfinding result  "));
        JPanel pnl2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl2.add(new JLabel("Status: "));
        pnl2.add(lblStatus = new JLabel("                                  "));
        lblResultHeader.setFont(HEADER_FONT);
        JPanel pnl3 = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        pnl3.add(new JLabel("Alogorithm: "));
        pnl3.add(lblAlgorithm = new JLabel("                                  "));
        JPanel pnl4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl4.add(new JLabel("Start time: "));
        pnl4.add(lblStartTime = new JLabel("                             "));
        JPanel pnl5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl5.add(new JLabel("Finishing time: "));
        pnl5.add(lblFinishTime = new JLabel("                             "));
        JPanel pnl6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl6.add(new JLabel("Time used: "));
        pnl6.add(lblDuration = new JLabel("                              "));        
        JPanel pnl7 = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        pnl7.add(new JLabel("Path lenght: "));
        pnl7.add(lblPathLenght = new JLabel("                              "));  
        JPanel pnl8 = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        pnl8.add(new JLabel("Nodes visited: "));
        pnl8.add(lblNodesEvaluated = new JLabel("                              "));        
        pnlInner.add(pnl1);
        pnlInner.add(pnl2);        
        pnlInner.add(pnl3);
        pnlInner.add(pnl4);
        pnlInner.add(pnl5);
        pnlInner.add(pnl6);
        pnlInner.add(pnl7);
        pnlInner.add(pnl8);
        pnlResult.add(pnlInner);
        return pnlResult;
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
            tfFilename.setText(filename);
        }
    }

    private void loadMap() {
        String filename = tfFilename.getText();
        GridMap map = MapUtil.loadMap(filename);
        setMap(map);
    }

    private void findPath() {
        // Temporarily work with Dijkstra only while developing the algorithm
        Pathfinder finder = new DijkstraPathfinder();
        try {
            int startX = Integer.parseInt(tfStartX.getText().trim());
            int startY = Integer.parseInt(tfStartY.getText().trim());
            int finishX = Integer.parseInt(tfFinishX.getText().trim());
            int finishY = Integer.parseInt(tfFinishY.getText().trim());
            Point starPoint = new Point(startX, startY);
            Point finishPoint = new Point(finishX, finishY);            
            Result result = finder.navigate(this.map, starPoint, finishPoint);
            view.paintPath(result.path);
            showResult(result);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void showResult(Result result) {
        lblStatus.setText(result.success ? "Success" : "Failed");
        lblAlgorithm.setText(MapUtil.ALGORITHM_NAMES[result.algorithm]);
        lblStartTime.setText(TIMESTAMP_FORMAT.format(new Date(result.startTime)));
        lblFinishTime.setText(TIMESTAMP_FORMAT.format(new Date(result.finishTime)));
        lblDuration.setText(result.duration + " ms");        
        lblPathLenght.setText(String.format("%,.2f", result.distance));
        lblNodesEvaluated.setText("" + result.numeOfEvaluatedNodes);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnSelectFile) {
            selectFile();
        } else if(e.getSource() == btnLoadMap) {
            loadMap();
        } else if(e.getSource() == btnFindPath) {
            findPath();

        }
    }

    /*
    private void checkCoordinates() {
        try {
            int startX = Integer.parseInt(tfStartX.getText().trim());
            int startY = Integer.parseInt(tfStartY.getText().trim());
            if(startX < map.getWidth() && startY < map.getHeight()) {
                view.paintPixel(startX, startY, GridMap.PIXEL_STATUS_ENDPOINT);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            int finishX = Integer.parseInt(tfFinishX.getText().trim());
            int finishY = Integer.parseInt(tfFinishY.getText().trim());
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
    */

    public static void main(String[] args) {
        //GridMap map = new GridMap(1000,  600);
        //map.randomize(0.3);
        
        GridMapUI ui = new GridMapUI();
        //ui.setMap(map);
        //Edge[][][] edges = map.getAdjancencyList();
        //System.out.println(edges[0]);
    }
}