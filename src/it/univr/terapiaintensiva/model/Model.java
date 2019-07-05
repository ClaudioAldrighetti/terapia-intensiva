package it.univr.terapiaintensiva.model;

import java.io.*;

public class Model {

    private BufferedReader autenticationFile;
    private final String pathAutenticationFile = "../../assets/login.csv";

    // Constructor
    public Model() {
        autenticationFile = null;
        //TODO
    }

    // UC1
    public char autenticate(String username, String password) throws IOException {
        try {
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Return error
            return 'e';
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
}
