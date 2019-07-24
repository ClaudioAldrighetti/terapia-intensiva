package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Dialog in which the user inputs two dates.
 * The dates will be used to get a complete report of the events between those two dates.
 */
class GetReportDialog extends JDialog implements ActionListener {

    private final SpinnerDateModel startModel = new SpinnerDateModel();
    private final SpinnerDateModel endModel = new SpinnerDateModel();
    private final JButton okButton = new JButton("Ok");
    private final JButton cancelButton = new JButton("Annulla");

    GetReportDialog() {
        super(MonitorFrame.getInstance());
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Seleziona periodo");
        this.getContentPane().setLayout(new BorderLayout());
        this.add(new JLabel("Inserire data di inizio e data di fine"), BorderLayout.NORTH);
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new FlowLayout());
        JSpinner startSpinner = new JSpinner(startModel);
        JSpinner endSpinner = new JSpinner(endModel);
        startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "dd/MM/yyyy"));
        endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "dd/MM/yyyy"));
        centralPanel.add(startSpinner);
        centralPanel.add(endSpinner);
        centralPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(centralPanel, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(cancelButton);
        southPanel.add(okButton);
        this.add(southPanel, BorderLayout.SOUTH);
        this.pack();
        this.setLocationRelativeTo(null);

        // Listener
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    @Override

    public void actionPerformed(ActionEvent e) {
        ReportDialog reportDialog;
        if (e.getSource().equals(okButton)) {
            Instant instant = Instant.ofEpochMilli(startModel.getDate().getTime());
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            LocalDate startDate = ldt.toLocalDate();
            instant = Instant.ofEpochMilli(endModel.getDate().getTime());
            ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            LocalDate endDate = ldt.toLocalDate();
            String report = Model.getInstance().getReport(startDate, endDate);
            if (report.length() > 0) {
                reportDialog = new ReportDialog(report);
                reportDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Il report per le date selezionate è vuoto", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
        this.dispose();
    }
}
