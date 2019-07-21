package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Alarm;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlarmDialog extends JDialog implements ActionListener {

    private final JLabel nameLabel;
    private final JLabel timerLabel;
    private final JButton deactivateButton = new JButton("Spegni");
    private final Patient patient;
    private final Alarm alarm;
    private final int remainingTime;

    public AlarmDialog(Patient patient, Alarm alarm) {
        this.patient = patient;
        this.alarm = alarm;
        nameLabel = new JLabel(patient.getName() + " " + patient.getSurname() + ": " + alarm.getName());
        remainingTime = (4 - alarm.getLevel()) * 60;
        timerLabel = new JLabel(String.valueOf(remainingTime));
        timerLabel.setFont(new Font("sansserif", Font.PLAIN, 50));
        timerLabel.setForeground(Color.red);
        this.getContentPane().setLayout(new BorderLayout());
        this.add(nameLabel, BorderLayout.NORTH);
        this.add(timerLabel, BorderLayout.CENTER);
        this.add(deactivateButton, BorderLayout.SOUTH);
        this.setTitle("Allarme");
        this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(MonitorFrame.getInstance());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
