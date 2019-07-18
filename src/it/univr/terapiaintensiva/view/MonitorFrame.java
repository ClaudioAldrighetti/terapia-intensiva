package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MonitorFrame extends JFrame {

    private static final String title = "Terapia intensiva";
    private static final JMenu chiefMenu = new JMenu("Primario");
    private static final JMenu nurseMenu = new JMenu("Infermiere");
    private static final JMenuItem newPatientMenuItem = new JMenuItem("Nuovo paziente");
    private static final JMenuItem dischargeMenuItem = new JMenuItem("Dimetti paziente");
    private static final JMenuItem reportMenuItem = new JMenuItem("Visualizza report");
    private static final JMenuBar menuBar = new JMenuBar();
    private static MonitorFrame istance = null;
    private final ArrayList<MonitorPanel> monitors = new ArrayList<>();
    private final Container contentPane = this.getContentPane();
    private final Model model = Model.getIstance();

    private MonitorFrame(char type) {

        newPatientMenuItem.addActionListener(new NewPatientController());

        contentPane.setLayout(new GridLayout(2, 5));

        for (int i = 0; i < 10; i++) {
            monitors.add(new MonitorPanel(type));
            contentPane.add(monitors.get(i));
            monitors.get(i).setVisible(false);
        }
        for (int i = 0; i < model.getPatients().size(); i++) {
            monitors.get(i).setPatient(model.getPatients().get(i));
        }

        // Menu
        chiefMenu.add(dischargeMenuItem);
        chiefMenu.add(reportMenuItem);
        nurseMenu.add(newPatientMenuItem);
        if (type == Model.CHIEF)
            menuBar.add(chiefMenu);
        else if (type == Model.NURSE)
            menuBar.add(nurseMenu);
        this.setJMenuBar(menuBar);

        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static MonitorFrame getIstance(char type) {
        if (istance == null)
            istance = new MonitorFrame(type);
        return istance;
    }

    public void addPatient(Patient patient) {
        for (int i = 0; i < 10; i++) {
            if (monitors.get(i).isEmpty()) {
                monitors.get(i).setPatient(patient);
                break;
            }
        }
    }
}

