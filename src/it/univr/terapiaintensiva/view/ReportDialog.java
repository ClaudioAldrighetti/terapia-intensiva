package it.univr.terapiaintensiva.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;

/**
 * A simple dialog that shows the reporr passed as a string.
 */
class ReportDialog extends JDialog implements ActionListener {

    private final JTextArea textArea;

    /**
     * @param report the report to print
     */
    ReportDialog(String report) {
        super(MonitorFrame.getInstance());
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Report");
        this.getContentPane().setLayout(new BorderLayout());
        textArea = new JTextArea(report.replaceAll("\t", "            ").replaceAll("&comma", ","));
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(scrollPane, BorderLayout.CENTER);
        JButton printButton = new JButton("Stampa");
        this.add(printButton, BorderLayout.SOUTH);
        this.setSize(500, 700);
        this.setLocationRelativeTo(null);

        printButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) { // Print dialog
        try {
            textArea.print(null, null, true, null, null, false);
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }
}
