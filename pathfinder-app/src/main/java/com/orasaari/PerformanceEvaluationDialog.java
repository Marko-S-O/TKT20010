package com.orasaari;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
     * Save results to two CSV files: one for all individual evaluations and one for the summary per algorihm.
     */
    void saveToCsv() {
        MapUtils.saveToCsv(results);
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
