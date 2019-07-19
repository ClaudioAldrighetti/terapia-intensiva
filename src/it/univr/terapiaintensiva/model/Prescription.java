package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Prescription implements CsvWritable {
    private final int duration;
    private final String medicine;
    private final int nDoses;
    private final double dose;
    private final LocalDate date;
    private final LocalTime time;

    public Prescription(int duration, String medicine, int nDoses, double dose, LocalDate date, LocalTime time){
        this.duration = duration;
        this.medicine = medicine;
        this.nDoses = nDoses;
        this.dose = dose;
        this.date = date;
        this.time = time;
    }

    public String toString() {
        return
               (
                "Duration of prescription: " + duration + " days" +
                "\nMedicine: " + medicine +
                "\nDaily doses: " + nDoses +
                "\nQuantity: " + dose +
                "\nDate of prescription: " + date +
                "\nTime of prescription: " + time
               );
    }

    public String toCsv() {
        return duration + "," + medicine + "," + nDoses + "," + dose + "," + date + "," + time;
    }

    public static String csvFormat() {
        return "duration,medicine,nDoses,dose,date,time";
    }
}
