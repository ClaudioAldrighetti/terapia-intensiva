package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;
import it.univr.terapiaintensiva.model.Prescription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;

/**
 * A form used to acquire all the data about a new {@link Prescription}.
 */
class NewPrescriptionDialog extends JDialog implements ActionListener {

    private final Patient patient;

    private static final String title = "Nuova prescrizione";
    private static final JLabel medicineLabel = new JLabel("Farmaco");
    private static final JLabel durationLabel = new JLabel("Durata");
    private static final JLabel doseLabel = new JLabel("Dose");
    private static final JLabel nDosesLabel = new JLabel("Numero di dosi");
    private static final JLabel dateLabel = new JLabel("Data");
    private final JTextField medicineTextField = new JTextField();
    private final SpinnerNumberModel durationModel = new SpinnerNumberModel();
    private final SpinnerNumberModel doseModel = new SpinnerNumberModel(0.0, 0.0, null, 0.1);
    private final SpinnerNumberModel nDosesModel = new SpinnerNumberModel();
    private final SpinnerDateModel dateModel = new SpinnerDateModel();
    private final JButton okButton = new JButton("Ok");

    /**
     * @param patient the patient to whom to add the new prescription
     */
    NewPrescriptionDialog(Patient patient) {

        super(MonitorFrame.getInstance());
        this.setModal(true);

        this.patient = patient;

        // Listener
        okButton.addActionListener(this);
        JButton cancelButton = new JButton("Annulla");
        cancelButton.addActionListener(this);

        JSpinner durationSpinner = new JSpinner(durationModel);
        durationSpinner.setPreferredSize(new Dimension(60, 27));
        JSpinner doseSpinner = new JSpinner(doseModel);
        doseSpinner.setPreferredSize(new Dimension(60, 27));
        JSpinner nDosesSpinner = new JSpinner(nDosesModel);
        nDosesSpinner.setPreferredSize(new Dimension(60, 27));

        durationSpinner.setMinimumSize(new Dimension(60, 27));
        doseSpinner.setMinimumSize(new Dimension(60, 27));
        nDosesSpinner.setMinimumSize(new Dimension(60, 27));

        GridBagConstraints c = new GridBagConstraints();
        c.insets.set(5, 5, 5, 5);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = c.gridy = 0;
        c.weightx = 0.15;
        c.weighty = 0.0;
        JPanel centerPanel = new JPanel(new GridBagLayout());
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
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        centerPanel.add(dateSpinner, c);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel southPanel = new JPanel(new FlowLayout());
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

    /**
     * Reads all the {@link JTextField} and {@link JSpinner}
     * @return a new Prescription
     */
    private Prescription getPrescription() {
        Instant instant = Instant.ofEpochMilli(dateModel.getDate().getTime());
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = ldt.toLocalDate();
        LocalTime localTime = LocalTime.now();
        return new Prescription(
                durationModel.getNumber().intValue(),
                medicineTextField.getText().trim(),
                nDosesModel.getNumber().intValue(),
                doseModel.getNumber().doubleValue(),
                localDate,
                localTime
        );
    }

    // Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton))
            Model.getInstance().addPrescription(patient.getCf(), getPrescription());
        this.dispose();
    }
}
