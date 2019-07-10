package it.univr.terapiaintensiva.model;

import java.time.LocalDate;

public class Prescription implements CsvWritable {
    private final LocalDate date;
    private final int duration;
    private final String medicine;
    private final int nDoses;
    private final double dose;

    public Prescription(LocalDate date, int duration, String medicine, int nDoses, double dose){
        this.date = date;
        this.duration = duration;
        this.medicine = medicine;
        this.nDoses = nDoses;
        this.dose = dose;
    }

    public String toString() {
        return
               (
                "Date of prescription: " + date +
                "\nDuration of prescription: " + duration + " days" +
                "\nMedicine: " + medicine +
                "\nDaily doses: " + nDoses +
                "\nQuantity: " + dose
               );
    }

    public String toCsv() {
        return date + "," + duration + "," + medicine + "," + nDoses + "," + dose;
    }

    public static String csvFormat() {
        return "date,duration,medicine,nDoses,dose";
    }
}
