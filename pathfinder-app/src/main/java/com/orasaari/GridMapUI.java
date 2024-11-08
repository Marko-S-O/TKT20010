package com.orasaari;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/** 
 * The main class to run pathfinding algorithms.
 * The entry point to the application.
*/
public class GridMapUI extends JFrame implements ActionListener {
  
    private GridMapView view;
    private GridMap map;
    private JButton selectFileButton, loadMapButton;
    private JTextField fileNameTextfield;

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
        coordinatePanel.add(new JTextField(4));
        coordinatePanel.add(new JLabel("  Y:"));
        coordinatePanel.add(new JTextField(4));
        coordinatePanel.add(new JLabel("     Päätepiste X:"));
        coordinatePanel.add(new JTextField(4));
        coordinatePanel.add(new JLabel("  Y:"));
        coordinatePanel.add(new JTextField(4));

        JPanel runPanel = new JPanel();
        runPanel.add(new JRadioButton("A-Star", false));
        runPanel.add(new JRadioButton("JPS", false));
        runPanel.add(new JLabel("        "));
        runPanel.add(new JCheckBox("Visualisoi edistyminen    "));
        runPanel.add(new JButton("           Etsi reitti           "));

        controlPanel.add(getFilePanel()); 
        controlPanel.add(coordinatePanel);
        controlPanel.add(runPanel);

        return controlPanel;
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
        jfc.setCurrentDirectory(new File("."));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == selectFileButton) {
            selectFile();
        } else if(e.getSource() == loadMapButton) {
            loadMap();
        }
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