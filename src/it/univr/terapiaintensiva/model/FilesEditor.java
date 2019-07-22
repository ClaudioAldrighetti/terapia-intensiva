package it.univr.terapiaintensiva.model;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author mizukami
 * Static class used to perform some operations with files and object (especially with csv files and CsvWritable classes).
 */
public final class FilesEditor {

    // DEFAULT SEPARATORS
    private static String csvSeparator = ",";
    private static String dateSeparator = "-";
    private static String timeSeparator = ":";

    // CSV INTERACTIONS

    /**
     * @param pathCsvFile path of new csv file.
     * @param csvFormat format of records.
     * @author mizukami
     * Creates csv file and writes in its first line the format that records must have.
     */
    // Create csv file and write in first line record fields
    public static void csvCreateFile(String pathCsvFile, String csvFormat) throws IOException {
        Files.createFile(Paths.get(pathCsvFile));
        write(pathCsvFile, csvFormat.concat("\n"));
    }

    /**
     * @param csvFile {@link BufferedReader} used to read one record (line) from a csv file.
     * @return an array of {@link String} that contains the values of read record. Null if there isn't a record to read or in case of error.
     * @author mizukami
     * Returns values of read record.
     */
    // Return one record from csvFile as String[]
    public static String[] csvReadRecord(BufferedReader csvFile) {
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
     * @param csvFile {@link BufferedReader} used to read last record (line) from a csv file.
     * @return an array of {@link String} that contains the values of read record. Null in case of error or empty file.
     * @author mizukami
     * Returns values of last record.
     */
    // Return last record of csvFile
    public static String[] csvReadLastRecord(BufferedReader csvFile) {
        String[] lastRecord= null;

        String[] record = csvReadRecord(csvFile);
        while( record != null ){
            lastRecord = record;
            record = csvReadRecord(csvFile);
        }

        return lastRecord;
    }

    /**
     * @param csvFile {@link BufferedReader} of csv file.
     * @throws IOException
     * @author mizukami
     * Skip current record of csv file (usually is used to skip format line).
     */
    // Skip next record of csvFile
    public static void csvSkipRecord(BufferedReader csvFile) throws IOException {
        csvFile.readLine();
    }

    /**
     * @param registryData values read from registry csv file.
     * @return {@link Patient} got from data.
     * @author mizukami
     * Returns {@link Patient} from values in registryData.
     */
    public static Patient csvGetPatient(String[] registryData){
        String name = registryData[0];
        String surname = registryData[1];
        String cf = registryData[2];
        String pob = registryData[3];
        LocalDate dob = strToLocalDate(registryData[4]);

        return new Patient(name, surname, cf, pob, dob);
    }

    /**
     * @param vitalsData values read from vitals csv file.
     * @return {@link Vitals} got from data.
     * @author mizukami
     * Returns {@link Vitals} from values in vitalsData.
     */
    public static Vitals csvGetVitals(String[] vitalsData){
        int heartBeat = Integer.parseInt(vitalsData[0]);
        double temperature = Double.parseDouble(vitalsData[1]);
        int sbp = Integer.parseInt(vitalsData[2]);
        int dbp = Integer.parseInt(vitalsData[3]);

        return new Vitals(heartBeat, temperature, sbp, dbp);
    }

    /**
     * @param vitalsLogData values read from vitals csv file.
     * @return {@link VitalsLog} got from data.
     * @author mizukami
     * Returns {@link VitalsLog} from values in vitalsLogData.
     */
    public static VitalsLog csvGetVitalsLog(String[] vitalsLogData){
        LocalDate date = strToLocalDate(vitalsLogData[4]);
        LocalTime time = strToLocalTime(vitalsLogData[5]);

        return new VitalsLog(csvGetVitals(vitalsLogData), date, time);
    }

    /**
     * @param prescriptionData values read from prescriptions csv file.
     * @return {@link Prescription} got from data.
     * @author mizukami
     * Returns {@link Prescription} from values in prescriptionData.
     */
    public  static Prescription csvGetPrescription(String[] prescriptionData) {
        int duration = Integer.parseInt(prescriptionData[0]);
        String medicine = prescriptionData[1];
        int nDoses = Integer.parseInt(prescriptionData[2]);
        double dose = Double.parseDouble(prescriptionData[3]);
        LocalDate date = strToLocalDate(prescriptionData[4]);
        LocalTime time = strToLocalTime(prescriptionData[5]);

        return new Prescription(duration, medicine, nDoses, dose, date, time);
    }

    /**
     * @param administrationData values read from administrations csv file.
     * @return {@link Administration} got from data.
     * @author mizukami
     * Returns {@link Administration} from values in administrationData.
     */
    public static Administration csvGetAdministration(String[] administrationData) {
        String medicine = administrationData[0];
        double dose = Double.parseDouble(administrationData[1]);
        String notes = (administrationData.length == 5)? administrationData[2] : "";
        LocalDate date = strToLocalDate(administrationData[3]);
        LocalTime time = strToLocalTime(administrationData[4]);

        return new Administration(medicine, dose, notes, date, time);
    }

    /**
     * @param alarmData values read from alarms csv file.
     * @return {@link Alarm} got from data.
     * @author mizukami
     * Returns {@link Alarm} from values in alarmData.
     */
    public static Alarm csvGetAlarm(String[] alarmData){
        String name = alarmData[0];
        int level = Integer.parseInt(alarmData[1]);
        char status = alarmData[2].charAt(0);
        LocalTime time = strToLocalTime(alarmData[3]);

        return new Alarm(name, level, status, time);
    }

    /**
     * @param pathCsvFile file on wich write operation is performed.
     * @param csvObject {@link CsvWritable} object that has to be written on csv file.
     * @param append if true, new record is written at the end of file, else at the start.
     * @throws IOException
     * @author mizukami
     * Writes csvObject on csv file as a new record.
     */
    // Write CsvWritable object on csvFile using path
    public static void csvWriteRecord(String pathCsvFile, CsvWritable csvObject, boolean append) throws IOException {
        FileWriter csvFileWriter = new FileWriter(pathCsvFile, append);
        String endLine = "\n";
        if(csvObject instanceof Vitals)
            endLine = "," + LocalDate.now().toString() + "," + LocalTime.now().toString() + endLine;
        csvFileWriter.write(csvObject.toCsv().concat(endLine));
        csvFileWriter.flush();
        csvFileWriter.close();
    }

    /**
     * @param pathCsvFile file on wich write operation is performed.
     * @param csvObject {@link CsvWritable} object that has to be written on csv file.
     * @throws IOException
     * @author mizukami
     * Writes csvObject on csv file as a new record, always at the end of the file.
     */
    public static void csvWriteRecord(String pathCsvFile, CsvWritable csvObject) throws IOException {
        csvWriteRecord(pathCsvFile, csvObject, true);
    }

    // GENERIC TEXT FILE INTERACTIONS

    /**
     * @param pathFile file on wich write operation is performed.
     * @param str {@link String} that has to be written on file.
     * @param append if true, str is written at the end of file, else at the start.
     * @throws IOException
     * @author mizukami
     * Write {@link String} str on file.
     */
    // Write string on file using path
    public static void write(String pathFile, String str, boolean append) throws IOException {
        FileWriter fileWriter = new FileWriter(pathFile, append);
        fileWriter.write(str);
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * @param pathFile file on wich write operation is performed.
     * @param str {@link String} that has to be written on file.
     * @throws IOException
     * @author mizukami
     * Write {@link String} str on file, always at the end of it.
     */
    public static void write(String pathFile, String str) throws IOException {
        write(pathFile, str, true);
    }

    // AUXILIARY METHODS

    /**
     * @param dateStr {@link String} that has to be convertet in LocalDate.
     * @return {@link LocalDate} got from dateStr.
     * @author mizukami
     */
    public static LocalDate strToLocalDate(String dateStr) {
        return LocalDate.of(
            Integer.parseInt(dateStr.split(dateSeparator)[0]),
            Integer.parseInt(dateStr.split(dateSeparator)[1]),
            Integer.parseInt(dateStr.split(dateSeparator)[2])
        );
    }

    /**
     * @param timeStr {@link String} that has to be convertet in LocalTime.
     * @return {@link LocalTime} got from timeStr in hours, minutes and seconds.
     * @author mizukami
     */
    public static LocalTime strToLocalTime(String timeStr) {
        return LocalTime.of(
            Integer.parseInt(timeStr.split(timeSeparator)[0]),
            Integer.parseInt(timeStr.split(timeSeparator)[1]),
            Integer.parseInt((timeStr.split(timeSeparator)[2]).substring(0,2))
        );
    }
}