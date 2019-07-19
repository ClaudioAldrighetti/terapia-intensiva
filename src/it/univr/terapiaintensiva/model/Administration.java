package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Administration implements CsvWritable {
    private final String medicine;
    private final double dose;
    private final String notes;
    private final LocalDate date;
    private final LocalTime time;

    public Administration(String medicine, double dose, String notes, LocalDate date, LocalTime time){
        this.medicine = medicine;
        this.dose = dose;
        this.notes = notes;
        this.date = date;
        this.time = time;
    }

    public Administration(String medicine, double dose, LocalDate date, LocalTime time){
        this.medicine = medicine;
        this.dose = dose;
        this.notes = "";
        this.date = date;
        this.time = time;
    }

    public String toString() {
        String timeStr = time.getHour() + ":" + time.getMinute();
        String noteStr = ( notes.equals("") ? "" : "\n" + notes + "\n");
        return
                (
                 "Medicine: " + medicine +
                 "\nDose: " + dose +
                 "\nNotes: " + noteStr +
                 "\nDate of administration: " + date +
                 "\nTime of administration: " + timeStr
                );
    }

    public String toCsv() {
        return medicine + "," + dose + "," + notes + "," + date + "," + time;
    }

    public static String csvFormat() {
        return "medicine,dose,notes,date,time";
    }
}
