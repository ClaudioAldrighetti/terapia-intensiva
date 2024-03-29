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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A form in which to put all the informations about a new patient.
 */
class NewPatientDialog extends JDialog implements ActionListener {

    private static final String title = "Nuovo paziente";
    private static final JLabel nameLabel = new JLabel("Nome");
    private static final JLabel surnameLabel = new JLabel("Cognome");
    private static final JLabel dobLabel = new JLabel("Data di nascita");
    private static final JLabel pobLabel = new JLabel("Luogo di nascita");
    private static final JLabel cfLabel = new JLabel("Codice fiscale");
    private final JTextField nameTextField = new JTextField();
    private final JTextField surnameTextField = new JTextField();
    private final SpinnerDateModel dateModel = new SpinnerDateModel();
    private final JTextField pobTextField = new JTextField();
    private final JTextField cfTextField = new JTextField();
    private final JButton okButton = new JButton("Ok");
    private final Pattern pattern = Pattern.compile("^(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$");

    NewPatientDialog() {

        super(MonitorFrame.getInstance());
        this.setModal(true);

        // Listener
        okButton.addActionListener(this);
        JButton cancelButton = new JButton("Annulla");
        cancelButton.addActionListener(this);

        GridBagConstraints c = new GridBagConstraints();
        c.insets.set(5, 5, 5, 5);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = c.gridy = 0;
        c.weightx = 0.15;
        c.weighty = 0.0;
        JPanel centerPanel = new JPanel(new GridBagLayout());
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
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        centerPanel.add(dateSpinner, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy++;
        centerPanel.add(pobTextField, c);
        c.gridy++;
        centerPanel.add(cfTextField, c);
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
     * Reads all the the data in the form
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

    // Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton)) {       // Ok
            // Check if codice fiscale checks out
            Matcher matcher = pattern.matcher(cfTextField.getText().toUpperCase().trim());
            if (matcher.matches()) {
                if (Model.getInstance().hospitalizePatient(getPatient())) {
                    MonitorFrame.getInstance().addPatient(getPatient());
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Il paziente con il codice fiscale inserito è già presente\n" +
                                    "oppure è stato raggiunto il numero massimo di posti",
                            "Errore", JOptionPane.ERROR_MESSAGE
                    );
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Il codice fiscale non è valido",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {                                    // Cancel
            this.dispose();
        }
    }
}
