package it.univr.terapiaintensiva.model;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Static class used to perform some operations with files and object (especially with csv files and CsvWritable classes).
 * @author mizukami
 */
public final class FilesEditor {

    // CSV INTERACTIONS

    /**
     * Creates csv file and writes in its first line the format that records must have.
     * @param pathCsvFile path of new csv file.
     * @param csvFormat format of records.
     * @throws IOException wrong pathCsvFile.
     */
    // Create csv file and write in first line record fields
    static void csvCreateFile(String pathCsvFile, String csvFormat) throws IOException {
        Files.createFile(Paths.get(pathCsvFile));
        write(pathCsvFile, csvFormat.concat("\n"));
    }

    /**
     * Returns values of read record.
     * @param csvFile {@link BufferedReader} used to read one record (line) from a csv file.
     * @return an array of {@link String} that contains the values of read record. Null if there isn't a record to read or in case of error.
     */
    // Return one record from csvFile as String[]
    static String[] csvReadRecord(BufferedReader csvFile) {
        // DEFAULT SEPARATORS
        String csvSeparator = ",";

        try {
            String line = csvFile.readLine();
            return (line != null)?
                    line.split(csvSeparator) : null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns values of last record.
     * @param csvFile {@link BufferedReader} used to read last record (line) from a csv file.
     * @return an array of {@link String} that contains the values of read record. Null in case of error or empty file.
     */
    // Return last record of csvFile
    static String[] csvReadLastRecord(BufferedReader csvFile) {
        String[] lastRecord= null;

        String[] record = csvReadRecord(csvFile);
        while( record != null ){
            lastRecord = record;
            record = csvReadRecord(csvFile);
        }

        return lastRecord;
    }

    /**
     * Skip current record of csv file (usually is used to skip format line).
     * @param csvFile {@link BufferedReader} of csv file.
     * @throws IOException wrong csvFile.
     */
    // Skip next record of csvFile
    static void csvSkipRecord(BufferedReader csvFile) throws IOException {
        csvFile.readLine();
    }

    /**
     * Returns {@link Patient} from values in registryData.
     * @param registryData values read from registry csv file.
     * @return {@link Patient} got from data.
     */
    static Patient csvGetPatient(String[] registryData){
        String name = registryData[0];
        String surname = registryData[1];
        String cf = registryData[2];
        String pob = registryData[3];
        LocalDate dob = strToLocalDate(registryData[4]);

        return new Patient(name, surname, cf, pob, dob);
    }

    /**
     * Returns {@link Vitals} from values in vitalsData.
     * @param vitalsData values read from vitals csv file.
     * @return {@link Vitals} got from data.
     */
    static Vitals csvGetVitals(String[] vitalsData){
        int heartBeat = Integer.parseInt(vitalsData[0]);
        double temperature = Double.parseDouble(vitalsData[1]);
        int sbp = Integer.parseInt(vitalsData[2]);
        int dbp = Integer.parseInt(vitalsData[3]);

        return new Vitals(heartBeat, temperature, sbp, dbp);
    }

    /**
     * Returns {@link VitalsLog} from values in vitalsLogData.
     * @param vitalsLogData values read from vitals csv file.
     * @return {@link VitalsLog} got from data.
     */
    static VitalsLog csvGetVitalsLog(String[] vitalsLogData){
        LocalDate date = strToLocalDate(vitalsLogData[4]);
        LocalTime time = strToLocalTime(vitalsLogData[5]);

        return new VitalsLog(csvGetVitals(vitalsLogData), date, time);
    }

    /**
     * Returns {@link Prescription} from values in prescriptionData.
     * @param prescriptionData values read from prescriptions csv file.
     * @return {@link Prescription} got from data.
     */
    static Prescription csvGetPrescription(String[] prescriptionData) {
        int duration = Integer.parseInt(prescriptionData[0]);
        String medicine = prescriptionData[1];
        int nDoses = Integer.parseInt(prescriptionData[2]);
        double dose = Double.parseDouble(prescriptionData[3]);
        LocalDate date = strToLocalDate(prescriptionData[4]);
        LocalTime time = strToLocalTime(prescriptionData[5]);

        return new Prescription(duration, medicine, nDoses, dose, date, time);
    }

    /**
     * Returns {@link Administration} from values in administrationData.
     * @param administrationData values read from administrations csv file.
     * @return {@link Administration} got from data.
     */
    static Administration csvGetAdministration(String[] administrationData) {
        String medicine = administrationData[0];
        double dose = Double.parseDouble(administrationData[1]);
        String notes = (administrationData.length == 5)? administrationData[2] : "";
        LocalDate date = strToLocalDate(administrationData[3]);
        LocalTime time = strToLocalTime(administrationData[4]);

        return new Administration(medicine, dose, notes, date, time);
    }

    /**
     * Returns {@link Alarm} from values in alarmData.
     * @param alarmData values read from alarms csv file.
     * @return {@link Alarm} got from data.
     */
    static Alarm csvGetAlarm(String[] alarmData){
        String name = alarmData[0];
        int level = Integer.parseInt(alarmData[1]);
        char status = alarmData[2].charAt(0);
        LocalDate date = strToLocalDate(alarmData[3]);
        LocalTime time = strToLocalTime(alarmData[4]);

        return new Alarm(name, level, status, date, time);
    }

    /**
     * Returns {@link AlarmOff} from values in alarmData.
     * @param alarmData values read from alarms csv file.
     * @return {@link AlarmOff} got from data.
     */
    static AlarmOff csvGetAlarmOff(String[] alarmData){
        String name = alarmData[0];
        int level = Integer.parseInt(alarmData[1]);
        char status = alarmData[2].charAt(0);
        LocalDate date = strToLocalDate(alarmData[3]);
        LocalTime time = strToLocalTime(alarmData[4]);
        String notes = alarmData[5];

        return new AlarmOff(name, level, status, date, time, notes);

    }

    /**
     * Writes csvObject on csv file as a new record.
     * @param pathCsvFile file on wich write operation is performed.
     * @param csvObject {@link CsvWritable} object that has to be written on csv file.
     * @throws IOException wrong pathFile.
     */
    static void csvWriteRecord(String pathCsvFile, CsvWritable csvObject) throws IOException {
        FileWriter csvFileWriter = new FileWriter(pathCsvFile, true);
        String endLine = "\n";
        if(csvObject instanceof Vitals)
            endLine = "," + LocalDate.now().toString() + "," + LocalTime.now().toString() + endLine;
        csvFileWriter.write(csvObject.toCsv().concat(endLine));
        csvFileWriter.flush();
        csvFileWriter.close();
    }

    // GENERIC TEXT FILE INTERACTIONS

    /**
     * Write {@link String} str on file.
     * @param pathFile file on wich write operation is performed.
     * @param str {@link String} that has to be written on file.
     * @param append if true, str is written at the end of file, else at the start.
     * @throws IOException wrong pathFile.
     */
    static void write(String pathFile, String str, boolean append) throws IOException {
        FileWriter fileWriter = new FileWriter(pathFile, append);
        fileWriter.write(str);
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * Write {@link String} str on file, always at the end of it.
     * @param pathFile file on wich write operation is performed.
     * @param str {@link String} that has to be written on file.
     * @throws IOException wrong pathFile.
     */
    static void write(String pathFile, String str) throws IOException {
        write(pathFile, str, true);
    }

    // AUXILIARY METHODS

    /**
     * @param dateStr {@link String} that has to be convertet in LocalDate.
     * @return {@link LocalDate} got from dateStr.
     */
    static LocalDate strToLocalDate(String dateStr) {
        String dateSeparator = "-";
        return LocalDate.of(
            Integer.parseInt(dateStr.split(dateSeparator)[0]),
            Integer.parseInt(dateStr.split(dateSeparator)[1]),
            Integer.parseInt(dateStr.split(dateSeparator)[2])
        );
    }

    /**
     * @param timeStr {@link String} that has to be convertet in LocalTime.
     * @return {@link LocalTime} got from timeStr in hours, minutes and seconds.
     */
    static LocalTime strToLocalTime(String timeStr) {
        String timeSeparator = ":";
        return LocalTime.of(
            Integer.parseInt(timeStr.split(timeSeparator)[0]),
            Integer.parseInt(timeStr.split(timeSeparator)[1]),
            Integer.parseInt((timeStr.split(timeSeparator)[2]).substring(0,2))
        );
    }
}