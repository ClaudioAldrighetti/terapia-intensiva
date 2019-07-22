package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;
import it.univr.terapiaintensiva.model.VitalsLog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Dialog used to show the last vitals of a patient
 */
public class ParametersDialog extends JDialog {

    public static final int HEARTBEAT = 0;
    public static final int PRESSURE = 1;
    public static final int TEMPERATURE = 2;

    private final Patient patient;
    private final int parameterType;

    /**
     * @param patient       the patient from which to pick the parameters
     * @param parameterType one of the three contants
     *                      <ul>
     *                      <li><code>ParametersDialog.HEARTBEAT</code> to display the hearbeat log</li>
     *                      <li><code>ParametersDialog.PRESSURE</code> to display the pressure log</li>
     *                      <li><code>ParametersDialog.TEMPERATURE</code> to display the temperature log</li>
     *                      </ul>
     */
    public ParametersDialog(Patient patient, int parameterType) {
        super(MonitorFrame.getInstance());
        this.patient = patient;
        this.parameterType = parameterType;
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        JTable parametersTable = getParametersTable();
        JScrollPane scrollPane = new JScrollPane(parametersTable);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }

    /**
     *
     * @return The correct JTable
     */
    private JTable getParametersTable() {
        ArrayList<VitalsLog> vitalsList = Model.getInstance().getLastParameters(patient.getCf());
        String[][] rowData;
        String[] columnNames;

        switch (parameterType) {
            case HEARTBEAT:
                this.setTitle("Battito");
                this.setMinimumSize(new Dimension(150, 300));
                rowData = new String[vitalsList.size()][2];
                columnNames = new String[2];
                columnNames[0] = "Ora";
                columnNames[1] = "Battito";
                for (int i = 0; i < vitalsList.size(); i++) {
                    rowData[i][0] = String.valueOf(vitalsList.get(i).getTime());
                    rowData[i][1] = String.valueOf(vitalsList.get(i).getHeartBeat());
                }
                break;
            case PRESSURE:
                this.setTitle("Pressione");
                this.setMinimumSize(new Dimension(250, 300));
                rowData = new String[vitalsList.size()][3];
                columnNames = new String[3];
                columnNames[0] = "Ora";
                columnNames[1] = "Sistolica";
                columnNames[2] = "Diastolica";
                for (int i = 0; i < vitalsList.size(); i++) {
                    rowData[i][0] = String.valueOf(vitalsList.get(i).getTime());
                    rowData[i][1] = String.valueOf(vitalsList.get(i).getSbp());
                    rowData[i][2] = String.valueOf(vitalsList.get(i).getDbp());
                }
                break;
            case TEMPERATURE:
                this.setTitle("Temperatura");
                this.setMinimumSize(new Dimension(200, 300));
                rowData = new String[vitalsList.size()][2];
                columnNames = new String[2];
                columnNames[0] = "Ora";
                columnNames[1] = "Temperatura";
                for (int i = 0; i < vitalsList.size(); i++) {
                    rowData[i][0] = String.valueOf(vitalsList.get(i).getTime());
                    rowData[i][1] = String.valueOf(vitalsList.get(i).getTemperature()).concat(" °C");
                }
                break;
            default:
                this.setTitle("---");
                this.setMinimumSize(new Dimension(250, 300));
                rowData = new String[vitalsList.size()][3];
                columnNames = new String[3];
                columnNames[0] = "Ora";
                columnNames[1] = "-";
                columnNames[2] = "-";
                for (int i = 0; i < vitalsList.size(); i++) {
                    rowData[i][0] = String.valueOf(0);
                    rowData[i][1] = String.valueOf(0);
                    rowData[i][2] = String.valueOf(0);
                }
        }

        return new JTable(rowData, columnNames);
    }
}
