package it.univr.terapiaintensiva.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Model {

    // Paths
    private final String pathAutenticationFile = "../../assets/login.csv";
    private final String pathPatients = "../../patients/";

    // Macros
    private final int maxPatients = 10;

    private ArrayList<Patient> patients;
    private BufferedReader autenticationFile;

    // Constructor
    public Model() {
        patients = new ArrayList<>();
        autenticationFile = null;
        //TODO
    }

    // UC1
    public char autenticate(String username, String password) throws IOException {
        try {
            autenticationFile = new BufferedReader(new FileReader(pathAutenticationFile));
            String record;
            // Check if there is a record
            while ((record = autenticationFile.readLine()) != null) {
                String[] recordData = record.split(",");

                // Get record username and password
                String recordUsr = recordData[0];
                String recordPsw = recordData[1];

                // Check with sing in data
                if ( recordUsr.equals(username) && recordPsw.equals(password) ){
                    autenticationFile.close();
                    // Autentication complete: return user type
                    return (recordData[2].toCharArray())[0];
                }
            }
            // Wrong username and/or password
            autenticationFile.close();
            return 'w';

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Return error
            return 'e';
        }
    }

    // UC2
    public boolean createPatient(Patient patient) throws IOException {
        // Check if there is a free bed
        if(patients.size() >= maxPatients)
            return false;

        else{
            // Add new patient to list
            patients.add(patient);

            // Create new patient's medical records
            String pathNewPatient = pathPatients.concat(patient.getCf());
            Files.createDirectory(Paths.get(pathNewPatient));

            // Create vitals log file
            String pathVitals = pathNewPatient.concat("/vitals.csv");
            Files.createFile(Paths.get(pathVitals));

            // Write vitals on log file
            FileWriter csvVitalsWriter = new FileWriter(pathVitals);

            ArrayList<String> csvVitalsData = new ArrayList<>();
            csvVitalsData.add(String.valueOf(patient.getVitals().getHeartBeat()));
            csvVitalsData.add(String.valueOf(patient.getVitals().getTemperature()));
            csvVitalsData.add(String.valueOf(patient.getVitals().getSbp()));
            csvVitalsData.add(String.valueOf(patient.getVitals().getDbp()));

            csvVitalsWriter.append(String.join(",", csvVitalsData));
            csvVitalsWriter.append("\n");

            csvVitalsWriter.flush();
            csvVitalsWriter.close();

            return true;
        }
    }

    // UC3
    public void addDiagnosis(String cf, String diagnosis){
        //TODO
    }

    // UC4
    public void addPrecription(String cf, Prescription prescription){
        //TODO
    }

    // UC5
    public void addAdministration(String cf, Administration administration){
        //TODO
    }

    // UC11
    public void dismissPatient(String cf){
        //TODO
    }

    // UC12
    public void changeVitals(String cf, Vitals vitals){
        //TODO
    }
}
