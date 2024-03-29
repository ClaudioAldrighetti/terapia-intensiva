package it.univr.terapiaintensiva.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Model component of MVC pattern, this class is a singleton that manages memory of software (medical records, log files, registries, patients list etc..).
 * It meets Controller requests, archiving datas coming from View and extracting informations stored.
 * @author mizukami
 */
public class Model {

    private static Model instance = null;

    public static final char DOCTOR = 'd';
    public static final char NURSE = 'n';
    public static final char CHIEF = 'c';
    public static final char GUEST = 'g';

    private final String pathRegistryFile = "registry.csv";
    private final String pathVitalsFile = "vitals.csv";
    private final String pathPrescriptionsFile = "prescriptions.csv";
    private final String pathAdministrationsFile = "administrations.csv";
    private final String pathAlarmsFile = "alarms.csv";
    private final String pathDiagnosisFile = "diagnosis.txt";

    public static final String pathPatients = "./patients/";
    public static final String pathDischarged = "./discharged/";

    // Macros
    private final int maxPatients;

    private ArrayList<Patient> patients;

    private char type;

    /**
     * Loads medical records and patients list, searching informations about all hospitalized patients stored in files inside each medical records.
     */
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
                File[] patientsList = patientsDir.listFiles();
                if (patientsList != null && patientsList.length > 0) {

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
                            if (patientFiles != null) {
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
                                            if (registryRecord != null)
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
                            }

                            // Without registry file it's impossible to register foundPatient
                            if (foundPatient.hasRegistry()) {
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

        } catch (IOException e) {
            System.out.println("Model() catches IOException");
            e.printStackTrace();
        }
    }

    /**
     * Returns an instance of {@link Model}, initializing it if it is null. It makes {@link Model} a singleton.
     * @return An instance of {@link Model}.
     */
    public static Model getInstance(){
        if (instance == null)
            instance = new Model();
        return instance;
    }

    /**
     * Authenticates user checking username and password and returns his user type.
     * @param username username.
     * @param password password.
     * @return User type (guest, doctor etc...). In case of exception, it returns 'e'.
     */
    public char authenticate(String username, String password) {
        try {
            // Paths
            String pathAuthenticationFile = "./assets/login.csv";
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

    /**
     * @return type of logged user.
     */
    public char getType() {
        return type;
    }

    /**
     * If it is possible, it hospitalizes patient, adds him to patients list and creates his medical records and logs files.
     * @param patient {@link Patient} to hospitalize.
     * @return success of operation.
     */
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
                String pathNewPatient = pathPatients.concat(patient.getMedicalRecordsName() + "/");
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

                // Create alarms csv file
                String pathAlarms = pathNewPatient.concat(pathAlarmsFile);
                FilesEditor.csvCreateFile(pathAlarms, Alarm.csvFormat().concat(",notes"));

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
                System.out.println("hospitalizePatient() catches IOException!");
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Adds new diagnosis if patient hasn't a diagnosis or changes or modifies last diagnosis with new diagnosis.
     * @param cf codice fiscale of {@link Patient}.
     * @param diagnosis {@link String} diagnosis to add or change.
     */
    public void setDiagnosis(String cf, String diagnosis) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return;
        }

        Patient patient = patients.get(pEntry);
        try {
            // Check diagnosis file
            String pathDiagnosis = pathPatients.concat(patient.getMedicalRecordsName() + "/" + pathDiagnosisFile);
            if (!Files.exists(Paths.get(pathDiagnosis)))
                // Create diagnosis file if it doesn't exist
                Files.createFile(Paths.get(pathDiagnosis));

            // Write diagnosis on it
            FilesEditor.write(pathDiagnosis, diagnosis, false);

            // set patient's diagnosis
            patient.setDiagnosis(diagnosis);

        } catch (IOException e){
            System.out.println("setDiagnosis() catches IOException");
            e.printStackTrace();
        }
    }

