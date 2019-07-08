package it.univr.terapiaintensiva.model;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
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

    // Macros
    private final int maxPatients = 10;

    private ArrayList<Patient> patients;
    private BufferedReader autenticationFile;

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
                        String name = "";
                        String surname = "";
                        String cf = "";
                        String pob = "";
                        LocalDate dob = LocalDate.of(1970,1,1);
                        String diagnosis = "";
                        Vitals vitals = new Vitals();
                        ArrayList<Prescription> prescriptions = new ArrayList<>();
                        ArrayList<Administration> administrations = new ArrayList<>();

                        // Get patient's data from each file in medical records
                        for(String patientFile: patientFiles){
                            String pathPatientFile = pathMedicalRecords.concat(patientFile);
                            switch (patientFile){

                                // Registry file
                                case(pathRegistryFile):
                                    BufferedReader registryFile = new BufferedReader(new FileReader(pathPatientFile));
                                    String[] registryData = registryFile.readLine().split(",");

                                    // Get name, surname, cf and pob
                                    name = registryData[0];
                                    surname = registryData[1];
                                    cf = registryData[2];
                                    pob = registryData [3];

                                    // Get dob
                                    int bYear = Integer.parseInt(registryData[4].split("-")[0]);
                                    int bMonth = Integer.parseInt(registryData[4].split("-")[1]);
                                    int bDay = Integer.parseInt(registryData[4].split("-")[2]);
                                    dob = LocalDate.of(bYear, bMonth, bDay);

                                    registryFile.close();
                                    break;

                                // Vitals file
                                case(pathVitalsFile):
                                    BufferedReader vitalsFile = new BufferedReader(new FileReader(pathPatientFile));

                                    // Find last log
                                    String lastRecord = "";
                                    String vRecord;
                                    while( (vRecord = vitalsFile.readLine()) != null ){
                                        lastRecord = vRecord;
                                    }

                                    // Get vitals
                                    String[] vitalsData = lastRecord.split(",");

                                    int vHeartBeat = Integer.parseInt(vitalsData[0]);
                                    double vTemperature = Double.parseDouble(vitalsData[1]);
                                    int vSbp = Integer.parseInt(vitalsData[2]);
                                    int vDbp = Integer.parseInt(vitalsData[3]);

                                    vitals = new Vitals(vHeartBeat, vTemperature, vSbp, vDbp);

                                    vitalsFile.close();
                                    break;

                                // Diagnosis file
                                case(pathDiagnosisFile):
                                    BufferedReader diagnosisFile = new BufferedReader(new FileReader(pathPatientFile));

                                    // Get diagnosis
                                    String line;
                                    while( (line = diagnosisFile.readLine()) != null ){
                                        diagnosis = diagnosis.concat(line);
                                    }

                                    diagnosisFile.close();
                                    break;

                                // Prescriptions file
                                case(pathPrescriptionsFile):
                                    BufferedReader prescriptionsFile = new BufferedReader(new FileReader(pathPatientFile));

                                    String pRecord;
                                    while( (pRecord = prescriptionsFile.readLine()) != null ){
                                        // Get prescription
                                        String[] prescriptionData = pRecord.split(",");

                                        int pDuration = Integer.parseInt(prescriptionData[1]);
                                        String pMedicine = prescriptionData[2];
                                        int pNDoses = Integer.parseInt(prescriptionData[3]);
                                        double pDose = Double.parseDouble(prescriptionData[4]);

                                        // Prescription date
                                        int pYear = Integer.parseInt(prescriptionData[0].split("-")[0]);
                                        int pMonth = Integer.parseInt(prescriptionData[0].split("-")[1]);
                                        int pDay = Integer.parseInt(prescriptionData[0].split("-")[2]);
                                        LocalDate pDate = LocalDate.of(pYear, pMonth, pDay);

                                        prescriptions.add(new Prescription(pDate, pDuration, pMedicine, pNDoses, pDose));
                                    }

                                    prescriptionsFile.close();
                                    break;

                                // Administrations file
                                case(pathAdministrationsFile):
                                    BufferedReader administrationsFile = new BufferedReader(new FileReader(pathPatientFile));

                                    String aRecord;
                                    while( (aRecord = administrationsFile.readLine()) != null ){
                                        // Get administration
                                        String[] administrationData = aRecord.split(",");

                                        String aMedicine = administrationData[2];
                                        double aDose = Double.parseDouble(administrationData[3]);
                                        String aNotes = (administrationData.length == 5)? administrationData[4] : "";

                                        // Administration date
                                        int aYear = Integer.parseInt(administrationData[0].split("-")[0]);
                                        int aMonth = Integer.parseInt(administrationData[0].split("-")[1]);
                                        int aDay = Integer.parseInt(administrationData[0].split("-")[2]);
                                        LocalDate aDate = LocalDate.of(aYear, aMonth, aDay);

                                        // Administration time
                                        int aHour = Integer.parseInt(administrationData[1].split(":")[0]);
                                        int aMinute = Integer.parseInt(administrationData[1].split(":")[1]);
                                        LocalTime aTime = LocalTime.of(aHour, aMinute);

                                        administrations.add(new Administration(aDate, aTime, aMedicine, aDose, aNotes));
                                    }

                                    administrationsFile.close();
                                    break;

                                // Ignored file
                                default:
                                    // Just nothing...
                                    break;
                            }
                        }

                        // Found patient
                        Patient foundPatient = new Patient(name, surname, cf, pob, dob);
                        foundPatient.setVitals(vitals);
                        foundPatient.addDiagnosis(diagnosis);
                        foundPatient.setPrescriptions(prescriptions);
                        foundPatient.setAdministrations(administrations);

                        // Add found patient to list
                        patients.add(foundPatient);
                    }
                }
            }
        }
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
            // Check if patient isn't hospitalized yet
            int pEntry = findPatient(patient.getCf());

            // Wrong cf or patient isn't hospitalized
            if(pEntry != -1) {
                System.out.println("Patient is already hospitalized");
                return false;
            }

            // Add new patient to list
            patients.add(patient);

            // Create new patient's medical records
            String pathNewPatient = pathPatients.concat(patient.getCf() + "/");
            Files.createDirectory(Paths.get(pathNewPatient));

            // Create registry file
            String pathRegistry = pathNewPatient.concat(pathRegistryFile);
            Files.createFile(Paths.get(pathRegistry));

            // Write patient's data on registry file
            FileWriter csvWriter = new FileWriter(pathRegistry);
            csvWriter.write(patient.toCsv());
            csvWriter.flush();

            // Create vitals log file
            String pathVitals = pathNewPatient.concat(pathVitalsFile);
            Files.createFile(Paths.get(pathVitals));

            // Write vitals on log file
            csvWriter = new FileWriter(pathVitals);
            csvWriter.write(patient.getVitals().toCsv());
            csvWriter.flush();

            csvWriter.close();

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
            System.out.println("Patient has already a diagnosis");
            return;
        }

        // Add diagnosis to patient
        patients.get(pEntry).addDiagnosis(diagnosis);

        // Create diagnosis file in patient's medical records
        String pathDiagnosis = pathPatients.concat(cf + "/" + pathDiagnosisFile);
        Files.createFile(Paths.get(pathDiagnosis));

        // Write diagnosis on it
        FileWriter diagnosisWriter = new FileWriter(pathDiagnosis);
        diagnosisWriter.write(diagnosis + "\n");

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

        // Get prescriptions file path
        String pathPrescriptions = pathPatients.concat(cf + "/" + pathPrescriptionsFile);

        // If prescriptions file doesn't exist
        if(!Files.exists(Paths.get(pathPrescriptions)))
            // Create prescription file in patient's medical records
            Files.createFile(Paths.get(pathPrescriptions));

        // Write prescription on it
        FileWriter prescriptionsWriter = new FileWriter(pathPrescriptions, true);
        prescriptionsWriter.write(prescription.toCsv());

        prescriptionsWriter.flush();
        prescriptionsWriter.close();
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

        // Get administrations file path
        String pathAdministrations = pathPatients.concat(cf + "/" + pathAdministrationsFile);

        // If administrations file doesn't exist
        if(!Files.exists(Paths.get(pathAdministrations)))
            // Create administrations file in patient's medical records
            Files.createFile(Paths.get(pathAdministrations));

        // Write administration on it
        FileWriter administrationsWriter = new FileWriter(pathAdministrations, true);
        administrationsWriter.write(administration.toCsv());

        administrationsWriter.flush();
        administrationsWriter.close();
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
        File dirDischarged = new File(pathPatients.concat(cf));

        // Remove it
        String[] filesList = dirDischarged.list();
        for(String pathFile: filesList){
            File file = new File(dirDischarged.getPath() ,pathFile);
            file.delete();
        }
        dirDischarged.delete();

        // Remove patient from patients list
        patients.remove(pEntry);

        //TODO
    }

    // UC12
    public void changeVitals(String cf, Vitals vitals) throws IOException {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return;
        }

        // Change vitals value
        patients.get(pEntry).setVitals(vitals);

        // Add log
        File pathVitals = new File(pathPatients.concat(cf + "/" + pathVitalsFile));
        FileWriter vitalsWriter = new FileWriter(pathVitals, true);
        vitalsWriter.write(vitals.toCsv());

        vitalsWriter.flush();
        vitalsWriter.close();
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
/*
    // TEST MAIN
    public static void main(String[] args){
        try {
            Model model = new Model();
            System.out.println("Model model: creato!");

            String cf1 = "ASDF";
            String pathDir1 = model.pathPatients.concat(cf1 + "/");
            if(Files.exists(Paths.get(pathDir1))){
                // Print found patient
                System.out.println("Model model: paziente gia' ricoverato:");
                Patient patient1 = model.patients.get(0);
                System.out.println(patient1);

                // Print vitals
                System.out.println("Model model: parametri vitali:");
                System.out.println(patient1.getVitals());

                // Print diagnosis
                System.out.println("Model model: diagnosi:");
                System.out.println(patient1.getDiagnosis());

                // Print prescriptions
                System.out.println("Model model: prescrizioni:");
                System.out.println(patient1.getPrescriptions().get(0));
                System.out.println(patient1.getPrescriptions().get(1));

                // Print administrations
                System.out.println("Model model: somministrazioni:");
                System.out.println(patient1.getAdministrations().get(0));

                model.dischargePatient(cf1);
                System.out.println("Patient patient1: dimesso!");

            } else {
                Patient patient1 = new Patient("Claudio", "Aldrighetti", cf1, "Verona", LocalDate.of(1998, 02, 25));
                System.out.println("Patient patient1: creato:");
                System.out.println(patient1);

                model.createPatient(patient1);
                int pEntry1 = model.patients.indexOf(patient1);
                System.out.println("Patient patient1: ricoverato!");
                System.out.println(model.patients.get(pEntry1));

                model.addDiagnosis(cf1, "La Febbra");
                System.out.println("Patient patient1: aggiunta diagnosi:");
                System.out.println(model.patients.get(pEntry1).getDiagnosis());

                System.out.println("Patient patient1: aggiunta seconda diagnosi:");
                model.addDiagnosis(cf1, "La Febbra 2");
                System.out.println("Patient patient1: diagnosi finale:");
                System.out.println(model.patients.get(pEntry1).getDiagnosis());

                Prescription prs1 = new Prescription(LocalDate.now(), 120, "To mare", 3, 0.9);
                model.addPrecription(cf1, prs1);
                System.out.println("Patient patient1: aggiunta prescrizione 1!");
                System.out.println(model.patients.get(pEntry1).getPrescriptions().get(0));

                Prescription prs2 = new Prescription(LocalDate.now(), 90, "To sorella", 3, 0.9);
                model.addPrecription(cf1, prs2);
                System.out.println("Patient patient1: aggiunta prescrizione 2!");
                System.out.println(model.patients.get(pEntry1).getPrescriptions().get(1));
                System.out.println("Patient patient1: prescrizione precedente:");
                System.out.println(model.patients.get(pEntry1).getPrescriptions().get(0));

                Administration adm1 = new Administration(LocalDate.now(), LocalTime.now(), "To mare", 0.9);
                model.addAdministration(cf1, adm1);
                System.out.println("Patient patient1: aggiunta somministrazione 1!");
                System.out.println(model.patients.get(pEntry1).getAdministrations().get(0));

                Vitals vit1 = new Vitals(80, 36.5, 130, 85);
                model.changeVitals(cf1, vit1);
                System.out.println("Patient patient1: valori vitali cambiati!");
                System.out.println(model.patients.get(pEntry1).getVitals());

                Vitals vit2 = new Vitals(85, 37.1, 125, 90);
                model.changeVitals(cf1, vit2);
                System.out.println("Patient patient1: valori vitali cambiati!");
                System.out.println(model.patients.get(pEntry1).getVitals());
            }

        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }
*/
}
