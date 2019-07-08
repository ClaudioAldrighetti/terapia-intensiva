package it.univr.terapiaintensiva.view;

import javax.swing.*;
import java.awt.*;

public class MonitorPanel extends JPanel {

    GridBagConstraints c = new GridBagConstraints();

    // North panel
    private final JLabel bedNumberLabel = new JLabel();
    private final JLabel nameLabel = new JLabel("Mario Rossi");
    private final JPanel northPanel = new JPanel(new BorderLayout());

    // Center panel
    private final JLabel bpmLabel = new JLabel("60");
    private final JLabel tempLabel = new JLabel("36");
    private final JLabel sbpLabel = new JLabel("120");
    private final JLabel dash = new JLabel("-");
    private final JLabel dbpLabel = new JLabel("80");
    private final JButton tempButton = new JButton(new ImageIcon("./icons/health-thermometer.png"));
    private final JButton pressButton = new JButton(new ImageIcon("./icons/blood-pressure-control-tool.png"));
    private final JButton diagnosisButton = new JButton("Diagnosi");
    private final JButton newPrescriptionButton = new JButton("Nuova prescrizione");
    private final JButton newAdministrationButton = new JButton("Nuova somministrazione");
    private final JButton prescriptionLogButton = new JButton("Log prescrizioni");
    private final JButton administrationLogButton = new JButton("Log somministrazioni");
    private final JPanel northCenterPanel = new JPanel(new GridLayout(1, 2));
    private final JPanel northEastCenterPanel = new JPanel(new GridBagLayout());
    private final JPanel southCenterPanel = new JPanel(new FlowLayout());
    private final JPanel centerPanel = new JPanel(new GridLayout(2, 1));

    public MonitorPanel(int bedNumber) {

        this.setLayout(new BorderLayout());

        // Name and bed in top panel
        bedNumberLabel.setText(String.valueOf(bedNumber));
        bedNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bedNumberLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        nameLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        northPanel.add(bedNumberLabel, BorderLayout.WEST);
        northPanel.add(nameLabel, BorderLayout.CENTER);
        northPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black));
        this.add(northPanel, BorderLayout.NORTH);

        // Center panel
        // Northwest
        bpmLabel.setOpaque(true);
        bpmLabel.setBackground(Color.black);
        bpmLabel.setForeground(Color.green);
        bpmLabel.setFont(new Font("sansserif", Font.PLAIN, 80));
        bpmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        northCenterPanel.add(bpmLabel);

        // Northeast
        sbpLabel.setFont(new Font("sansserif", Font.PLAIN, 17));
        dbpLabel.setFont(new Font("sansserif", Font.PLAIN, 17));
        tempLabel.setFont(new Font("sansserif", Font.PLAIN, 30));
        c.weightx = c.weighty = 1.0;
        c.gridx = c.gridy = 0;
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
        southCenterPanel.add(diagnosisButton);
        southCenterPanel.add(newPrescriptionButton);
        southCenterPanel.add(newAdministrationButton);
        southCenterPanel.add(prescriptionLogButton);
        southCenterPanel.add(administrationLogButton);


        northCenterPanel.add(northEastCenterPanel);
        centerPanel.add(northCenterPanel);
        centerPanel.add(southCenterPanel);
        this.add(centerPanel, BorderLayout.CENTER);

        this.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }
}
