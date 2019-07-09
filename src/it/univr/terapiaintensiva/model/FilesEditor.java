package it.univr.terapiaintensiva.model;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

public final class FilesEditor {

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
                    line.split(",") : null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Return last record of csvFile
    public static String[] csvReadLastRecord(BufferedReader csvFile) {
        String lastRecord[] = null;

        String record[] = csvReadRecord(csvFile);
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
        LocalDate date = strToLocalDate(prescriptionData[0]);
        int duration = Integer.parseInt(prescriptionData[1]);
        String medicine = prescriptionData[2];
        int nDoses = Integer.parseInt(prescriptionData[3]);
        double dose = Double.parseDouble(prescriptionData[4]);

        return new Prescription(date, duration, medicine, nDoses, dose);
    }

    public static Administration csvGetAdministration(String[] administrationData) {
        LocalDate date = strToLocalDate(administrationData[0]);
        LocalTime time = strToLocalTime(administrationData[1]);
        String medicine = administrationData[2];
        double dose = Double.parseDouble(administrationData[3]);
        String notes = (administrationData.length == 5)? administrationData[4] : "";

        return new Administration(date, time, medicine, dose, notes);
    }

    // Write CsvWritable object on csvFile using path
    public static void csvWriteRecord(String pathCsvFile, CsvWritable csvObject, boolean append) throws IOException {
        FileWriter csvFileWriter = new FileWriter(pathCsvFile, append);
        csvFileWriter.write(csvObject.toCsv().concat("\n"));
        csvFileWriter.flush();
        csvFileWriter.close();
    }
    public static void csvWriteRecord(String pathCsvFile, CsvWritable csvObject) throws IOException {
        csvWriteRecord(pathCsvFile, csvObject, true);
    }

    // Write CsvWritable object on csvFile using fileWriter
    public static void csvWriteRecord(FileWriter csvFileWriter, CsvWritable csvObject) throws IOException {
        csvFileWriter.write(csvObject.toCsv().concat("\n"));
    }

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
            Integer.parseInt(dateStr.split("-")[0]),
            Integer.parseInt(dateStr.split("-")[1]),
            Integer.parseInt(dateStr.split("-")[2])
        );
    }

    private static LocalTime strToLocalTime(String timeStr) {
        return LocalTime.of(
            Integer.parseInt(timeStr.split(":")[0]),
            Integer.parseInt(timeStr.split(":")[1])
        );
    }

}
