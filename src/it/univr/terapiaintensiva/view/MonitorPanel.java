package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Alarm;
import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;
import it.univr.terapiaintensiva.model.Vitals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * A panel representing a patient.
 */
class MonitorPanel extends JPanel implements ActionListener {

    private static final JLabel dash = new JLabel("-");
    private final Model model = Model.getInstance();
    private final JLabel nameLabel = new JLabel();
    private final JButton bpmLabel = new JButton("60");
    private final JLabel tempLabel = new JLabel("36");
    private final JLabel sbpLabel = new JLabel("120");
    private final JLabel dbpLabel = new JLabel("80");
    private final JButton tempButton = new JButton(new ImageIcon("./icons/health-thermometer.png"));
    private final JButton pressButton = new JButton(new ImageIcon("./icons/blood-pressure-control-tool.png"));
    private final JButton diagnosisButton = new JButton("Diagnosi");
    private final JButton newPrescriptionButton = new JButton("Nuova prescrizione");
    private final JButton newAdministrationButton = new JButton("Nuova somministrazione");
    private Patient patient = null;

    MonitorPanel() {

        // Listener
        diagnosisButton.addActionListener(this);
        newPrescriptionButton.addActionListener(this);
        newAdministrationButton.addActionListener(this);
        bpmLabel.addActionListener(this);
        pressButton.addActionListener(this);
        tempButton.addActionListener(this);

        this.setLayout(new BorderLayout());

        // Name in top panel
        nameLabel.setText("-");
        nameLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("sansserif", Font.PLAIN, 20));
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(nameLabel, BorderLayout.CENTER);
        northPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black));
        this.add(northPanel, BorderLayout.NORTH);

        // Center panel
        // Northwest
        bpmLabel.setOpaque(true);
        bpmLabel.setBackground(Color.black);
        bpmLabel.setForeground(Color.green);
        bpmLabel.setFont(new Font("sansserif", Font.PLAIN, 50));
        bpmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bpmLabel.setBorderPainted(false);
        JPanel northCenterPanel = new JPanel(new GridLayout(1, 2));
        northCenterPanel.add(bpmLabel);

        // Northeast
        sbpLabel.setFont(new Font("sansserif", Font.PLAIN, 17));
        dbpLabel.setFont(new Font("sansserif", Font.PLAIN, 17));
        tempLabel.setFont(new Font("sansserif", Font.PLAIN, 30));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridx = c.gridy = 0;
        JPanel northEastCenterPanel = new JPanel(new GridBagLayout());
        northEastCenterPanel.add(tempButton, c);
        c.gridy++;
        northEastCenterPanel.add(pressButton, c);
        c.gridx++;
        northEastCenterPanel.add(sbpLabel, c);
        c.gridx++;
        northEastCenterPanel.add(dash, c);
        c.gridx++;
        northEastCenterPanel.add(dbpLabel, c);
        c.gridy--;
        c.gridx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        northEastCenterPanel.add(tempLabel, c);
        northEastCenterPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black));

        // South
        JPanel southCenterPanel = new JPanel(new FlowLayout());
        southCenterPanel.add(diagnosisButton);
        southCenterPanel.add(newPrescriptionButton);
        southCenterPanel.add(newAdministrationButton);

        northCenterPanel.add(northEastCenterPanel);
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(northCenterPanel);
        centerPanel.add(southCenterPanel);
        this.add(centerPanel, BorderLayout.CENTER);

        this.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        // Change menu depending on user type
        char type = model.getType();
        switch (type) {
            case Model.GUEST:
                diagnosisButton.setVisible(false);
                newPrescriptionButton.setVisible(false);
                newAdministrationButton.setVisible(false);
                break;
            case Model.DOCTOR:
            case Model.CHIEF:
                newAdministrationButton.setVisible(false);
                break;
            case Model.NURSE:
                diagnosisButton.setVisible(false);
                newPrescriptionButton.setVisible(false);
                break;
        }
    }

    /**
     * Checks if the panel is not associated with a patient.
     *
     * @return true if the panel is not associated with any patient, false if it is
     */
    boolean isEmpty() {
        return (this.patient == null);
    }

    /**
     * Associates the panel with a patient.
     * @param patient the patient to associate
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
        this.nameLabel.setText(patient.getName() + " " + patient.getSurname());
        this.setVisible(true);
    }

    /**
     * returns the patient associated with the panel
     * @return the patient
     */
    public Patient getPatient() {
        return this.patient;
    }

    /**
     * Removes the association between the panel and the patient stored
     */
    void removePatient() {
        this.patient = null;
        this.setVisible(false);
    }

    /**
     * Updates the vitals labels
     */
    void updateVitals() {
        if (patient != null) {
            Vitals vitals = model.getCurrentVitals(patient.getCf());
            this.bpmLabel.setText(String.valueOf(vitals.getHeartBeat()));
            this.dbpLabel.setText(String.valueOf(vitals.getDbp()));
            this.sbpLabel.setText(String.valueOf(vitals.getSbp()));
            this.tempLabel.setText(String.valueOf(vitals.getTemperature()));
            this.revalidate();
        }
    }

    /**
     * Dhecks if there are any new alarms associated with the patient and displays them using a {@link AlarmDialog}.
     */
    void checkAlarms() {
        AlarmDialog alarmDialog;
        if (this.patient != null) {
            ArrayList<Alarm> alarms = model.checkNewAlarms(patient.getCf());
            for (Alarm alarm : alarms) {
                alarmDialog = new AlarmDialog(patient, alarm);
                alarmDialog.setVisible(true);
            }
        }
    }

    // Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        DiagnosisDialog diagnosisDialog;
        NewPrescriptionDialog newPrescriptionDialog;
        NewAdministrationDialog newAdministrationDialog;
        ParametersDialog parametersDialog;
        if (e.getSource().equals(diagnosisButton)) {                // Diagnosis
            diagnosisDialog = new DiagnosisDialog(this.patient);
            diagnosisDialog.setDiagnosis(patient.getDiagnosis());
            diagnosisDialog.setVisible(true);
        } else if (e.getSource().equals(newPrescriptionButton)) {   // Prescription
            newPrescriptionDialog = new NewPrescriptionDialog(this.patient);
            newPrescriptionDialog.setVisible(true);
        } else if (e.getSource().equals(newAdministrationButton)) { // Administration
            newAdministrationDialog = new NewAdministrationDialog(this.patient);
            newAdministrationDialog.setVisible(true);
        } else if (e.getSource().equals(bpmLabel)) {                // Hearbeat log
            parametersDialog = new ParametersDialog(this.patient, ParametersDialog.HEARTBEAT);
            parametersDialog.setVisible(true);
        } else if (e.getSource().equals(pressButton)) {             // Pressure log
            parametersDialog = new ParametersDialog(this.patient, ParametersDialog.PRESSURE);
            parametersDialog.setVisible(true);
        } else if (e.getSource().equals(tempButton)) {              // Temperatire log
            parametersDialog = new ParametersDialog(this.patient, ParametersDialog.TEMPERATURE);
            parametersDialog.setVisible(true);
        }
    }
}
