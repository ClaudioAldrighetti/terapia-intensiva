package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Alarm;
import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A dialog that displays ana alarm, the patient name and a stoppable countdown.
 * When the countDown is stopped, the user is required to enter the actions performed to bring the conditions of
 * the patient back to a normal state.
 */

class AlarmDialog extends JDialog implements ActionListener {

    private final JLabel timerLabel;
    private final JButton deactivateButton = new JButton("Spegni");
    private final Patient patient;
    private final Alarm alarm;
    private final Timer timer;
    private int remainingTime;

    /**
     * @param patient the patient that generated the alarm
     * @param alarm   the alarm
     */
    AlarmDialog(Patient patient, Alarm alarm) {
        super(MonitorFrame.getInstance());
        this.patient = patient;
        this.alarm = alarm;
        JLabel nameLabel = new JLabel(patient.getName() + " " + patient.getSurname() + ": " + alarm.getName().toLowerCase());
        nameLabel.setFont(new Font("sansserif", Font.PLAIN, 30));
        remainingTime = (4 - alarm.getLevel()) * 60;
        timerLabel = new JLabel(String.valueOf(remainingTime));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setFont(new Font("sansserif", Font.PLAIN, 50));
        timerLabel.setForeground(Color.red);
        JPanel container = new JPanel();
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.setLayout(new BorderLayout());
        container.add(nameLabel, BorderLayout.NORTH);
        container.add(timerLabel, BorderLayout.CENTER);
        container.add(deactivateButton, BorderLayout.SOUTH);
        this.add(container);
        this.setTitle("Allarme");
        this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(MonitorFrame.getInstance());

        //Listener
        deactivateButton.addActionListener(this);
        timer = new Timer(1000, this);

        timer.start();
    }

    // Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(deactivateButton)) {   // Stop the countdown
            timer.stop();
            if (remainingTime > 0)
                alarm.setStatus(Alarm.ALARM_OFF_INT);
            else
                alarm.setStatus(Alarm.ALARM_OFF_OUTT);
            String actions = JOptionPane.showInputDialog(
                    this,
                    "Quali azioni sono state eseguite\n" +
                            "per riportare il paziente ad uno stato normale?",
                    "Azioni",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (actions != null)
                actions = actions.trim().replaceAll(",", "&comma");
            else
                actions = "Nessuna operazione";
            Model.getInstance().offAlarm(patient.getCf(), this.alarm, actions);
            this.dispose();
        } else {                                        // Countdown
            if (remainingTime > 0) {
                remainingTime--;
                timerLabel.setText(String.valueOf(remainingTime));
            }
        }
    }
}
