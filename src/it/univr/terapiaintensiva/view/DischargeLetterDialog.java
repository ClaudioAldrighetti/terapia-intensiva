package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;

public class DischargeLetterDialog extends JDialog implements ActionListener {

    private JButton okButton = new JButton("Ok");
    private JTextPane textPane = new JTextPane();
    private JButton printButton = new JButton("Stampa");
    private Patient patient;

    public DischargeLetterDialog(Patient patient) {

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
        panel.add(printButton);
        this.add(panel, BorderLayout.SOUTH);
        this.setMinimumSize(new Dimension(500, 600));
        this.setLocationRelativeTo(MonitorFrame.getInstance());

        // Listener
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        printButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton)) {
            MonitorFrame.getInstance().removePatient(patient);
            Model.getInstance().dischargePatient(patient.getCf(), textPane.getText());
            this.dispose();
        } else if (e.getSource().equals(printButton)) {
            try {
                textPane.print(null, null, true, null, null, false);
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        } else {
            this.dispose();
        }
    }
}
