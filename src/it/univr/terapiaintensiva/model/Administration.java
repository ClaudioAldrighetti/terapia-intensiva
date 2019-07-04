package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Administration {
    private final LocalDate date;
    private final LocalTime time;
    private final String medicine;
    private final float dose;
    private final String notes;

    public Administration(LocalDate date, LocalTime time, String medicine, float dose, String notes){
        this.date = date;
        this.time = time;
        this.medicine = medicine;
        this.dose = dose;
        this.notes = notes;
    }

    public Administration(LocalDate date, LocalTime time, String medicine, float dose){
//        this.Administration(date, time, medicine, dose, "");
        this.date = date;
        this.time = time;
        this.medicine = medicine;
        this.dose = dose;
        this.notes = "";
    }

    public String toString() {
        String timeStr = time.getHour() + ":" + time.getMinute();
        String noteStr = ( notes.equals("") ? "" : notes + "\n");
        return
                (
                 "\nDate of administration: " + date +
                 "\nTime of administration: " + timeStr +
                 "\nMedicine: " + medicine +
                 "\nDose: " + dose +
                 "\n" + noteStr
                );
    }
}
