package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Patient hospitalized or that has been hospitalized.
 * Class CsvWritable.
 * @author mizukami
 */
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

    /**
     * Sets all registry fields null, used during the loading of a medical records, when registry csv file is not found.
     */
    public Patient(){
        new Patient(null, null, null, null, null);
    }

    public String getCf(){
        return cf;
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

    void setDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }

    ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    void addPrescription(Prescription prescription){
        prescriptions.add(prescription);
    }

    void setPrescriptions(ArrayList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    ArrayList<Administration> getAdministrations() {
        return administrations;
    }

    void addAdministration(Administration administration){
        administrations.add(administration);
    }

    void setAdministrations(ArrayList<Administration> administrations) {
        this.administrations = administrations;
    }

    /**
     * Used to check if {@link Patient} is initialized with registry.
     * @return true if all patient's registry field are null.
     */
    boolean hasRegistry(){
        return
                (super.getName() != null) ||
                (super.getSurname() != null) ||
                (cf != null) ||
                (pob != null) ||
                (dob != null);
    }

    public String toString(){
        return
                (
                 "\nNome: " + super.getName() +
                 "\nCognome: " + super.getSurname() +
                 "\nCodice fiscale: " + cf +
                 "\nLuogo di nascita: " + pob +
                 "\nData di nascita: " + dob
                );
    }

    public String toCsv() {
        return super.getName() + "," + super.getSurname() + "," + cf + "," + pob + "," + dob;
    }

    public static String csvFormat(){
        return "name,surname,cf,pob,dop";
    }
}
