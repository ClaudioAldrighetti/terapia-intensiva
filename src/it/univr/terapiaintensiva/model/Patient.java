package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Patient extends Person {

    public String cf;
    public String pob;
    public LocalDate dob;

    public Vitals vitals;
    public ArrayList<Prescription> prescriptions;
    public ArrayList<Administration> administrations;

    public Patient(String name, String surname, String cf, String pob, LocalDate dob){
        super(name, surname);
        this.cf = cf;
        this.pob = pob;
        this.dob = dob;
        this.vitals = new Vitals();
        this.prescriptions = new ArrayList<Prescription>();
        this.administrations = new ArrayList<Administration>();
    }

    public String getCf(){
        return cf;
    }

    public String getPob(){
        return pob;
    }

    public LocalDate getDob(){
        return dob;
    }

    public Vitals getVitals() {
        return vitals;
    }

    public void setVitals(Vitals vitals) {
        this.vitals = vitals;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void addPrescription(Prescription prescription){
        prescriptions.add(prescription);
    }

    public ArrayList<Administration> getAdministrations() {
        return administrations;
    }

    public void addAdministration(Administration administration){
        administrations.add(administration);
    }
}
