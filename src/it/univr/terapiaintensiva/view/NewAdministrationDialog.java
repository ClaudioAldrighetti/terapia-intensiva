package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Administration;
import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;

/**
 * A form used to add a new {@link Administration} to the record of the patient.
 */
class NewAdministrationDialog extends JDialog implements ActionListener {

    private static final String title = "Nuova somministrazione";
    private static final JLabel medicineLabel = new JLabel("Farmaco");
    private static final JLabel doseLabel = new JLabel("Dose");
    private static final JLabel dateLabel = new JLabel("Data e ora");
    private static final JLabel notesLabel = new JLabel("Note");
    private final JTextField medicineTextField = new JTextField();
    private final SpinnerNumberModel doseModel = new SpinnerNumberModel(0.0, 0.0, null, 0.1);
    private final SpinnerDateModel dateModel = new SpinnerDateModel();
    private final JButton okButton = new JButton("Ok");
    private final JTextPane notesPane = new JTextPane();
    private final Patient patient;

    /**
     * @param patient the patient to whom to add the administration
     */
    NewAdministrationDialog(Patient patient) {

        super(MonitorFrame.getInstance());
        this.setModal(true);

        this.patient = patient;

        // Listener
        okButton.addActionListener(this);
        JButton cancelButton = new JButton("Annulla");
        cancelButton.addActionListener(this);

        JSpinner doseSpinner = new JSpinner(doseModel);
        doseSpinner.setPreferredSize(new Dimension(60, 27));
        doseSpinner.setMinimumSize(new Dimension(60, 27));

        GridBagConstraints c = new GridBagConstraints();
        c.insets.set(5, 5, 5, 5);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = c.gridy = 0;
        c.weightx = 0.15;
        c.weighty = 0.0;
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(medicineLabel, c);
        c.gridy++;
        centerPanel.add(doseLabel, c);
        c.gridy++;
        centerPanel.add(dateLabel, c);
        c.gridy++;
        centerPanel.add(notesLabel, c);
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 1;
        centerPanel.add(medicineTextField, c);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridy++;
        centerPanel.add(doseSpinner, c);
        c.gridy++;
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy HH:mm"));
        centerPanel.add(dateSpinner, c);
        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 3;
        JScrollPane scrollPane = new JScrollPane(notesPane);
        centerPanel.add(scrollPane, c);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(cancelButton);
        southPanel.add(okButton);

        Container contentPane = this.getContentPane();
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(southPanel, BorderLayout.SOUTH);

        this.setSize(500, 350);
        this.setResizable(false);
        this.setTitle(title);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Reads all the data about the administration
     * @return a new administration
     */
    private Administration getAdministration() {
        Instant instant = Instant.ofEpochMilli(dateModel.getDate().getTime());
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = ldt.toLocalDate();
        LocalTime localTime = ldt.toLocalTime();
        return new Administration(
                medicineTextField.getText().trim(),
                doseModel.getNumber().doubleValue(),
                StringEscapeUtils.escapeCsv(notesPane.getText().trim()),
                localDate,
                localTime
        );
    }

    // Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton))
            Model.getInstance().addAdministration(patient.getCf(), getAdministration());
        this.dispose();
    }
}
