package com.orasaari;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/** 
 * A dialog class for displaying the results of a performance evaluation run.
 */
public class PerformanceEvaluationDialog extends JDialog implements ActionListener {

    private JTextArea textArea = new JTextArea(45, 50);
    private JButton saveButton = new JButton("Save to CSV file");
    private JButton closeButton = new JButton("   Close        ");
    private PerformanceEvaluationResults results;

    /**
     * Lay out the UI components and show the dialog.
     * 
     * @param parent    The parent frame, used for setting the location of the dialog.
     */
    PerformanceEvaluationDialog(JFrame parent)  {

        setLayout(new BorderLayout());
        setTitle("Performance Evaluation Results");
        setLocationRelativeTo(parent);
        setLocation(250, 50);

        textArea.setEditable(false);
        textArea.setFont(new Font(textArea.getFont().getName(), textArea.getFont().getStyle(), 17));
        add(textArea, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.add(saveButton);
        southPanel.add(closeButton);
        saveButton.addActionListener(this);
        closeButton.addActionListener(this);
        add(southPanel, BorderLayout.SOUTH);
        add(new JLabel("  "), BorderLayout.WEST);
        add(new JLabel("  "), BorderLayout.EAST);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);        
        setPreferredSize(getPreferredSize());
        pack();
        setVisible(true);
    }

    /**
     * Set the text to be displayed in the main text area of the dialog.
     * 
     * @param text  The text to be shown.
     */
    void showText(String text) {
        textArea.setText(text);
    }

    /**
     * Show the results of a performance evaluation run.
     * 
     * @param results   Results from a single performance evaluation run which can consist multiple algorithms, iterations and scenarios.
    */
    void showResults(PerformanceEvaluationResults results) {
        this.results = results;
        textArea.setText(results.toString());
    }

    /**
     * Write a summary line for a single algorithm to the summary CSV file.
     * 
     * @param writer        The file to be used.
     * @param algorithm     The algorithm id, mapping to MapUtil.ALGORITHM_*
     * @param algorithmName Name of the algorithm to be written to the file.
     * @throws IOException  Thrown automatically in case of I/O errors.
     */

    void writeSummaryLine(FileWriter writer, int algorithm, String algorithmName) throws IOException{
        writer.write("algorithmName," + 
            results.numberOfEvaluations[algorithm] + ',' + 
            results.success[algorithm] + ',' + 
            results.correctDistance[algorithm] + ',' + 
            results.executionTime[algorithm] + ',' + 
            results.executionTime[algorithm] / results.numberOfEvaluations[algorithm] + ',' + 
            results.pathNodes[algorithm] + ',' + 
            results.evaluatedNodes[algorithm] + '\n'); 
    }

    /**
     * Save the performance evaluation results to a CSV files to be used further, for example, in Excel.
     */
    private void saveToCsv() {
        
        // Write detailed results of all evaluations to be used, for example, in Excel.
        try (FileWriter writer = new FileWriter("evaluation_details.csv")) {
            writer.write("Timestamp,Duration,Algorithm,Distance,Success,CorrectDistance,PathNodes,EvaluatedNodes\n");
            for(PerformanceEvaluation evaluation : results.evaluations) {
                writer.write(evaluation.toCsvString() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Write summary lines for each algorithms to another file.
        try (FileWriter writer = new FileWriter("evaluation_summary.csv")) {
            writer.write("Algorithm,NumberOfEvaluations,Success,CorrectDistance,TotalTime,AverageTime,AveragePathNodes,AverageEvaluatedNodes\n");
            if(results.numberOfEvaluations[MapUtil.ALGORITHM_DIJKSTRA] > 0) {
                writeSummaryLine(writer, MapUtil.ALGORITHM_DIJKSTRA, "Dijkstra");
            } 
            if(results.numberOfEvaluations[MapUtil.ALGORITHM_ASTAR] > 0) {
                writeSummaryLine(writer, MapUtil.ALGORITHM_ASTAR, "A*");
            }
            if(results.numberOfEvaluations[MapUtil.ALGORITHM_JPS] > 0) {
                writeSummaryLine(writer, MapUtil.ALGORITHM_JPS, "JPS");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }        
        JOptionPane.showConfirmDialog(
            this,
            "CSV files evaluations.csv and evaluation_summary.csv have been saved to the current folder.",
            "Saved",
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * An event handler for the button clicks.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == saveButton) {
            saveToCsv();
        } else if(e.getSource() == closeButton) {
            dispose();
        }
    }

}
