package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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
    private static final JMenu clinicalRecordsMenu = new JMenu("Cartelle cliniche");
    private static final JMenuItem newPatientMenuItem = new JMenuItem("Nuovo paziente");
    private static final JMenuItem dischargeMenuItem = new JMenuItem("Dimetti paziente");
    private static final JMenuItem reportMenuItem = new JMenuItem("Visualizza report");
    private static final JMenuItem hospitalizedMenuItem = new JMenuItem("Ricoverati");
    private static final JMenuItem dischargedMenuItem = new JMenuItem("Dimessi");
    private static final JMenuBar menuBar = new JMenuBar();
    private static MonitorFrame instance = null;
    private final ArrayList<MonitorPanel> monitors = new ArrayList<>();
    private final Container contentPane = this.getContentPane();
    private final Model model = Model.getInstance();
    private final char type = model.getType();
    private Timer timer = new Timer(100, this);

    private MonitorFrame() {

        // Listener
        newPatientMenuItem.addActionListener(this);
        dischargeMenuItem.addActionListener(this);
        hospitalizedMenuItem.addActionListener(this);
        dischargedMenuItem.addActionListener(this);

        contentPane.setLayout(new GridLayout(2, 5));

        for (int i = 0; i < 10; i++) {
            monitors.add(new MonitorPanel());
            contentPane.add(monitors.get(i));
            monitors.get(i).setVisible(false);
        }
        for (int i = 0; i < model.getHospitalizedPatients().size(); i++) {
            monitors.get(i).setPatient(model.getHospitalizedPatients().get(i));
        }

        // Menu
        chiefMenu.add(dischargeMenuItem);
        chiefMenu.add(reportMenuItem);
        nurseMenu.add(newPatientMenuItem);
        clinicalRecordsMenu.add(hospitalizedMenuItem);
        clinicalRecordsMenu.add(dischargedMenuItem);
        menuBar.setVisible(false);
        if (type != Model.GUEST) {
            if (type == Model.CHIEF)
                menuBar.add(chiefMenu);
            else if (type == Model.NURSE)
                menuBar.add(nurseMenu);
            menuBar.add(clinicalRecordsMenu);
            menuBar.setVisible(true);
        }

        this.setJMenuBar(menuBar);

        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);

        timer.start();

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
     * Adds a new {@link MonitorPanel}.
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

    /**
     * Removes a patient.
     *
     * @param patient the patient to remove
     */
    public void removePatient(Patient patient) {
        for (int i = 0; i < 10; i++) {
            if (monitors.get(i).getPatient().equals(patient)) {
                monitors.get(i).removePatient();
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        NewPatientFrame newPatientFrame;
        DischargeLetterDialog dischargeLetterDialog;
        if (e.getSource().equals(newPatientMenuItem)) {
            newPatientFrame = new NewPatientFrame();
            newPatientFrame.setVisible(true);
        } else if (e.getSource().equals(hospitalizedMenuItem)) {
            JFileChooser fc = new JFileChooser(Model.pathPatients);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    Desktop.getDesktop().open(fc.getSelectedFile());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource().equals(dischargedMenuItem)) {
            JFileChooser fc = new JFileChooser(Model.pathDischarged);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    Desktop.getDesktop().open(fc.getSelectedFile());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource().equals(timer)) {

            for (MonitorPanel monitor : monitors) {
                monitor.updateVitals();
                revalidate();
                if(model.getType() == Model.CHIEF || model.getType() == Model.DOCTOR)
                    monitor.checkAlarms();
            }

            if (model.getType() == Model.CHIEF && monitors.isEmpty())
                chiefMenu.setVisible(false);
            else if (model.getType() == Model.CHIEF && !monitors.isEmpty())
                chiefMenu.setVisible(true);


        } else if (e.getSource().equals(dischargeMenuItem)) {
            int monitorsize = 0;
            for (MonitorPanel m : monitors) {
                if (!m.isEmpty())
                    monitorsize++;
            }
            Object[] names = new Object[monitorsize];
            for (int i = 0; i < monitorsize; i++) {
                names[i] = monitors.get(i).getPatient().getName() +
                        " " + monitors.get(i).getPatient().getSurname() +
                        " " + monitors.get(i).getPatient().getCf();
            }
            String patientString = (String) JOptionPane.showInputDialog(
                    this,
                    "Scegliere il paziente da dimettere",
                    "Dimissioni",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    names,
                    names[0]);
            String[] arr = patientString.split(" ", 3);
            Patient patient = null;
            for (Patient p : model.getHospitalizedPatients()) {
                if (p.getCf().equals(arr[2]))
                    patient = p;
            }
            dischargeLetterDialog = new DischargeLetterDialog(patient);
            dischargeLetterDialog.setVisible(true);
        }
    }
}

