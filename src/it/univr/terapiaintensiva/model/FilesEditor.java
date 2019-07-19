package it.univr.terapiaintensiva.model;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

public final class FilesEditor {

    // DEFAULT SEPARATORS
    private static String csvSeparator = ",";
    private static String dateSeparator = "-";
    private static String timeSeparator = ":";

    // CSV INTERACTIONS

    // Create csv file and write in first line record fields
    public static void csvCreateFile(String pathCsvFile, String csvFormat) throws IOException {
        Files.createFile(Paths.get(pathCsvFile));
        write(pathCsvFile, csvFormat.concat("\n"));
    }

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

    // Skip next record of csvFile
    public static void csvSkipRecord(BufferedReader csvFile) throws IOException {
        csvFile.readLine();
    }

    public static Patient csvGetPatient(String[] registryData){
        String name = registryData[0];
        String surname = registryData[1];
        String cf = registryData[2];
        String pob = registryData[3];
        LocalDate dob = strToLocalDate(registryData[4]);

        return new Patient(name, surname, cf, pob, dob);
    }

    public static Vitals csvGetVitals(String[] vitalsData){
        int heartBeat = Integer.parseInt(vitalsData[0]);
        double temperature = Double.parseDouble(vitalsData[1]);
        int sbp = Integer.parseInt(vitalsData[2]);
        int dbp = Integer.parseInt(vitalsData[3]);

        return new Vitals(heartBeat, temperature, sbp, dbp);
    }

    public  static Prescription csvGetPrescription(String[] prescriptionData) {
        int duration = Integer.parseInt(prescriptionData[0]);
        String medicine = prescriptionData[1];
        int nDoses = Integer.parseInt(prescriptionData[2]);
        double dose = Double.parseDouble(prescriptionData[3]);
        LocalDate date = strToLocalDate(prescriptionData[4]);
        LocalTime time = strToLocalTime(prescriptionData[5]);

        return new Prescription(duration, medicine, nDoses, dose, date, time);
    }

    public static Administration csvGetAdministration(String[] administrationData) {
        String medicine = administrationData[0];
        double dose = Double.parseDouble(administrationData[1]);
        String notes = (administrationData.length == 5)? administrationData[2] : "";
        LocalDate date = strToLocalDate(administrationData[3]);
        LocalTime time = strToLocalTime(administrationData[4]);

        return new Administration(medicine, dose, notes, date, time);
    }

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
    public static void csvWriteRecord(String pathCsvFile, CsvWritable csvObject) throws IOException {
        csvWriteRecord(pathCsvFile, csvObject, true);
    }

    // Write CsvWritable object on csvFile using fileWriter
/*    public static void csvWriteRecord(FileWriter csvFileWriter, CsvWritable csvObject) throws IOException {
        csvFileWriter.write(csvObject.toCsv().concat("\n"));
    }
*/
    // GENERIC TEXT FILE INTERACTIONS

    // Write string on file using path
    public static void write(String pathFile, String str, boolean append) throws IOException {
        FileWriter fileWriter = new FileWriter(pathFile, append);
        fileWriter.write(str);
        fileWriter.flush();
        fileWriter.close();
    }
    public static void write(String pathFile, String str) throws IOException {
        write(pathFile, str, true);
    }

    // AUXILIARY PRIVATE METHODS

    private static LocalDate strToLocalDate(String dateStr) {
        return LocalDate.of(
            Integer.parseInt(dateStr.split(dateSeparator)[0]),
            Integer.parseInt(dateStr.split(dateSeparator)[1]),
            Integer.parseInt(dateStr.split(dateSeparator)[2])
        );
    }

    private static LocalTime strToLocalTime(String timeStr) {
        return LocalTime.of(
            Integer.parseInt(timeStr.split(timeSeparator)[0]),
            Integer.parseInt(timeStr.split(timeSeparator)[1])
        );
    }
}