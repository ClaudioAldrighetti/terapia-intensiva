package it.univr.terapiaintensiva.model;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class Model {

    // Paths
    private final String pathAutenticationFile = "./assets/login.csv";
    private final String pathRegistryFile = "registry.csv";
    private final String pathVitalsFile = "vitals.csv";
    private final String pathPrescriptionsFile = "prescriptions.csv";
    private final String pathAdministrationsFile = "administrations.csv";
    private final String pathDiagnosisFile = "diagnosis.txt";
    private final String pathPatients = "./patients/";
    private final String pathDischarged = "./discharged/";

    // Macros
    private final int maxPatients = 10;

    private ArrayList<Patient> patients;

    // Constructor
    public Model() throws IOException {
        // Search if there are patients already hospitalized
        File patientsDir = new File(pathPatients);
        if(patientsDir.isDirectory()){
            this.patients = new ArrayList<>();

            // There are patients already hospitalized
            if(patientsDir.list().length > 0) {
                File[] patientsList = patientsDir.listFiles();

                // Get patient's medical records
                for(File medicalRecord: patientsList){
                    if(medicalRecord.isDirectory()){
                        // Path medical records
                        String pathMedicalRecords = pathPatients.concat(medicalRecord.getName() + "/");

                        // List of medical records files
                        String[] patientFiles = medicalRecord.list();

                        // Patient's data
                        Patient foundPatient = new Patient();
                        String foundDiagnosis = new String();
                        Vitals foundVitals = new Vitals();
                        ArrayList<Prescription> foundPrescriptions = new ArrayList<>();
                        ArrayList<Administration> foundAdministrations = new ArrayList<>();

                        // Get patient's data from each file in medical records
                        for(String patientFile: patientFiles){
                            String pathPatientFile = pathMedicalRecords.concat(patientFile);
                            switch (patientFile){

                                // Registry file
                                case(pathRegistryFile):
                                    BufferedReader registryFile = new BufferedReader(new FileReader(pathPatientFile));

                                    // Skip format line
                                    FilesEditor.csvSkipRecord(registryFile);

                                    // Get patient
                                    String[] registryRecord = FilesEditor.csvReadRecord(registryFile);
                                    foundPatient = FilesEditor.csvGetPatient(registryRecord);

                                    registryFile.close();
                                    break;

                                // Vitals file
                                case(pathVitalsFile):
                                    BufferedReader vitalsFile = new BufferedReader(new FileReader(pathPatientFile));

                                    // Skip format line
                                    FilesEditor.csvSkipRecord(vitalsFile);

                                    // Get vitals
                                    String[] vitalsRecord = FilesEditor.csvReadLastRecord(vitalsFile);
                                    foundVitals = FilesEditor.csvGetVitals(vitalsRecord);

                                    vitalsFile.close();
                                    break;

                                // Diagnosis file
                                case(pathDiagnosisFile):
                                    BufferedReader diagnosisFile = new BufferedReader(new FileReader(pathPatientFile));

                                    // Get diagnosis
                                    String line;
                                    while( (line = diagnosisFile.readLine()) != null ){
                                        foundDiagnosis = foundDiagnosis.concat(line);
                                    }

                                    diagnosisFile.close();
                                    break;

                                // Prescriptions file
                                case(pathPrescriptionsFile):
                                    BufferedReader prescriptionsFile = new BufferedReader(new FileReader(pathPatientFile));

                                    // Skip format line
                                    FilesEditor.csvSkipRecord(prescriptionsFile);

                                    String[] prescriptionRecord = FilesEditor.csvReadRecord(prescriptionsFile);
                                    while( prescriptionRecord != null ){
                                        // Get prescription from record
                                        Prescription prescription = FilesEditor.csvGetPrescription(prescriptionRecord);
                                        foundPrescriptions.add(prescription);

                                        // Next record
                                        prescriptionRecord = FilesEditor.csvReadRecord(prescriptionsFile);
                                    }

                                    prescriptionsFile.close();
                                    break;

                                // Administrations file
                                case(pathAdministrationsFile):
                                    BufferedReader administrationsFile = new BufferedReader(new FileReader(pathPatientFile));

                                    // Skip format line
                                    FilesEditor.csvSkipRecord(administrationsFile);

                                    String[] administrationRecord = FilesEditor.csvReadRecord(administrationsFile);
                                    while( administrationRecord != null ){
                                        // Get administration from record
                                        Administration administration = FilesEditor.csvGetAdministration(administrationRecord);
                                        foundAdministrations.add(administration);

                                        // Next record
                                        administrationRecord = FilesEditor.csvReadRecord(administrationsFile);
                                    }

                                    administrationsFile.close();
                                    break;

                                // Ignored file
                                default:
                                    // Just nothing...
                                    break;
                            }
                        }

                        // Without registry file it's impossible to register foundPatient
                        if(foundPatient.noRegistry()){
                            // Found patient
                            foundPatient.setVitals(foundVitals);
                            foundPatient.setDiagnosis(foundDiagnosis);
                            foundPatient.setPrescriptions(foundPrescriptions);
                            foundPatient.setAdministrations(foundAdministrations);

                            // Add found patient to list
                            patients.add(foundPatient);
                        }
                    }
                }
            }
        }
    }

    // UC1
    public char authenticate(String username, String password) throws IOException {
        BufferedReader authenticationFile = new BufferedReader(new FileReader(pathAutenticationFile));

        // Skip format line
        FilesEditor.csvSkipRecord(authenticationFile);

        String[] recordData = FilesEditor.csvReadRecord(authenticationFile);
        // Check if there is a record
        while (recordData != null) {

            // Check record username and password with sing in username and password
            if ( recordData[0].equals(username) && recordData[1].equals(password) ){
                authenticationFile.close();
                // Authentication complete: return user type
                return recordData[2].charAt(0);
            }

            recordData = FilesEditor.csvReadRecord(authenticationFile);
        }
        // Wrong username and/or password
        authenticationFile.close();
        return 'w';
    }

    // UC2
    public boolean hospitalizePatient(Patient patient) throws IOException {
        // Check if there is a free bed
        if(patients.size() >= maxPatients)
            return false;

        else {
            // Check if patient isn't hospitalized yet
            int pEntry = findPatient(patient.getCf());

            // Wrong cf or patient is already hospitalized
            if(pEntry != -1) {
                System.out.println("Patient is already hospitalized");
                return false;
            }

            // Add new patient to list
            patients.add(patient);

            // Create new patient's medical records
            String pathNewPatient = pathPatients.concat(patient.getCf() + "/");
            Files.createDirectory(Paths.get(pathNewPatient));

            // Create registry csv file
            String pathRegistry = pathNewPatient.concat(pathRegistryFile);
            FilesEditor.csvCreateFile(pathRegistry, Patient.csvFormat());

            // Create vitals csv file
            String pathVitals = pathNewPatient.concat(pathVitalsFile);
            FilesEditor.csvCreateFile(pathVitals, Vitals.csvFormat());

            // Create prescriptions csv file
            String pathPrescriptions = pathNewPatient.concat(pathPrescriptionsFile);
            FilesEditor.csvCreateFile(pathPrescriptions, Prescription.csvFormat());

            // Create administrations csv file
            String pathAdministrations = pathNewPatient.concat(pathAdministrationsFile);
            FilesEditor.csvCreateFile(pathAdministrations, Administration.csvFormat());

            // Create diagnosis txt file
            String pathDiagnosis = pathNewPatient.concat(pathDiagnosisFile);
            Files.createFile(Paths.get(pathDiagnosis));

            // Write patient's data on registry file
            FilesEditor.csvWriteRecord(pathRegistry, patient);

            // Write vitals on log file
            FilesEditor.csvWriteRecord(pathVitals, patient.getVitals());

            return true;
        }
    }

    // UC3
    public boolean setDiagnosis(String cf, String diagnosis) throws IOException {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return false;
        }

        // set patient's diagnosis
        patients.get(pEntry).setDiagnosis(diagnosis);

        // Check diagnosis file
        String pathDiagnosis = pathPatients.concat(cf + "/" + pathDiagnosisFile);
        if(!Files.exists(Paths.get(pathDiagnosis)))
            // Create diagnosis file if it doesn't exist
            Files.createFile(Paths.get(pathDiagnosis));

        // Write diagnosis on it
        FilesEditor.write(pathDiagnosis, diagnosis, false);

        return true;
    }

    // UC4
    public boolean addPrescription(String cf, Prescription prescription) throws IOException {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return false;
        }

        // Add prescription to patient
        Patient patient = patients.get(pEntry);
        patient.addPrescription(prescription);

        // Check prescriptions file
        String pathPrescriptions = pathPatients.concat(cf + "/" + pathPrescriptionsFile);
        // File doesn't exist
        if(!Files.exists(Paths.get(pathPrescriptions))) {
            // Create prescriptions csv file
            FilesEditor.csvCreateFile(pathPrescriptions, Prescription.csvFormat());
            // Write list of patient's prescriptions on it
            for(Prescription p: patient.getPrescriptions())
                FilesEditor.csvWriteRecord(pathPrescriptions, p);
        }
        // File exist
        else
            // Write prescription
            FilesEditor.csvWriteRecord(pathPrescriptions, prescription);

        return true;
    }

    // UC5
    public boolean addAdministration(String cf, Administration administration) throws IOException {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return false;
        }

        // Add administration to patient
        Patient patient = patients.get(pEntry);
        patient.addAdministration(administration);

        // Check administrations file
        String pathAdministrations = pathPatients.concat(cf + "/" + pathAdministrationsFile);
        // File doesn't exist
        if(!Files.exists(Paths.get(pathAdministrations))) {
            // Create administrations csv file
            FilesEditor.csvCreateFile(pathAdministrations, Administration.csvFormat());
            // Write list of patient's administrations on it
            for(Administration a: patient.getAdministrations())
                FilesEditor.csvWriteRecord(pathAdministrationsFile, a);
        }
        // File exist
        else
            // Write administration
            FilesEditor.csvWriteRecord(pathAdministrations, administration);

        return true;
    }

    // UC11
    public void dischargePatient(String cf) throws IOException {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return;
        }

        // Get discharged patient's medical records
        String pathSrc = pathPatients.concat(cf + "/");

        // Check if it doesn't exist in discharged/
        String pathDest = pathDischarged.concat(cf + "/");
        if(Files.exists(Paths.get(pathDest)) && Files.isDirectory(Paths.get(pathDest))){
            // If it already exist
            File dirOld = new File(pathDest);
            // Remove previous files
            String[] filesList = dirOld.list();
            for(String pathFile: filesList){
                File file = new File(dirOld.getPath() ,pathFile);
                file.delete();
            }
            // Remove previous directory
            dirOld.delete();
        }

        Files.move(Paths.get(pathSrc), Paths.get(pathDest));

        // Remove patient from patients list
        patients.remove(pEntry);
    }

    // UC12
    public boolean changeVitals(String cf, Vitals vitals) throws IOException {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return false;
        }

        // Change vitals value
        Patient patient = patients.get(pEntry);
        patient.setVitals(vitals);

        // Check vitals csv file
        String pathVitals = pathPatients.concat(cf + "/" + pathVitalsFile);
        // File doesn't exist
        if(!Files.exists(Paths.get(pathVitals)))
            // Create vitals csv file
            FilesEditor.csvCreateFile(pathVitals, Vitals.csvFormat());

        // Write vitals
        FilesEditor.csvWriteRecord(pathVitals, vitals);

        return true;
    }

    // PUBLIC METHODS

    // Return patients ArrayList
    public ArrayList<Patient> getPatients(){
        return patients;
    }

    // AUXILIARY PRIVATE METHODS

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
