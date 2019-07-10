package it.univr.terapiaintensiva.view;

import javax.swing.*;
import java.awt.*;

public class NewPrescriptionFrame extends JFrame {

    private static final String title = "Nuova prescrizione";
    private static final JLabel medicineLabel = new JLabel("Farmaco");
    private static final JLabel durationLabel = new JLabel("Durata");
    private static final JLabel doseLabel = new JLabel("Dose");
    private static final JLabel nDosesLabel = new JLabel("Numero di dosi");
    private static final JLabel dateLabel = new JLabel("Data");
    private static final JTextField medicineTextField = new JTextField();
    private static final SpinnerNumberModel durationModel = new SpinnerNumberModel();
    private static final JSpinner durationSpinner = new JSpinner(durationModel);
    private static final SpinnerNumberModel doseModel = new SpinnerNumberModel();
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

    public NewPrescriptionFrame() {

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
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setVisible(true);
    }
}
