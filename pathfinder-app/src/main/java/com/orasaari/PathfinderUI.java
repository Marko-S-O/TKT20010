package com.orasaari;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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
 * The main UI class. Includes functions to 
 * - Run and visualize pathfinding: select and load a map file, select algorithms, find paths and show the results and path on the map
 * - run performance evaluation with a selected scenario file, algorithms and number of iterations & show the results.
*/
public class PathfinderUI extends JFrame implements ActionListener {  
    private GridMapView view;
    private GridMap map;
    private JButton btnSelectFile, btnLoadMap, btnFindPath, btnLoadScenarioFile, btnPerformanceEvalulation;
    private JTextField tfMapFilename, tfStartX, tfStartY, tfFinishX, tfFinishY, tfIterations, tfPerformanceEvaluationFilename;
    private JCheckBox cbDijkstra, cbAstar, cbJPS;
    private ResultPanel pnlResultsDisjkstra, pnlResultsAstar, pnlResultsJPS;
 
    /** 
     * Initialize the UI. 
    */
    PathfinderUI() {
        setLayout(new BorderLayout());
        view = new GridMapView(new GridMap(1024, 1024, 100.0));

        JScrollPane scp = new JScrollPane(view);
        Dimension d = new Dimension(1200, 800);
        scp.setPreferredSize(d);
        scp.setMinimumSize(d);
        add(scp, BorderLayout.CENTER);        
        add(getControlPanel(), BorderLayout.SOUTH);
        add(getResultPanels(), BorderLayout.EAST);

        setTitle("Pathfinder UI v. 1.0");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocation(50,5);
        setVisible(true);
    }

    /**
     * Construct the panel below the map including the control components.
    */
    private JPanel getControlPanel() {
        JPanel pnlControl = new JPanel(new GridLayout(4,1));
        pnlControl.setBorder(BorderFactory.createLineBorder(Color.gray, 1));

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
        pnlControl.add(getPerformanceEvalautionPanel());

        return pnlControl;
    }

    /** 
     * Construct the sub-panel with components to set run parameters and execute pathfinding.
    */
    private JPanel getRunPanel() {
        JPanel pnlRun = new JPanel();
        pnlRun.add(cbDijkstra = new JCheckBox("Dijstra", false));
        pnlRun.add(cbAstar = new JCheckBox("A-Star", false));
        pnlRun.add(cbJPS = new JCheckBox("JPS", false));
        pnlRun.add(new JLabel("        "));
        pnlRun.add(btnFindPath = new JButton("          Find Path          "));
        btnFindPath.addActionListener(this);
        return pnlRun;
    }

    /** 
     * Construct the sub-panel that allows user to select and load a map file.
     */
    private JPanel getFilePanel() {
        JPanel pnlFile = new JPanel();
        pnlFile.add(new JLabel("Filename: "));
        pnlFile.add(tfMapFilename = new JTextField(20));
        pnlFile.add(btnSelectFile = new JButton("Select Map File"));
        btnSelectFile.addActionListener(this);        
        pnlFile.add(btnLoadMap = new JButton("Load Map"));
        btnLoadMap.addActionListener(this);
        return pnlFile;
    }

    /** 
     * Construct the sub-panel showing the result after pathfinding.
    */
    private JPanel getResultPanels() {
        JPanel pnlResults = new JPanel();
        pnlResults.setLayout(new BoxLayout(pnlResults, BoxLayout.Y_AXIS));
        pnlResults.add(pnlResultsDisjkstra = new ResultPanel("Dijkstra"));
        pnlResults.add(pnlResultsAstar = new ResultPanel("A-Star"));
        pnlResults.add(pnlResultsJPS = new ResultPanel("JPS"));
        return pnlResults;
    }

    /**
     * Construct the sub-panel for the performance evaluation.
    */
    private JPanel getPerformanceEvalautionPanel() {
        JPanel pnl = new JPanel();
        pnl.add(new JLabel("Scenario filename: "));
        pnl.add(tfPerformanceEvaluationFilename = new JTextField(20));
        pnl.add(btnLoadScenarioFile = new JButton("Select Scenario File"));
        pnl.add(new JLabel("Iterations: "));
        pnl.add(tfIterations = new JTextField(5));
        pnl.add(btnPerformanceEvalulation = new JButton("Run Performance Evaluation"));        
        btnLoadScenarioFile.addActionListener(this);
        btnPerformanceEvalulation.addActionListener(this);
        return pnl;
    }

