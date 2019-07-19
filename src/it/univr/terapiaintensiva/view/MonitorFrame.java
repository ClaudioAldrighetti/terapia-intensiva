package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The main frame of the application, made using the Singleton pattern. It displays several
 * {@link it.univr.terapiaintensiva.view.MonitorPanel} to perform several operations on patients and to display
 * their status.
 * Its design varies depending on the type of user logged in.
 */
public class MonitorFrame extends JFrame implements ActionListener {

    private static final String title = "Terapia intensiva";
    private static final JMenu chiefMenu = new JMenu("Primario");
    private static final JMenu nurseMenu = new JMenu("Infermiere");
    private static final JMenuItem newPatientMenuItem = new JMenuItem("Nuovo paziente");
    private static final JMenuItem dischargeMenuItem = new JMenuItem("Dimetti paziente");
    private static final JMenuItem reportMenuItem = new JMenuItem("Visualizza report");
    private static final JMenuBar menuBar = new JMenuBar();
    private static MonitorFrame instance = null;
    private final ArrayList<MonitorPanel> monitors = new ArrayList<>();
    private final Container contentPane = this.getContentPane();
    private final Model model = Model.getInstance();
    private final char type = model.getType();

    private MonitorFrame() {

        newPatientMenuItem.addActionListener(this);

        contentPane.setLayout(new GridLayout(2, 5));

        for (int i = 0; i < 10; i++) {
            monitors.add(new MonitorPanel());
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

    /**
     * Used to make this object a Singleton.
     *
     * @return a new instance of this class if there is not one present, or the only instance already present
     */
    public static MonitorFrame getInstance() {
        if (instance == null)
            instance = new MonitorFrame();
        return instance;
    }

    /**
     * Adds a new {@link it.univr.terapiaintensiva.view.MonitorPanel}.
     * @param patient the Patient to link to the monitor
     */
    public void addPatient(Patient patient) {
        for (int i = 0; i < 10; i++) {
            if (monitors.get(i).isEmpty()) {
                monitors.get(i).setPatient(patient);
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NewPatientFrame newPatientFrame;
        if (e.getSource().equals(newPatientMenuItem)) {
            newPatientFrame = new NewPatientFrame();
            newPatientFrame.setVisible(true);
        }
    }
}