    /**
     * Adds prescription to the patient and writes it on prescriptions csv file.
     * @param cf codice fiscale of {@link Patient}.
     * @param prescription {@link Prescription} to add.
     */
    public void addPrescription(String cf, Prescription prescription) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return;
        }

        Patient patient = patients.get(pEntry);
        try {
            // Check prescriptions file
            String pathPrescriptions = pathPatients.concat(patient.getMedicalRecordsName() + "/" + pathPrescriptionsFile);
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

        } catch (IOException e) {
            System.out.println("addPrescription() catches IOException");
            e.printStackTrace();
        }
    }

    /**
     * Adds adnministration to the patient and writes it on administrations csv file.
     * @param cf codice fiscale of {@link Patient}.
     * @param administration {@link Administration} to add.
     */
    public void addAdministration(String cf, Administration administration) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return;
        }

        Patient patient = patients.get(pEntry);
        try {
            // Check administrations file
            String pathAdministrations = pathPatients.concat(patient.getMedicalRecordsName() + "/" + pathAdministrationsFile);
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

        } catch (IOException e) {
            System.out.println("addAdministration() catches IOException");
            e.printStackTrace();
        }
    }

    /**
     * Returns list of last vital parameters logs of the patient.
     * @param cf codice fiscale of {@link Patient}.
     * @return List of {@link VitalsLog} registered within 15 minutes for guests or 2 hours for authenticated users. Null in case of error.
     */
    public ArrayList<VitalsLog> getLastParameters(String cf) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return null;
        }

        Patient patient = patients.get(pEntry);

        LocalTime maxTime = LocalTime.now();
        int maxSeconds;
        if(type == GUEST)
            maxSeconds = 900; // 15 minutes
        else
            maxSeconds = 7200; // 2 hours
        maxTime = maxTime.minusSeconds(maxSeconds);

        try {
            // List of vital parameters returned
            ArrayList<VitalsLog> vitalsLogs = new ArrayList<>();

            // Search vitals log
            String pathVitals = pathPatients.concat(patient.getMedicalRecordsName() + "/" + pathVitalsFile);
            if (Files.exists(Paths.get(pathVitals))) {
                BufferedReader vitalsFile = new BufferedReader(new FileReader(pathVitals));
                FilesEditor.csvSkipRecord(vitalsFile);

                LocalDate today = LocalDate.now();

                String[] vitalsLog = FilesEditor.csvReadRecord(vitalsFile);
                while (vitalsLog != null) {
                    LocalTime logTime = FilesEditor.strToLocalTime(vitalsLog[5]);
                    LocalDate logDate = FilesEditor.strToLocalDate(vitalsLog[4]);

                    // Check time
                    if( logDate.equals(today) && (maxTime.isBefore(logTime) || maxTime.equals(logTime)))
                        vitalsLogs.add(FilesEditor.csvGetVitalsLog(vitalsLog));

                    else if(isTomorrow(today, logDate)){
                        int diffSeconds =
                                (60 - maxTime.getSecond() + logTime.getSecond()) +
                                (60 - maxTime.getMinute() + logTime.getMinute())*60 +
                                (24 - maxTime.getHour() + logTime.getHour())*3600;

                        if(diffSeconds <= maxSeconds)
                            vitalsLogs.add(FilesEditor.csvGetVitalsLog(vitalsLog));
                    }

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

    /**
     * Discharges the hospitalized patient archiving his medical records, creating a discharge letter and removing him from patients list.
     * @param cf codice fiscale of {@link Patient}.
     * @param letterText text of resignation letter.
     */
    public void dischargePatient(String cf, String letterText) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if(pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return;
        }

        Patient patient = patients.get(pEntry);
        try {
            // Get discharged patient's medical records
            String pathPatient = pathPatients.concat(patient.getMedicalRecordsName() + "/");

            // Check patient's dir in discharged/
            String pathDischargedPatient = pathDischarged.concat(patient.getMedicalRecordsName() + "/");
            if(!Files.exists(Paths.get(pathDischargedPatient)))
                Files.createDirectory(Paths.get(pathDischargedPatient));

            // Create directory to archive patient's medical records
            String pathThisDischarge = pathDischargedPatient.concat(LocalDate.now()+"_"+LocalTime.now()+"/");
            Files.createDirectory(Paths.get(pathThisDischarge));

            String[] filesToMove = (new File(pathPatient)).list();
            if(filesToMove != null)
                for (String fileToMove : filesToMove)
                    Files.move(Paths.get(pathPatient.concat(fileToMove)), Paths.get(pathThisDischarge.concat(fileToMove)));
            Files.delete(Paths.get(pathPatient));

            // Creating new discharge letter
            String pathLetterFile = "dischargeLetter.txt";
            String pathDischargeLetter = pathThisDischarge.concat(pathLetterFile);
            Files.createFile(Paths.get(pathDischargeLetter));
            FilesEditor.write(pathDischargeLetter, letterText, false);

            // Remove patient from patients list
            patients.remove(pEntry);

        } catch (IOException e){
            System.out.println("dischargePatient() catches IOException");
            e.printStackTrace();
        }
    }

    /**
     * Returns the current parameters of the given patient.
     * @param cf codice fiscale of {@link Patient}.
     * @return the most recent instance of {@link Vitals}. Null in case of error.
     */
    public Vitals getCurrentVitals(String cf) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if (pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return null;
        }

        Patient patient = patients.get(pEntry);
        String pathVitals = pathPatients.concat(patient.getMedicalRecordsName() + "/" + pathVitalsFile);
        if (Files.exists(Paths.get(pathVitals))) {
            try {
                BufferedReader vitalsFile = new BufferedReader(new FileReader(pathVitals));
                FilesEditor.csvSkipRecord(vitalsFile);
                Vitals currentVitals = FilesEditor.csvGetVitals(FilesEditor.csvReadLastRecord(vitalsFile));

                patient.setVitals(currentVitals);
                return currentVitals;

            } catch (IOException e) {
                System.out.println("getCurrentVitals() catches IOException");
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Looks for new {@link Alarm}s of patient and returns their list.
     * @param cf codice fiscale of {@link Patient}.
     * @return list of new {@link Alarm}s.
     */
    public ArrayList<Alarm> checkNewAlarms(String cf){
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if (pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return null;
        }

        Patient patient = patients.get(pEntry);

        ArrayList<Alarm> alarms = new ArrayList<>();
        String pathPatient = pathPatients.concat(patient.getMedicalRecordsName() + "/");
        if(Files.exists(Paths.get(pathPatient)) && Files.isDirectory(Paths.get(pathPatient))){
            File dirPatient = new File(pathPatient);
            String[] filesPatient = dirPatient.list();

            if(filesPatient != null)
                for(String filePatient: filesPatient)
                    if (filePatient.contains("newAlr"))
                        alarms.add(readAlarm(cf, pathPatient.concat(filePatient)));
        }

        return alarms;
    }

    /**
     * Returns new {@link Alarm} and deletes its file.
     * @param cf codice fiscale of {@link Patient}.
     * @param pathAlarmFile path of new alarm file.
     * @return {@link Alarm} extracted.
     */
    private Alarm readAlarm(String cf, String pathAlarmFile) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if (pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return null;
        }

        try {
            BufferedReader alarmFile = new BufferedReader(new FileReader(pathAlarmFile));
            FilesEditor.csvSkipRecord(alarmFile);

            // Get new alarm
            String[] alarmData = FilesEditor.csvReadRecord(alarmFile);
            if(alarmData != null) {
                Alarm alarm = FilesEditor.csvGetAlarm(alarmData);

                // Delete new alarm file
                Files.delete(Paths.get(pathAlarmFile));

                return alarm;
            }

            return null;

        } catch (IOException e) {
            System.out.println("readAlarm() catches IOException");
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Writes alarm on alarms csv file to archive it as resolved.
     * @param cf codice fiscale of {@link Patient}.
     * @param alarm resolved {@link Alarm}.
     * @param notes information about how alarm was offed.
     */
    public void offAlarm(String cf, Alarm alarm, String notes) {
        // Find patient
        int pEntry = findPatient(cf);

        // Wrong cf or patient isn't hospitalized
        if (pEntry == -1) {
            System.out.println("Patient not found: invalid cf");
            return;
        }

        Patient patient = patients.get(pEntry);
        try {
            // Check alarms csv file
            String pathAlarms = pathPatients.concat(patient.getMedicalRecordsName() + "/" + pathAlarmsFile);
            if (!Files.exists(Paths.get(pathAlarms)))
                FilesEditor.csvCreateFile(pathAlarms, Alarm.csvFormat().concat(",notes"));

            // Write resolved alarm on alarms csv file
            // N.B: To concat notes, we have to use write() and not csvWriteRecord()
            FilesEditor.write(pathAlarms, alarm.toCsv().concat("," + notes + "\n"));

        } catch(IOException e) {
            System.out.println("offAlarm() catches IOException");
            e.printStackTrace();
        }
    }

    /**
     * @param cf codice fiscale of {@link Patient}.
     * @param pathEsternDir directory that contains hospitalized patient's medical records or that is discharged patient's archived medical records.
     * @param isHospitalized true if patient is hospitalized.
     * @return list of {@link AlarmOff}s correlated to the {@link Patient}.
     */
    private ArrayList<AlarmOff> getPatientAlarmsOff(String cf, String pathEsternDir, boolean isHospitalized){
        // Alarms list
        ArrayList<AlarmOff> alarms = new ArrayList<>();

        // Alarms csv file
        String pathAlarms;

        if(isHospitalized) {
            // Find patient
            int pEntry = findPatient(cf);

            // Wrong cf or patient isn't hospitalized
            if (pEntry == -1) {
                System.out.println("Patient not found: invalid cf");
                return null;
            }

            Patient patient = patients.get(pEntry);
            pathAlarms = pathEsternDir.concat(patient.getMedicalRecordsName() + "/" + pathAlarmsFile);

        } else
            pathAlarms = pathEsternDir.concat((pathEsternDir.endsWith("/")? "" : "/") + pathAlarmsFile);

        try {
            BufferedReader alarmsFile = new BufferedReader(new FileReader(pathAlarms));
            FilesEditor.csvSkipRecord(alarmsFile);

            String[] alarmRecord = FilesEditor.csvReadRecord(alarmsFile);
            while (alarmRecord != null) {
                alarms.add(FilesEditor.csvGetAlarmOff(alarmRecord));
                alarmRecord = FilesEditor.csvReadRecord(alarmsFile);
            }

            return alarms;

        } catch(IOException e) {
            System.out.println("getPatientAlarmsOff() catches IOException");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Selects default directory that contains patients medical records as pathPatientsDir.
     * @param cf codice fiscale of {@link Patient}.
     * @return list of {@link Alarm}s correlated to the {@link Patient}.
     */
    private ArrayList<AlarmOff> getPatientAlarmsOff(String cf){
        return getPatientAlarmsOff(cf, pathPatients, true);
    }

    /**
     * @return list of hospitalized {@link Patient}s.
     */
    // Return hospitalized patients list
    public ArrayList<Patient> getHospitalizedPatients(){
        return patients;
    }

    /**
     * Returns discharged patients found searching their registry file in their archived medical records.
     * @return list of discharged {@link Patient}s.
     */
    private ArrayList<Patient> getDischargedPatients(){
        ArrayList<Patient> dischargedPatients = new ArrayList<>();

        // Search in discharged patients directory
        if(Files.exists(Paths.get(pathDischarged)) && Files.isDirectory(Paths.get(pathDischarged))) {
            File dirDischargedPatients = new File(pathDischarged);
            String[] pathDirDischargedPatients = dirDischargedPatients.list();

            // Search discharged patients
            if(pathDirDischargedPatients != null) {
                for (String pathDirDischargedPatient : pathDirDischargedPatients) {
                    if (Files.isDirectory(Paths.get(pathDischarged.concat(pathDirDischargedPatient)))) {

                        // Here it's in: ./discharged/cf/
                        File dirDischargedPatient = new File(pathDischarged.concat(pathDirDischargedPatient));
                        File[] filesDirDischargedPatient = dirDischargedPatient.listFiles();
                        if (filesDirDischargedPatient != null && filesDirDischargedPatient.length != 0) {

                            // Take last archived medical records files list
                            File[] lastMRFiles = filesDirDischargedPatient[filesDirDischargedPatient.length - 1].listFiles();
                            Patient foundPatient = new Patient();
                            String diagnosis = "";

                            // Search registry file in medical records
                            if(lastMRFiles != null) {
                                for (File lastMRFile : lastMRFiles) {

                                    // Registry file
                                    if (lastMRFile.getName().contains(pathRegistryFile)) {
                                        try {
                                            // Get discharged patient from registry file
                                            BufferedReader registryFile = new BufferedReader(new FileReader(lastMRFile));
                                            FilesEditor.csvSkipRecord(registryFile);

                                            String[] registryData = FilesEditor.csvReadRecord(registryFile);
                                            if (registryData != null) {
                                                foundPatient = FilesEditor.csvGetPatient(registryData);
                                                dischargedPatients.add(foundPatient);
                                            }

                                        } catch (IOException e) {
                                            System.out.println("getDischargedPatients() catches IOException");
                                            e.printStackTrace();
                                        }
                                    }

                                    // Diagnosis file
                                    else if (lastMRFile.getName().contains(pathDiagnosisFile)) {
                                        try {
                                            // Get diagnosis from diagnosis file
                                            BufferedReader diagnosisFile = new BufferedReader(new FileReader(lastMRFile));

                                            String line = diagnosisFile.readLine();
                                            while (line != null) {
                                                diagnosis = diagnosis.concat(line);
                                                line = diagnosisFile.readLine();
                                            }

                                        } catch (IOException e) {
                                            System.out.println("getDischargedPatients() catches IOException");
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                // Set diagnosis if patient is found
                                if (foundPatient.hasRegistry())
                                    foundPatient.setDiagnosis(diagnosis);
                            }
                        }
                    }
                }
            }
        }

        return dischargedPatients;
    }

    /**
     * @param firstDate the less recent date of report.
     * @param lastDate the latest date of report.
     * @param patient {@link Patient} to report.
     * @return patient's report.
     */
    private String getHospitalizedPatientReport(LocalDate firstDate, LocalDate lastDate, Patient patient) {
        // Patient string
        String patientStr = "";

        // Check prescriptions list
        String prescriptionsStr = "";
        if(!patient.getPrescriptions().isEmpty()) {
            int n = 1;
            for (Prescription prescription : patient.getPrescriptions())
                if(isBetween(prescription.getDate(), firstDate, lastDate))
                    prescriptionsStr = prescriptionsStr.concat("\n" + (n++) + ")" + prescription.toString());
        }
        if(!prescriptionsStr.equals("")) {
            prescriptionsStr = "\n\nPrescrizioni:" + prescriptionsStr;
            patientStr = patientStr.concat(prescriptionsStr);
        }

        // Check administrations list
        String administrationsStr = "";
        if(!patient.getAdministrations().isEmpty()) {
            int n = 1;
            for (Administration administration : patient.getAdministrations())
                if(isBetween(administration.getDate(), firstDate, lastDate))
                    administrationsStr = administrationsStr.concat("\n" + (n++) + ")" + administration.toString());
        }
        if(!administrationsStr.equals("")) {
            administrationsStr = "\n\nSomministrazioni:" + administrationsStr;
            patientStr = patientStr.concat(administrationsStr);
        }

        // Search for alarms
        String alarmsStr = "";
        ArrayList<AlarmOff> alarms = getPatientAlarmsOff(patient.getCf());
        if(!alarms.isEmpty()) {
            int n = 1;
            for (AlarmOff alarm : alarms)
                if(isBetween(alarm.getDate(), firstDate, lastDate))
                    alarmsStr = alarmsStr.concat("\n" + (n++) + ")" + alarm.toString());
        }
        if(!alarmsStr.equals("")) {
            alarmsStr = "\n\nAllarmi:" + alarmsStr;
            patientStr = patientStr.concat(alarmsStr);
        }

        // If some events were found
        if(!patientStr.equals("")) {
            patientStr =
                    patient.toString() +
                    "\nDiagnosi: " + ((patient.getDiagnosis().equals(""))? "nessuna" : patient.getDiagnosis()) +
                    patientStr + "\n";
        }

        return patientStr;
    }

    /**
     * @param firstDate the less recent date of report.
     * @param lastDate the latest date of report.
     * @param patient {@link Patient} to report.
     * @return patient's report.
     */
    private String getDischargedPatientReport(LocalDate firstDate, LocalDate lastDate, Patient patient) {
        // Patient string
        String patientStr = "";

        // Other strings
        String prescriptionsStr = "";
        String administrationsStr = "";
        String alarmsStr = "";

        String pathPatient = pathDischarged.concat(patient.getMedicalRecordsName() + "/");
        if(Files.exists(Paths.get(pathPatient))){
            File dirPatient = new File(pathPatient);
            if(dirPatient.isDirectory()){

                // Here it's in: ./discharged/cf/
                File[] listMedicalRecords = dirPatient.listFiles();
                if(listMedicalRecords != null) {
                    for (File medicalRecords : listMedicalRecords) {
                        if (medicalRecords.isDirectory()) {

                            // Search, for each medical records, list of prescriptions, administrations and alarms
                            String pathMedicalRecords = medicalRecords.getPath();
                            String[] patientFiles = medicalRecords.list();
                            if(patientFiles != null) {
                                for (String patientFile : patientFiles) {
                                    String pathPatientFile = pathMedicalRecords.concat("/" + patientFile);
                                    switch (patientFile) {

                                        // Prescriptions list
                                        case (pathPrescriptionsFile):
                                            try {
                                                ArrayList<Prescription> prescriptions = new ArrayList<>();
                                                BufferedReader prescriptionsFile = new BufferedReader(new FileReader(pathPatientFile));
                                                FilesEditor.csvSkipRecord(prescriptionsFile);

                                                // Extract prescriptions list from prescriptions csv file
                                                String[] prescriptionRecord = FilesEditor.csvReadRecord(prescriptionsFile);
                                                while (prescriptionRecord != null) {
                                                    prescriptions.add(FilesEditor.csvGetPrescription(prescriptionRecord));
                                                    prescriptionRecord = FilesEditor.csvReadRecord(prescriptionsFile);
                                                }

                                                // Check each prescription
                                                int n = 1;
                                                for (Prescription prescription : prescriptions)
                                                    if (isBetween(prescription.getDate(), firstDate, lastDate))
                                                        prescriptionsStr = prescriptionsStr.concat("\n" + (n++) + ")" + prescription.toString());

                                            } catch (IOException e) {
                                                System.out.println("getDischargedPatientReport() catches IOException");
                                                e.printStackTrace();
                                            }
                                            break;

                                        // Administration list
                                        case (pathAdministrationsFile):
                                            try {
                                                ArrayList<Administration> administrations = new ArrayList<>();
                                                BufferedReader administrationsFile = new BufferedReader(new FileReader(pathPatientFile));
                                                FilesEditor.csvSkipRecord(administrationsFile);

                                                // Extract administrations list from administrations csv file
                                                String[] administrationRecord = FilesEditor.csvReadRecord(administrationsFile);
                                                while (administrationRecord != null) {
                                                    administrations.add(FilesEditor.csvGetAdministration(administrationRecord));
                                                    administrationRecord = FilesEditor.csvReadRecord(administrationsFile);
                                                }

                                                int n = 1;
                                                for (Administration administration : administrations)
                                                    if (isBetween(administration.getDate(), firstDate, lastDate))
                                                        administrationsStr = administrationsStr.concat("\n" + (n++) + ")" + administration.toString());

                                            } catch (IOException e) {
                                                System.out.println("getDischargedPatientReport() catches IOException");
                                                e.printStackTrace();
                                            }
                                            break;

                                        // Alarms list
                                        case (pathAlarmsFile):
                                            ArrayList<AlarmOff> alarms = getPatientAlarmsOff(patient.getCf(), pathMedicalRecords, false);
                                            if (alarms != null) {
                                                int n = 1;
                                                for (AlarmOff alarm : alarms)
                                                    if (isBetween(alarm.getDate(), firstDate, lastDate))
                                                        alarmsStr = alarmsStr.concat("\n" + (n++) + ")" + alarm.toString());
                                            }

                                            break;

                                        default:
                                            // Just nothing...
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Check prescriptions
        if(!prescriptionsStr.equals("")) {
            prescriptionsStr = "\n\nPrescrizioni:" + prescriptionsStr;
            patientStr = patientStr.concat(prescriptionsStr);
        }

        // Check administrations
        if(!administrationsStr.equals("")) {
            administrationsStr = "\n\nSomministrazioni:" + administrationsStr;
            patientStr = patientStr.concat(administrationsStr);
        }

        // Search for alarms
        if(!alarmsStr.equals("")) {
            alarmsStr = "\n\nAlarms:" + alarmsStr;
            patientStr = patientStr.concat(alarmsStr);
        }

        // Patient report string
        if(!patientStr.equals("")) {
            patientStr =
                patient.toString() +
                "\nDiagnosi: " + ((patient.getDiagnosis().equals(""))? "nessuna" : patient.getDiagnosis()) +
                patientStr + "\n";
        }

        return patientStr;
    }

    /**
     * Generates a report of happened events between firstDate and lastDate.
     * @param firstDate the less recent date of report.
     * @param lastDate the latest date of report.
     * @return report of selected time lapse.
     */
    public String getReport(LocalDate firstDate, LocalDate lastDate){
        // Check user
        if(type != CHIEF)
            return null;

        // Check and switch invalid time lapse
        if(firstDate.isAfter(lastDate))
            return getReport(lastDate, firstDate);

        // Report string
        String reportStr = "";
        String reportSeparatorStr = ("\n<><><><><><><><><><><><><><><><><><><><><><><><>\n");

        // Search in hospitalized patients list
        for(Patient hospitalizedPatient: patients){
            String patientReportStr = getHospitalizedPatientReport(firstDate, lastDate, hospitalizedPatient);
            if(!patientReportStr.equals(""))
                reportStr = reportStr.concat(patientReportStr + reportSeparatorStr);
        }

        // Search in discharged patients list
        for(Patient dischargedPatient: getDischargedPatients()){
            String patientReportStr = getDischargedPatientReport(firstDate, lastDate, dischargedPatient);
            if(!patientReportStr.equals(""))
                reportStr = reportStr.concat(patientReportStr + reportSeparatorStr);
        }

        return reportStr;
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

    private boolean isBetween(LocalDate thisDate, LocalDate firstDate, LocalDate lastDate){
        return (
            (thisDate.isAfter(firstDate) || thisDate.isEqual(firstDate)) &&
            (thisDate.isBefore(lastDate) || thisDate.isEqual(lastDate))
        );
    }
}