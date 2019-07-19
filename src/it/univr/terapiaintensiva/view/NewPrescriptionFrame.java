package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;
import it.univr.terapiaintensiva.model.Prescription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class NewPrescriptionFrame extends JFrame implements ActionListener {

    private final Patient patient;

    private static final String title = "Nuova prescrizione";
    private static final JLabel medicineLabel = new JLabel("Farmaco");
    private static final JLabel durationLabel = new JLabel("Durata");
    private static final JLabel doseLabel = new JLabel("Dose");
    private static final JLabel nDosesLabel = new JLabel("Numero di dosi");
    private static final JLabel dateLabel = new JLabel("Data");
    private static final JTextField medicineTextField = new JTextField();
    private static final SpinnerNumberModel durationModel = new SpinnerNumberModel();
    private static final JSpinner durationSpinner = new JSpinner(durationModel);
    private static final SpinnerNumberModel doseModel = new SpinnerNumberModel(0.0, 0.0, null, 0.1);
    private static final JSpinner doseSpinner = new JSpinner(doseModel);
    private static final SpinnerNumberModel nDosesModel = new SpinnerNumberModel();
    private static final JSpinner nDosesSpinner = new JSpinner(nDosesModel);
    private static final SpinnerDateModel dateModel = new SpinnerDateModel();
    private static final JSpinner dateSpinner = new JSpinner(dateModel);
    private static final JButton okButton = new JButton("Ok");
    private static final JButton cancelButton = new JButton("Annulla");
    private static final JPanel centerPanel = new JPanel(new GridBagLayout());
    private static final JPanel southPanel = new JPanel(new FlowLayout());
    private static final GridBagConstraints c = new GridBagConstraints();

    public NewPrescriptionFrame(Patient patient) {

        this.patient = patient;

        // Listener
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        durationSpinner.setPreferredSize(new Dimension(60, 27));
        doseSpinner.setPreferredSize(new Dimension(60, 27));
        nDosesSpinner.setPreferredSize(new Dimension(60, 27));

        durationSpinner.setMinimumSize(new Dimension(60, 27));
        doseSpinner.setMinimumSize(new Dimension(60, 27));
        nDosesSpinner.setMinimumSize(new Dimension(60, 27));

        c.insets.set(5, 5, 5, 5);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = c.gridy = 0;
        c.weightx = 0.15;
        c.weighty = 0.0;
        centerPanel.add(medicineLabel, c);
        c.gridy++;
        centerPanel.add(durationLabel, c);
        c.gridy++;
        centerPanel.add(doseLabel, c);
        c.gridy++;
        centerPanel.add(nDosesLabel, c);
        c.gridy++;
        centerPanel.add(dateLabel, c);
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx++;
        centerPanel.add(medicineTextField, c);
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        centerPanel.add(durationSpinner, c);
        c.gridy++;
        centerPanel.add(doseSpinner, c);
        c.gridy++;
        centerPanel.add(nDosesSpinner, c);
        c.gridy++;
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        centerPanel.add(dateSpinner, c);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        southPanel.add(cancelButton);
        southPanel.add(okButton);

        Container contentPane = this.getContentPane();
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(southPanel, BorderLayout.SOUTH);

        this.setSize(580, 270);
        this.setResizable(false);
        this.setTitle(title);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private Prescription getPrescription() {
        Instant instant = Instant.ofEpochMilli(dateModel.getDate().getTime());
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localdate = ldt.toLocalDate();
        return new Prescription(
                localdate,
                durationModel.getNumber().intValue(),
                medicineTextField.getText(),
                nDosesModel.getNumber().intValue(),
                doseModel.getNumber().doubleValue());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton)) {
            Model.getIstance().addPrescription(patient.getCf(), getPrescription());
        }
        this.dispose();
    }
}
