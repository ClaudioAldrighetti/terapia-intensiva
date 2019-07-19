package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Administration;
import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;

/**
 * A form use to add a new {@link Administration} to the file of the patient.
 */
public class NewAdministrationFrame extends JFrame implements ActionListener {

    private static final String title = "Nuova somministrazione";
    private static final JLabel medicineLabel = new JLabel("Farmaco");
    private static final JLabel doseLabel = new JLabel("Dose");
    private static final JLabel dateLabel = new JLabel("Data e ora");
    private static final JLabel notesLabel = new JLabel("Note");
    private static final JTextField medicineTextField = new JTextField();
    private static final SpinnerNumberModel doseModel = new SpinnerNumberModel(0.0, 0.0, null, 0.1);
    private static final JSpinner doseSpinner = new JSpinner(doseModel);
    private static final SpinnerDateModel dateModel = new SpinnerDateModel();
    private static final JSpinner dateSpinner = new JSpinner(dateModel);
    private static final JButton okButton = new JButton("Ok");
    private static final JButton cancelButton = new JButton("Annulla");
    private static final JTextPane notesPane = new JTextPane();
    private static final JScrollPane scrollPane = new JScrollPane(notesPane);
    private static final JPanel centerPanel = new JPanel(new GridBagLayout());
    private static final JPanel southPanel = new JPanel(new FlowLayout());
    private static final GridBagConstraints c = new GridBagConstraints();
    private final Patient patient;

    public NewAdministrationFrame(Patient patient) {

        this.patient = patient;

        // Listener
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        doseSpinner.setPreferredSize(new Dimension(60, 27));
        doseSpinner.setMinimumSize(new Dimension(60, 27));
        //scrollPane.setMinimumSize(new Dimension(60, 27));

        c.insets.set(5, 5, 5, 5);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = c.gridy = 0;
        c.weightx = 0.15;
        c.weighty = 0.0;
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
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy HH:mm"));
        centerPanel.add(dateSpinner, c);
        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 3;
        centerPanel.add(scrollPane, c);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
     * Read all the {@link JTextField}, {@link JSpinner} and {@link JTextPane}
     *
     * @return a new administration
     */
    private Administration getAdministration() {
        Instant instant = Instant.ofEpochMilli(dateModel.getDate().getTime());
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = ldt.toLocalDate();
        LocalTime localTime = ldt.toLocalTime();
        return new Administration(
                localDate,
                localTime,
                medicineTextField.getText(),
                doseModel.getNumber().doubleValue(),
                notesPane.getText()
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton))
            Model.getIstance().addAdministration(patient.getCf(), getAdministration());
        this.dispose();
    }
}
