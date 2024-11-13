package com.orasaari;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/** 
 * The main class to run pathfinding algorithms.
 * The entry point to the application.
*/
public class GridMapUI extends JFrame implements ActionListener {
  
    private static final Font HEADER_FONT = new Font("Verdana", Font.BOLD, 17);
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private GridMapView view;
    private GridMap map;
    private JButton btnSelectFile, btnLoadMap, btnFindPath;
    private JTextField tfFilename, tfStartX, tfStartY, tfFinishX, tfFinishY;
    private JCheckBox cbDijkstra, cbAstar, cbJPS;
    private ResultPanel pnlResultsDisjkstra, pnlResultsAstar, pnlResultsJPS;

    /**
     * A private class to show a single result. One of these is created for each of the algorithms and embedded in the right side of the UI.
     */
    private class ResultPanel extends JPanel {

        private JLabel lblResultHeader, lblStatus, lblAlgorithm, lblStartTime, lblFinishTime, lblDuration, lblPathLenght, lblNodesEvaluated;

        private ResultPanel(String algorith) {
 
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(new JLabel(" "));
            add(new JLabel(" "));
            JPanel pnl1 = new JPanel(new FlowLayout());
            pnl1.add(lblResultHeader = new JLabel(algorith + " result  "));
            lblResultHeader.setFont(HEADER_FONT);
            JPanel pnl2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pnl2.add(new JLabel("Status: "));
            pnl2.add(lblStatus = new JLabel("                                  "));

            JPanel pnl3 = new JPanel(new FlowLayout(FlowLayout.LEFT));        
            pnl3.add(new JLabel("Alogorithm: "));
            pnl3.add(lblAlgorithm = new JLabel("                                  "));
            JPanel pnl4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pnl4.add(new JLabel("Start time: "));
            pnl4.add(lblStartTime = new JLabel("                             "));
            JPanel pnl5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pnl5.add(new JLabel("Finish time: "));
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
            add(pnl1);
            add(pnl2);        
            add(pnl3);
            add(pnl4);
            add(pnl5);
            add(pnl6);
            add(pnl7);
            add(pnl8);
            pack();
        }

        private void showResult(Result result) {
            if(result != null) {
                lblStatus.setText(result.success ? "Success" : "Failed");
                lblAlgorithm.setText(MapUtil.ALGORITHM_NAMES[result.algorithm]);
                lblStartTime.setText(TIMESTAMP_FORMAT.format(new Date(result.startTime)));
                lblFinishTime.setText(TIMESTAMP_FORMAT.format(new Date(result.finishTime)));
                lblDuration.setText(result.duration + " ms");        
                lblPathLenght.setText(String.format("%,.2f", result.distance));
                lblNodesEvaluated.setText("" + result.numeOfEvaluatedNodes);
            } else {
                System.out.println("showResult() can't view result: result is null");
            }
        }
    }


    GridMapUI() {
        setLayout(new BorderLayout());
        view = new GridMapView(new GridMap(1024, 1024));

        JScrollPane scp = new JScrollPane(view);
        Dimension d = new Dimension(1200, 800);
        scp.setPreferredSize(d);
        scp.setMinimumSize(d);
        add(scp, BorderLayout.CENTER);        
        add(getControlPanel(), BorderLayout.SOUTH);
        add(getResultPanels(), BorderLayout.EAST);

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
        pnlRun.add(cbDijkstra = new JCheckBox("Dijstra", false));
        pnlRun.add(cbAstar = new JCheckBox("A-Star", false));
        pnlRun.add(cbJPS = new JCheckBox("JPS", false));
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
    private JPanel getResultPanels() {

        JPanel pnlResults = new JPanel();
        pnlResults.setLayout(new BoxLayout(pnlResults, BoxLayout.Y_AXIS));
        pnlResults.add(pnlResultsDisjkstra = new ResultPanel("Dijkstra"));
        pnlResults.add(pnlResultsAstar = new ResultPanel("A-Star"));
        pnlResults.add(pnlResultsJPS = new ResultPanel("JPS"));

        return pnlResults;

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

    /**
     * Execute pathfinding with given algorithm
     * 
     * @param algorith algorithm to be used, code values defined in MapUtil
     * 
     * @return  results object wrapping necessary return information
    */
    private Result findPath(int algorith) {

        System.out.println("findPath(" + algorith + ")");

        // Temporarily work with Dijkstra only while developing the algorithm
        Pathfinder finder;
        if(algorith == MapUtil.ALGORITHM_DIJKSTRA) {
            finder = new DijkstraPathfinder();
        } else if(algorith == MapUtil.ALGORITHM_ASTAR) {
            finder = new AStartPathfinder();
        } else {
            finder = new JPSPathfinder2();
        }

        try {
            int startX = Integer.parseInt(tfStartX.getText().trim());
            int startY = Integer.parseInt(tfStartY.getText().trim());
            int finishX = Integer.parseInt(tfFinishX.getText().trim());
            int finishY = Integer.parseInt(tfFinishY.getText().trim());
            Point starPoint = new Point(startX, startY);
            Point finishPoint = new Point(finishX, finishY);            
            Result result = finder.navigate(this.map, starPoint, finishPoint);
            return result;            
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Find paths and show results.
     */
    private void findAllPaths() {

        List<List<Node>> paths = new ArrayList<>(3);

        if(cbDijkstra.isSelected()) {
            System.out.print("find Dijkstra");
            Result result = findPath(MapUtil.ALGORITHM_DIJKSTRA);
            pnlResultsDisjkstra.showResult(result);
            paths.add(result.path);
        }

        if(cbAstar.isSelected()) {
            System.out.print("find A*");
            Result result = findPath(MapUtil.ALGORITHM_ASTAR);
            pnlResultsAstar.showResult(result);
            paths.add(result.path);
        }

        if(cbJPS.isSelected()) {
            System.out.print("find JPS");
            Result result = findPath(MapUtil.ALGORITHM_JPS);
            pnlResultsJPS.showResult(result);
            paths.add(result.path);
        }
        view.paintPaths(paths);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnSelectFile) {
            selectFile();
        } else if(e.getSource() == btnLoadMap) {
            loadMap();
        } else if(e.getSource() == btnFindPath) {
            findAllPaths();
        }
    }

    public static void main(String[] args) {
        new GridMapUI();
    }
}