    /**
     *  Action method to select the used map file.
    */  
    private void selectMapFile() {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Select a Map File");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setCurrentDirectory(new File(MapUtils.MAP_DIRECTORY));
        int result = jfc.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            String filename = jfc.getSelectedFile().getPath();
            tfMapFilename.setText(filename);
        }
    }

    /** 
     * Action method to select the performance evaluation scenario list file .
    */  
    private void selectScenarioFile() {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Select a Scenario File");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setCurrentDirectory(new File(MapUtils.SCENARIO_DIRECTORY));
        int result = jfc.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            String filename = jfc.getSelectedFile().getPath();
            tfPerformanceEvaluationFilename.setText(filename);
        }
    }

    /** Load the map file using the MapUtils class. */
    private void loadMap() {
        String filename = tfMapFilename.getText();
        GridMap map = new GridMap(filename);
        view.setMap(map);
        this.map = map;
    }

    /**
     * Execute a single pathfinding run with a given algorithm.
     * 
     * @param algorith  Algorithm to be used, values defined in MapUtil.
     *  
     * @return  A results object wrapping necessary information to be visualized, or null in the case of an error.
    */
    private PathfindingResult findPath(int algorith) {

        Pathfinder finder;
        if(algorith == Pathfinder.ALGORITHM_DIJKSTRA) {
            finder = new DijkstraPathfinder();
        } else if(algorith == Pathfinder.ALGORITHM_ASTAR) {
            finder = new AStarPathfinder();
        } else {
            finder = new JPSPathfinder();
        }

        try {
            int startX = Integer.parseInt(tfStartX.getText().trim());
            int startY = Integer.parseInt(tfStartY.getText().trim());
            int goalX = Integer.parseInt(tfFinishX.getText().trim());
            int goalY = Integer.parseInt(tfFinishY.getText().trim());          
            PathfindingResult result = finder.findPath(this.map, startX, startY, goalX, goalY);
            return result;            
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Execute pathgfinding with selected algorithms, start point and goal.
     */
    private void findAllPaths() {

        List<List<Node>> paths = new ArrayList<>(3);

        if(cbDijkstra.isSelected()) {
            PathfindingResult result = findPath(Pathfinder.ALGORITHM_DIJKSTRA);
            pnlResultsDisjkstra.showResult(result);
            paths.add(result.path);
        }

        if(cbAstar.isSelected()) {
            PathfindingResult result = findPath(Pathfinder.ALGORITHM_ASTAR);
            pnlResultsAstar.showResult(result);
            paths.add(result.path);
        }

        if(cbJPS.isSelected()) {
            PathfindingResult result = findPath(Pathfinder.ALGORITHM_JPS);
            pnlResultsJPS.showResult(result);
            paths.add(result.path);
        }
        view.paintPaths(paths);
    }

    /**
     * Action method to run the performance evaluation with the selected algorithms, 
     * number of iterastions and scenario file.
     */
    private void runPerformanceEvaluation() {
        PerformanceEvaluator evaluator = new PerformanceEvaluator();

        int iterations = Integer.parseInt(tfIterations.getText().trim());
        String filename = tfPerformanceEvaluationFilename.getText().trim();

        List<Integer> algorithms = new ArrayList<>(3);
        if(cbDijkstra.isSelected()) {
            algorithms.add(Pathfinder.ALGORITHM_DIJKSTRA);
        }
        if(cbAstar.isSelected()) {
            algorithms.add(Pathfinder.ALGORITHM_ASTAR);
        }
        if(cbJPS.isSelected()) {
            algorithms.add(Pathfinder.ALGORITHM_JPS);
        }

        PerformanceEvaluationDialog dialog = new PerformanceEvaluationDialog(this);
        dialog.showText("Performance evaluation underway, results will be shown here shortly...");

        PerformanceEvaluationResults results = evaluator.runEvaluation(iterations, filename, algorithms); 
        dialog.showResults(results);
    }

    /**
     * The event listener method to handle button clicks.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnSelectFile) {
            selectMapFile();
        } else if(e.getSource() == btnLoadMap) {
            loadMap();
        } else if(e.getSource() == btnFindPath) {
            findAllPaths();
        } else if(e.getSource() == btnPerformanceEvalulation) {
            runPerformanceEvaluation();
        } else if (e.getSource() == btnLoadScenarioFile) {
            selectScenarioFile();
        }               
    }

    /**
     * Run the UI.
     * 
     * @param args  No args in use.
     */
    public static void main(String[] args) {
        new PathfinderUI();
    }
}