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
        this.diagnosis = "";
        this.vitals = new Vitals();
        this.prescriptions = new ArrayList<>();
        this.administrations = new ArrayList<>();
    }

    public Patient(){
        new Patient(null, null, null, null, null);
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

    public void setDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void addPrescription(Prescription prescription){
        prescriptions.add(prescription);
    }

    public void setPrescriptions(ArrayList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public ArrayList<Administration> getAdministrations() {
        return administrations;
    }

    public void addAdministration(Administration administration){
        administrations.add(administration);
    }

    public void setAdministrations(ArrayList<Administration> administrations) {
        this.administrations = administrations;
    }

    public boolean noRegistry(){
        return
                (super.getName() == null) &&
                (super.getSurname() == null) &&
                (cf == null) &&
                (pob == null) &&
                (dob == null);
    }

    public String toString(){
        return
                (
                 "Name: " + super.getName() +
                 "\nSurname: " + super.getSurname() +
                 "\nCf: " + cf +
                 "\nPlace of birth: " + pob +
                 "\nDate of birth: " + dob
                );
    }

    public String toCsv() {
        return super.getName() + "," + super.getSurname() + "," + cf + "," + pob + "," + dob;
    }

    public static String csvFormat(){
        return "name,surname,cf,pob,dop";
    }
}
