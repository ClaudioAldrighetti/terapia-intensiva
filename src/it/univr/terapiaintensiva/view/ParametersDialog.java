package it.univr.terapiaintensiva.view;

import it.univr.terapiaintensiva.model.Model;
import it.univr.terapiaintensiva.model.Patient;
import it.univr.terapiaintensiva.model.Vitals;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ParametersDialog extends JDialog {

    public static final int HEARTBEAT = 0;
    public static final int PRESSURE = 1;
    public static final int TEMPERATURE = 2;

    private final Patient patient;
    private final int parameterType;

    public ParametersDialog(Patient patient, String title, int parameterType) {
        super(MonitorFrame.getInstance());
        this.patient = patient;
        this.parameterType = parameterType;
        this.setTitle(title);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        JTable parametersTable = getParametersTable();
        JScrollPane scrollPane = new JScrollPane(parametersTable);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }

    private JTable getParametersTable() {
        ArrayList<Vitals> vitalsList = Model.getInstance().getLastParameters(patient.getCf());
        String[][] rowData;
        String[] columnNames;

        switch (parameterType) {
            case HEARTBEAT:
                this.setMinimumSize(new Dimension(150, 300));
                rowData = new String[vitalsList.size()][2];
                columnNames = new String[2];
                columnNames[0] = "Ora";
                columnNames[1] = "Battito";
                for (int i = 0; i < vitalsList.size(); i++) {
                    rowData[i][0] = String.valueOf(0);
                    rowData[i][1] = String.valueOf(vitalsList.get(i).getHeartBeat());
                }
                break;
            case PRESSURE:
                this.setMinimumSize(new Dimension(250, 300));
                rowData = new String[vitalsList.size()][3];
                columnNames = new String[3];
                columnNames[0] = "Ora";
                columnNames[1] = "Sistolica";
                columnNames[2] = "Diastolica";
                for (int i = 0; i < vitalsList.size(); i++) {
                    rowData[i][0] = String.valueOf(0);
                    rowData[i][1] = String.valueOf(vitalsList.get(i).getSbp());
                    rowData[i][2] = String.valueOf(vitalsList.get(i).getDbp());
                }
                break;
            case TEMPERATURE:
                this.setMinimumSize(new Dimension(200, 300));
                rowData = new String[vitalsList.size()][2];
                columnNames = new String[2];
                columnNames[0] = "Ora";
                columnNames[1] = "Temperatura";
                for (int i = 0; i < vitalsList.size(); i++) {
                    rowData[i][1] = String.valueOf(0);
                    rowData[i][1] = String.valueOf(vitalsList.get(i).getTemperature()).concat(" °C");
                }
                break;
            default:
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
