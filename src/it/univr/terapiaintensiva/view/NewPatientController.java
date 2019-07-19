package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewPatientController implements ActionListener {

    private static NewPatientFrame f;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            f = new NewPatientFrame();
            f.setVisible(true);
            f.getOkButton().addActionListener(this);
        } else if ((e.getSource()).equals(f.getOkButton())) {
            Patient p = f.getPatient();
            if (Model.getIstance().hospitalizePatient(p)) {
                MonitorFrame.getIstance().addPatient(p);
            } else {
                JOptionPane.showMessageDialog(null, "Il paziente con il codice fiscale inserito è già presente" +
                        "oppure è stato raggiunto il numero massimo di posti", "Errore", JOptionPane.ERROR_MESSAGE);
            }
            f.resetFields();
            f.dispose();
            f = null;
            MonitorFrame.getIstance().revalidate();
        } else {
            f.dispose();
        }
    }
}
