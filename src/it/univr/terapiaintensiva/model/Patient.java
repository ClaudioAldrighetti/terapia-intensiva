package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Patient extends Person {

    private String cf;
    private String pob;
    private LocalDate dob;

    private Vitals vitals;
    private String diagnosis;
    private ArrayList<Prescription> prescriptions;
    private ArrayList<Administration> administrations;

    public Patient(String name, String surname, String cf, String pob, LocalDate dob){
        super(name, surname);
        this.cf = cf;
        this.pob = pob;
        this.dob = dob;
        this.vitals = new Vitals();
        this.diagnosis = new String();
        this.prescriptions = new ArrayList<>();
        this.administrations = new ArrayList<>();
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

    public String getDiagnosis(){
        return this.diagnosis;
    }

    public void addDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
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
