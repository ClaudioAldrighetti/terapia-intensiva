package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A form in which to write and modify the diagnosis of a patient.
 */
class DiagnosisDialog extends JDialog implements ActionListener {

    private final Patient patient;

    private static final String title = "Inserisci diagnosi";
    private final JTextPane diagnosisPane = new JTextPane();
    private final JButton okButton = new JButton("Ok");

    /**
     * @param patient the patient to whom to add the prescription
     */
    DiagnosisDialog(Patient patient) {

        super(MonitorFrame.getInstance());
        this.setModal(true);

        // Listener
        okButton.addActionListener(this);
        JButton cancelButton = new JButton("Annulla");
        cancelButton.addActionListener(this);

        this.patient = patient;

        JScrollPane diagnosisScroll = new JScrollPane(diagnosisPane);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(diagnosisScroll, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(cancelButton);
        southPanel.add(okButton);

        Container contentPane = this.getContentPane();
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(southPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setTitle(title);
        this.setPreferredSize(new Dimension(540, 250));
        this.setMinimumSize(new Dimension(540, 250));
        this.setLocationRelativeTo(null);
    }

    /**
     * Writes a String to the JTextPane.
     *
     * @param s the string to write
     */
    void setDiagnosis(String s) {
        diagnosisPane.setText(s.trim());
    }

    // Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton)) {
            String txt = diagnosisPane.getText();
            Model.getInstance().setDiagnosis(patient.getCf(), txt);
        }
        this.setVisible(false);
    }
}
