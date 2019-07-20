package it.univr.terapiaintensiva.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Model {

    private static Model instance = null;

    public static final char DOCTOR = 'd';
    public static final char NURSE = 'n';
    public static final char CHIEF = 'c';
    public static final char GUEST = 'g';

    // Paths
    private final String pathAuthenticationFile = "./assets/login.csv";
    private final String pathRegistryFile = "registry.csv";
    private final String pathVitalsFile = "vitals.csv";
    private final String pathPrescriptionsFile = "prescriptions.csv";
    private final String pathAdministrationsFile = "administrations.csv";
    private final String pathDiagnosisFile = "diagnosis.txt";
    private final String pathPatients = "./patients/";
    private final String pathDischarged = "./discharged/";

    // Macros
    private final int maxPatients;

    private ArrayList<Patient> patients;

    private char type;

    // Constructor
    private Model() {
        maxPatients = 10;

        try {
            // Check directory patients/
            if (!Files.exists(Paths.get(pathPatients)))
                Files.createDirectory(Paths.get(pathPatients));

            // Check directory discharged/
            if (!Files.exists(Paths.get(pathDischarged)))
                Files.createDirectory(Paths.get(pathDischarged));

            // Search if there are patients already hospitalized
            File patientsDir = new File(pathPatients);
            if (patientsDir.isDirectory()) {
                this.patients = new ArrayList<>();

                // There are patients already hospitalized
                if (patientsDir.list().length > 0) {
                    File[] patientsList = patientsDir.listFiles();

                    // Get patient's medical records
                    for (File medicalRecord : patientsList) {
                        if (medicalRecord.isDirectory()) {
                            // Path medical records
                            String pathMedicalRecords = pathPatients.concat(medicalRecord.getName() + "/");

                            // List of medical records files
                            String[] patientFiles = medicalRecord.list();

                            // Patient's data
                            Patient foundPatient = new Patient();
                            String foundDiagnosis = "";
                            Vitals foundVitals = new Vitals();
                            ArrayList<Prescription> foundPrescriptions = new ArrayList<>();
                            ArrayList<Administration> foundAdministrations = new ArrayList<>();

                            // Get patient's data from each file in medical records
                            for (String patientFile : patientFiles) {
                                String pathPatientFile = pathMedicalRecords.concat(patientFile);
                                switch (patientFile) {

                                    // Registry file
                                    case (pathRegistryFile):
                                        BufferedReader registryFile = new BufferedReader(new FileReader(pathPatientFile));

                                        // Skip format line
                                        FilesEditor.csvSkipRecord(registryFile);

                                        // Get patient
                                        String[] registryRecord = FilesEditor.csvReadRecord(registryFile);
                                        foundPatient = FilesEditor.csvGetPatient(registryRecord);

                                        registryFile.close();
                                        break;

                                    // Vitals file
                                    case (pathVitalsFile):
                                        BufferedReader vitalsFile = new BufferedReader(new FileReader(pathPatientFile));

                                        // Skip format line
                                        FilesEditor.csvSkipRecord(vitalsFile);

                                        // Get vitals
                                        String[] vitalsRecord = FilesEditor.csvReadLastRecord(vitalsFile);
                                        foundVitals = FilesEditor.csvGetVitals(vitalsRecord);

                                        vitalsFile.close();
                                        break;

                                    // Diagnosis file
                                    case (pathDiagnosisFile):
                                        BufferedReader diagnosisFile = new BufferedReader(new FileReader(pathPatientFile));

                                        // Get diagnosis
                                        String line;
                                        while ((line = diagnosisFile.readLine()) != null) {
                                            foundDiagnosis = foundDiagnosis.concat(line);
                                        }

                                        diagnosisFile.close();
                                        break;

                                    // Prescriptions file
                                    case (pathPrescriptionsFile):
                                        BufferedReader prescriptionsFile = new BufferedReader(new FileReader(pathPatientFile));

                                        // Skip format line
                                        FilesEditor.csvSkipRecord(prescriptionsFile);

                                        String[] prescriptionRecord = FilesEditor.csvReadRecord(prescriptionsFile);
                                        while (prescriptionRecord != null) {
                                            // Get prescription from record
                                            Prescription prescription = FilesEditor.csvGetPrescription(prescriptionRecord);
                                            foundPrescriptions.add(prescription);

                                            // Next record
                                            prescriptionRecord = FilesEditor.csvReadRecord(prescriptionsFile);
                                        }

                                        prescriptionsFile.close();
                                        break;

                                    // Administrations file
                                    case (pathAdministrationsFile):
                                        BufferedReader administrationsFile = new BufferedReader(new FileReader(pathPatientFile));

                                        // Skip format line
                                        FilesEditor.csvSkipRecord(administrationsFile);

                                        String[] administrationRecord = FilesEditor.csvReadRecord(administrationsFile);
                                        while (administrationRecord != null) {
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
                            if (!foundPatient.noRegistry()) {
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

        } catch (IOException e){
            System.out.println("Model() catch IOException!");
            e.printStackTrace();
        }
    }

    public static Model getInstance(){
        if (instance == null)
            instance = new Model();
        return instance;
    }

    // UC1
    public char authenticate(String username, String password) {
        try {
            BufferedReader authenticationFile = new BufferedReader(new FileReader(pathAuthenticationFile));

            // Skip format line
            FilesEditor.csvSkipRecord(authenticationFile);

            String[] recordData = FilesEditor.csvReadRecord(authenticationFile);
            // Check if there is a record
            while (recordData != null) {

                // Check record username and password with sing in username and password
                if (recordData[0].equals(username) && recordData[1].equals(password)) {
                    authenticationFile.close();
                    // Authentication complete: return user type
                    return this.type = recordData[2].charAt(0);
                }

                recordData = FilesEditor.csvReadRecord(authenticationFile);
            }
            // Wrong username and/or password
            authenticationFile.close();
            return this.type = Model.GUEST;

        } catch (IOException e){
            System.out.println("authenticate() catches IOException!");
            e.printStackTrace();
            // Error
            return 'e';
        }
    }

    public char getType() {
        return type;
    }

    // UC2
    public boolean hospitalizePatient(Patient patient) {
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

            try {
                // Create new patient's medical records
                String pathNewPatient = pathPatients.concat(patient.getCf() + "/");
                Files.createDirectory(Paths.get(pathNewPatient));

                // Create registry csv file
                String pathRegistry = pathNewPatient.concat(pathRegistryFile);
                FilesEditor.csvCreateFile(pathRegistry, Patient.csvFormat());

                // Create vitals csv file
                String pathVitals = pathNewPatient.concat(pathVitalsFile);
                FilesEditor.csvCreateFile(pathVitals, Vitals.csvFormat().concat(",date,time"));

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

                // Write patient's data on vitals file
                Vitals nullVitals = new Vitals(Vitals.nullVital, Vitals.nullVital, Vitals.nullVital, Vitals.nullVital);
                FilesEditor.csvWriteRecord(pathVitals, nullVitals);

                // Add new patient to list
                patients.add(patient);

                return true;

            } catch (IOException e){
                System.out.println("hospitalizePatient() catch IOException!");
                e.printStackTrace();
                return false;
            }
        }
    }

    // UC3
    public boolean setDiagnosis(String cf, String diagnosis) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return false;
        }

        try {
            // Check diagnosis file
            String pathDiagnosis = pathPatients.concat(cf + "/" + pathDiagnosisFile);
            if (!Files.exists(Paths.get(pathDiagnosis)))
                // Create diagnosis file if it doesn't exist
                Files.createFile(Paths.get(pathDiagnosis));

            // Write diagnosis on it
            FilesEditor.write(pathDiagnosis, diagnosis, false);

            // set patient's diagnosis
            patients.get(pEntry).setDiagnosis(diagnosis);

            return true;

        } catch (IOException e){
            System.out.println("setDiagnosis() catch IOException!");
            e.printStackTrace();
            return false;
        }
    }

    // UC4
    public boolean addPrescription(String cf, Prescription prescription) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return false;
        }

        try {
            Patient patient = patients.get(pEntry);

            // Check prescriptions file
            String pathPrescriptions = pathPatients.concat(cf + "/" + pathPrescriptionsFile);
            // File doesn't exist
            if (!Files.exists(Paths.get(pathPrescriptions))) {
                // Create prescriptions csv file
                FilesEditor.csvCreateFile(pathPrescriptions, Prescription.csvFormat());
                // Write list of patient's prescriptions on it
                for (Prescription p : patient.getPrescriptions())
                    FilesEditor.csvWriteRecord(pathPrescriptions, p);
            }

            // Write prescription
            FilesEditor.csvWriteRecord(pathPrescriptions, prescription);

            // Add prescription to patient
            patient.addPrescription(prescription);

            return true;

        } catch (IOException e) {
            System.out.println("addPrescription() catch IOException!");
            e.printStackTrace();
            return false;
        }
    }

    // UC5
    public boolean addAdministration(String cf, Administration administration) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return false;
        }

        try {
            Patient patient = patients.get(pEntry);

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

            // Write administration
            FilesEditor.csvWriteRecord(pathAdministrations, administration);

            // Add administration to patient
            patient.addAdministration(administration);

            return true;

        } catch (IOException e) {
            System.out.println("addAdministration() catch IOException!");
            e.printStackTrace();
            return false;
        }
    }

    // UC7, UC8
    public ArrayList<VitalsLog> getLastParameters(String cf) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return null;
        }

        // Check valid userType
        if(type!=GUEST && type!=NURSE && type!=DOCTOR && type!=CHIEF) {
            System.out.println("Invalid user type");
            return null;
        }

        LocalTime maxTime = LocalTime.now();
        if(type == GUEST)
            maxTime = maxTime.minusMinutes(15);
        else
            maxTime = maxTime.minusHours(2);

        try {
            // List of vital parameters returned
            ArrayList<VitalsLog> vitalsLogs = new ArrayList<>();

            // Search vitals log
            String pathVitals = pathPatients.concat(cf + "/" + pathVitalsFile);
            if (Files.exists(Paths.get(pathVitals))) {
                BufferedReader vitalsFile = new BufferedReader(new FileReader(pathVitals));
                FilesEditor.csvSkipRecord(vitalsFile);

                LocalDate today = LocalDate.now();

                String[] vitalsLog = FilesEditor.csvReadRecord(vitalsFile);
                while (vitalsLog != null) {
                    LocalTime logTime = FilesEditor.strToLocalTime(vitalsLog[5]);
                    LocalDate logDate = FilesEditor.strToLocalDate(vitalsLog[4]);
                    // Check time
                    if(maxTime.isBefore(logTime) || maxTime.equals(logTime))
                        vitalsLogs.add(FilesEditor.csvGetVitalsLog(vitalsLog));
/*                    else if(maxTime.isAfter(logTime) && isTomorrow(today, logDate)){
                        int diffHours = (24 - maxTime.getHour()) - logTime.getHour();
                    }
*/
                    vitalsLog = FilesEditor.csvReadRecord(vitalsFile);
                }

            }

            return vitalsLogs;

        } catch (IOException e) {
            System.out.println("getLastParameters() catches IOException");
            e.printStackTrace();
            return null;
        }

    }

    // UC11
    public boolean dischargePatient(String cf) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return false;
        }

        try {
            // Get discharged patient's medical records
            String pathSrc = pathPatients.concat(cf + "/");

            // Check if it doesn't exist in discharged/
            String pathDest = pathDischarged.concat(cf + "/");
            if (Files.exists(Paths.get(pathDest)) && Files.isDirectory(Paths.get(pathDest))) {
                // If it already exist
                File dirOld = new File(pathDest);
                // Remove previous files
                String[] filesList = dirOld.list();
                for (String pathFile : filesList) {
                    File file = new File(dirOld.getPath(), pathFile);
                    file.delete();
                }
                // Remove previous directory
                dirOld.delete();
            }

            Files.move(Paths.get(pathSrc), Paths.get(pathDest));

            // Remove patient from patients list
            patients.remove(pEntry);

            return true;

        } catch (IOException e){
            System.out.println("dischargePatient() catch IOException!");
            e.printStackTrace();
            return false;
        }
    }

    // UC12
    public boolean changeVitals(String cf, Vitals vitals) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return false;
        }

        try {
            Patient patient = patients.get(pEntry);

            // Check vitals csv file
            String pathVitals = pathPatients.concat(cf + "/" + pathVitalsFile);
            // File doesn't exist
            if (!Files.exists(Paths.get(pathVitals)))
                // Create vitals csv file
                FilesEditor.csvCreateFile(pathVitals, Vitals.csvFormat());

            // Write vitals
            FilesEditor.csvWriteRecord(pathVitals, vitals);

            // Change vitals value
            patient.setVitals(vitals);

            return true;

        } catch (IOException e) {
            System.out.println("changeVitals() catch IOException!");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param cf odice fiscale of the patient
     * @return the most recent instance of {@link Vitals}. Null in case of error.
     * @author ecavicc
     * Returns the current parameters of the given patient.
     */
    public Vitals getCurrentVitals(String cf) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if (pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return null;
        }

        // Check valid userType
        if (type != GUEST && type != NURSE && type != DOCTOR && type != CHIEF) {
            System.out.println("Invalid user type");
            return null;
        }

        String pathVitals = pathPatients.concat(cf + "/" + pathVitalsFile);
        if (Files.exists(Paths.get(pathVitals))) {
            try {
                BufferedReader vitalsFile = new BufferedReader(new FileReader(pathVitals));
                return FilesEditor.csvGetVitals(FilesEditor.csvReadLastRecord(vitalsFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
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

    // Check if thisDate is tomorrow of thatDate
    private boolean isTomorrow(LocalDate thisDate, LocalDate thatDate){
        int thisYear = thisDate.getYear();
        int thatYear = thatDate.getYear();

        // Last of year
        if(thisYear - thatYear == 1)
            return (thisDate.getMonthValue() == 1 &&
                    thisDate.getDayOfMonth() == 1 &&
                    thatDate.getMonthValue() == 12 &&
                    thatDate.getDayOfMonth() == 31);

        // It must be the same year
        else if(thisYear - thatYear == 0) {
            int thisMonth = thisDate.getMonthValue();
            int thatMonth = thatDate.getMonthValue();

            int thatDay = thatDate.getDayOfMonth();

            // Last of month
            if(thisMonth - thatMonth == 1)
                return (thatDay == 1 &&
                        lastDayOfMonth(thisDate));

            // It must be the same month
            else if(thisMonth - thatMonth == 0) {
                int thisDay = thisDate.getDayOfMonth();
                return (thisDay - thatDay == 1);
            }

        }

        return false;
    }

    private boolean lastDayOfMonth(LocalDate date){
        int month = date.getMonthValue();
        switch (month){

            case 4:
            case 6:
            case 9:
            case 11:
                return (date.getDayOfMonth() == 30);

            case 2:
                if(date.isLeapYear())
                    return (date.getDayOfMonth() == 29);
                return (date.getDayOfMonth() == 28);

            default:
                return (date.getDayOfMonth() == 31);
        }
    }
}
