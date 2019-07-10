package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Administration implements CsvWritable {
    private final LocalDate date;
    private final LocalTime time;
    private final String medicine;
    private final double dose;
    private final String notes;

    public Administration(LocalDate date, LocalTime time, String medicine, double dose, String notes){
        this.date = date;
        this.time = time;
        this.medicine = medicine;
        this.dose = dose;
        this.notes = notes;
    }

    public Administration(LocalDate date, LocalTime time, String medicine, double dose){
//        this.Administration(date, time, medicine, dose, "");
        this.date = date;
        this.time = time;
        this.medicine = medicine;
        this.dose = dose;
        this.notes = "";
    }

    public String toString() {
        String timeStr = time.getHour() + ":" + time.getMinute();
        String noteStr = ( notes.equals("") ? "" : "\n" + notes + "\n");
        return
                (
                 "Date of administration: " + date +
                 "\nTime of administration: " + timeStr +
                 "\nMedicine: " + medicine +
                 "\nDose: " + dose +
                 noteStr
                );
    }

    public String toCsv() {
        return date + "," + time + "," + medicine + "," + dose + "," + notes;
    }

    public static String csvFormat() {
        return "date,time,medicine,dose,notes";
    }
}
