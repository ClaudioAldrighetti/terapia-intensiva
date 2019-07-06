package it.univr.terapiaintensiva.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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
    }

    // UC2
    public boolean createPatient(Patient patient) throws IOException {
        // Check if there is a free bed
        if(patients.size() >= maxPatients)
            return false;

        else {
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
    public void addDiagnosis(String cf, String diagnosis) throws IOException {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return;
        }

        // Check if patient already has a diagnosis
        if(!patients.get(pEntry).getDiagnosis().isEmpty()){
            System.out.println("Patient already has a diagnosis");
            return;
        }

        // Add diagnosis to patient
        patients.get(pEntry).addDiagnosis(diagnosis);

        // Create diagnosis file in patient's medical records
        String pathDiagnosis = pathPatients.concat(cf).concat("/diagnosis.txt");
        Files.createFile(Paths.get(pathDiagnosis));

        // Write diagnosis on it
        FileWriter diagnosisWriter = new FileWriter(pathDiagnosis);
        diagnosisWriter.append(diagnosis);

        diagnosisWriter.flush();
        diagnosisWriter.close();
    }

    // UC4
    public void addPrecription(String cf, Prescription prescription) throws IOException {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return;
        }

        // Add prescription to patient
        patients.get(pEntry).addPrescription(prescription);

        // Create prescription file in patient's medical records
        String dirCf = cf.concat("/");
        String pNum = String.valueOf(patients.get(pEntry).getPrescriptions().size());
        String prescriptionFile = "prescription_".concat(pNum).concat(".txt");
        String pathPrescription = pathPatients.concat(dirCf).concat(prescriptionFile);
        Files.createFile(Paths.get(pathPrescription));

        // Write prescription on it
        FileWriter prescriptionWriter = new FileWriter(pathPrescription);
        prescriptionWriter.append(prescription.toString());

        prescriptionWriter.flush();
        prescriptionWriter.close();
    }

    // UC5
    public void addAdministration(String cf, Administration administration) throws IOException {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return;
        }

        // Add administration to patient
        patients.get(pEntry).addAdministration(administration);

        // Create administration file in patient's medical records
        String dirCf = cf.concat("/");
        String aNum = String.valueOf(patients.get(pEntry).getAdministrations().size());
        String administrationFile = "administration_".concat(aNum).concat(".txt");
        String pathAdministration = pathPatients.concat(dirCf).concat(administrationFile);
        Files.createFile(Paths.get(pathAdministration));

        // Write prescription on it
        FileWriter administrationWriter = new FileWriter(pathAdministration);
        administrationWriter.append(administration.toString());

        administrationWriter.flush();
        administrationWriter.close();
    }

    // UC11
    public void dismissPatient(String cf){
        //TODO
    }

    // UC12
    public void changeVitals(String cf, Vitals vitals){
        //TODO
    }

    // AUXILIARY METHODS

    // Return index position of patient in patients, or -1 if patient doesn't exist
    private int findPatient(String cf){
        int pEntry;
        for(pEntry = 0; pEntry < patients.size(); pEntry++)
            // Check if patient at pEntry has cf equal to passed cf
            if(patients.get(pEntry).getCf().equals(cf))
                return pEntry;
        return -1;
    }
}
