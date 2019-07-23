package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A dialog used to write and/or print the discharge letter for the patient.
 */
class DischargeLetterDialog extends JDialog implements ActionListener {

    private JButton okButton = new JButton("Ok");
    private JTextPane textPane = new JTextPane();
    private Patient patient;

    /**
     * @param patient the patient to whom to add the discharge letter
     */
    DischargeLetterDialog(Patient patient) {

        super(MonitorFrame.getInstance());
        this.patient = patient;

        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        this.setTitle("Lettera di dimissioni");
        JScrollPane scrollPane = new JScrollPane(textPane);
        this.add(scrollPane, BorderLayout.CENTER);
        JButton cancelButton = new JButton("Annulla");
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(cancelButton);
        panel.add(okButton);
        this.add(panel, BorderLayout.SOUTH);
        this.setMinimumSize(new Dimension(500, 600));
        this.setLocationRelativeTo(MonitorFrame.getInstance());

        // Listener
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    // Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton)) {           // Sets the letter and discharges the patient
            MonitorFrame.getInstance().removePatient(patient);
            Model.getInstance().dischargePatient(patient.getCf(), textPane.getText());
            this.dispose();
        } else {
            this.dispose();
        }
    }
}
