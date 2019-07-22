package it.univr.terapiaintensiva.view;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;

/**
 * A simple dialog that shows the reporr passed as a string.
 */
class ReportDialog extends JDialog {

    /**
     * @param report the report to print
     */
    ReportDialog(String report) {
        super(MonitorFrame.getInstance());
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Report");
        JTextArea textArea = new JTextArea(StringEscapeUtils.unescapeCsv(report.replaceAll("\t", "            ")));
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(scrollPane);
        this.setSize(500, 700);
        this.setLocationRelativeTo(null);
    }
}
