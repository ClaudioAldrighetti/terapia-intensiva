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
public class DiagnosisFrame extends JFrame implements ActionListener {

    private final Patient patient;

    private static final String title = "Inserisci diagnosi";
    private final JTextPane diagnosisPane = new JTextPane();
    private final JButton okButton = new JButton("Ok");
    private final JButton cancelButton = new JButton("Annulla");
    private final JScrollPane diagnosisScroll = new JScrollPane(diagnosisPane);
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final JPanel southPanel = new JPanel(new FlowLayout());

    /**
     * @param patient the patient to whom to add the prescription
     */
    public DiagnosisFrame(Patient patient) {

        // Listener
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        this.patient = patient;

        centerPanel.add(diagnosisScroll, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
     * @param s the string to write
     */
    public void setDiagnosis(String s) {
        diagnosisPane.setText(s.trim());
    }

    // Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton)) {
            int i = Model.getInstance().getPatients().indexOf(patient);
            String txt = diagnosisPane.getText();
            Model.getInstance().setDiagnosis(patient.getCf(), txt);
        }
        this.setVisible(false);
    }
}
