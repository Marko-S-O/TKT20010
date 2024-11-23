package com.orasaari;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An UI component class to show a pathfinding result of a single algorithm. 
 * Intended to be used as an embedded component in the GridMapUI class.
 */
class ResultPanel extends JPanel {

    private static final Font HEADER_FONT = new Font("Verdana", Font.BOLD, 17);
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    
    private JLabel lblResultHeader, lblStatus, lblAlgorithm, lblStartTime, lblFinishTime, lblDuration, lblPathLenght, lblNodesEvaluated;

    /**
     * Init the component.
     * 
     * @param algorith  Textual name of the algorithm to be shown in the UI to identify the results.
     */
    ResultPanel(String algorith) {

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
    }

    /**
     * Show the result after a pathginding run.
     * 
     * @param result    Result of a single pathfinding run
     * 
     */
    void showResult(Result result) {
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