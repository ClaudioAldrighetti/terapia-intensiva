package it.univr.terapiaintensiva.view;

import com.sun.xml.internal.ws.util.StringUtils;
import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * A form in which to put all the informations about a new patient
 */
public class NewPatientFrame extends JFrame implements ActionListener {

    private static final String title = "Nuovo paziente";
    private static final JLabel nameLabel = new JLabel("Nome");
    private static final JLabel surnameLabel = new JLabel("Cognome");
    private static final JLabel dobLabel = new JLabel("Data di nascita");
    private static final JLabel pobLabel = new JLabel("Luogo di nascita");
    private static final JLabel cfLabel = new JLabel("Codice fiscale");
    private final JTextField nameTextField = new JTextField();
    private final JTextField surnameTextField = new JTextField();
    private final SpinnerDateModel dateModel = new SpinnerDateModel();
    private final JSpinner dateSpinner = new JSpinner(dateModel);
    private final JTextField pobTextField = new JTextField();
    private final JTextField cfTextField = new JTextField();
    private final JButton okButton = new JButton("Ok");
    private final JButton cancelButton = new JButton("Annulla");
    private final JPanel centerPanel = new JPanel(new GridBagLayout());
    private final JPanel southPanel = new JPanel(new FlowLayout());
    private final GridBagConstraints c = new GridBagConstraints();

    public NewPatientFrame() {

        // Listener
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        c.insets.set(5, 5, 5, 5);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = c.gridy = 0;
        c.weightx = 0.15;
        c.weighty = 0.0;
        centerPanel.add(nameLabel, c);
        c.gridy++;
        centerPanel.add(surnameLabel, c);
        c.gridy++;
        centerPanel.add(dobLabel, c);
        c.gridy++;
        centerPanel.add(pobLabel, c);
        c.gridy++;
        centerPanel.add(cfLabel, c);
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx++;
        centerPanel.add(nameTextField, c);
        c.gridy++;
        centerPanel.add(surnameTextField, c);
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        centerPanel.add(dateSpinner, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy++;
        centerPanel.add(pobTextField, c);
        c.gridy++;
        centerPanel.add(cfTextField, c);
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

    /**
     * Read all the {@link JTextField} and {@link JSpinner} of the form
     *
     * @return a new patient
     */
    private Patient getPatient() {
        Instant instant = Instant.ofEpochMilli(dateModel.getDate().getTime());
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localdate = ldt.toLocalDate();
        return new Patient(
                StringUtils.capitalize(nameTextField.getText()).trim(),
                StringUtils.capitalize(surnameTextField.getText()).trim(),
                cfTextField.getText().toUpperCase().trim(),
                StringUtils.capitalize(pobTextField.getText()).trim(),
                localdate);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton)) {
            if (Model.getInstance().hospitalizePatient(getPatient()))
                MonitorFrame.getInstance().addPatient(getPatient());
            else
                JOptionPane.showMessageDialog(
                        null,
                        "Il paziente con il codice fiscale inserito è già presente\n" +
                                "oppure è stato raggiunto il numero massimo di posti",
                        "Errore", JOptionPane.ERROR_MESSAGE
                );
        }
        this.dispose();
    }
}
