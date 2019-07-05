package it.univr.terapiaintensiva.model;

import java.io.*;

public class Model {

    private FileInputStream autenticationFile;
    private String pathAutenticationFile;

    // Constructor
    public Model() {
        autenticationFile = null;
        pathAutenticationFile = "../../assets/login.csv";
        //TODO
    }

    // UC1
    public boolean autenticate(String username, String password) throws IOException {
        try {
            autenticationFile = new FileInputStream(pathAutenticationFile);
            while(true) {
                // Check if it is possible to read an entry from autenticationFile
                if(csvEmptyFileInputStream(autenticationFile)) {
                    autenticationFile.close();
                    return false;
                }

                // Get username from File
                String usr = csvReadStringValue(autenticationFile);

                // User found
                if (usr.equals(username)) {
                    // Get user's password
                    autenticationFile = csvNextValue(autenticationFile);
                    String pswd = csvReadStringValue(autenticationFile);
                    autenticationFile.close();

                    // Check password
                    return pswd.equals(password);
                }

                // Next line
                autenticationFile = csvNextLine(autenticationFile);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UC2
    public boolean createPatient(Patient patient){
        return true; //TODO
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

    // AUSILIAR CSV FILE FUNCTIONS

    // Checks if csv file has something to read
    private boolean csvEmptyFileInputStream(FileInputStream readingFile) throws IOException {
        return (readingFile == null || readingFile.read() == -1);
    }

    // Reads a value from csv file and returns it as a String
    private String csvReadStringValue(FileInputStream readingFile) throws IOException {
        if(csvEmptyFileInputStream(readingFile))
            return null;

        String valueStr = "";
        for(char c=(char)readingFile.read(); c!=',' && c!='\n' ;c=(char)readingFile.read())
            valueStr += c;
        return valueStr;
    }

    // Goes to next value in this csv file
    private FileInputStream csvNextValue(FileInputStream readingFile) throws IOException {
        if(csvEmptyFileInputStream(readingFile))
            return null;

        for(char c = (char)readingFile.read(); c!=',' && c!='\n'; c = (char)readingFile.read());
        return readingFile;
    }

    // Goes to next line in this csv file
    private FileInputStream csvNextLine(FileInputStream readingFile) throws IOException {
        if(csvEmptyFileInputStream(readingFile))
            return null;

        for(char c = (char)readingFile.read(); c!='\n'; c = (char)readingFile.read());
        return readingFile;
    }
}
