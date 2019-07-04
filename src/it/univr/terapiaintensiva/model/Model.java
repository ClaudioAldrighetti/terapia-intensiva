package it.univr.terapiaintensiva.model;

import java.io.*;

public class Model {

    private FileInputStream autenticationFile;

    // Constructor
    public Model() throws IOException {
        try {
            this.autenticationFile = new FileInputStream("../../assets/login.csv");
        }
        finally {
            if(autenticationFile != null)
                autenticationFile.close();
        }
        //TODO
    }

    // UC1
    public boolean autenticate(String username, String password) throws IOException {
        while(true) {
            // Check if there is an user in this line
            int checklLine = autenticationFile.read();
            if (checklLine == -1)
                return false;

            // Get username from File
            String usr = "";
            char c = (char) checklLine;
            do {
                usr += c;
                c = (char) autenticationFile.read();
            } while (c != ',');

            // Username found
            if (usr.equals(username)) {
                // Check password
                c = (char) autenticationFile.read();
                for (int i = 0; i < password.length(); i++)

                    // Invalid password
                    if (c != password.charAt(i))
                        return false;

                // User verified
                return true;
            }

            // Next line
            do {
                c = (char) autenticationFile.read();
            } while (c != '\n');
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
