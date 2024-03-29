package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Administration given to a patient.
 * Class CsvWritable.
 * @author mizukami
 */
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

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String toString() {
        String timeStr = time.getHour() + ":" + time.getMinute();
        return
                (
                 "\n\tFarmaco somministrato: " + medicine +
                 "\n\tDose somministrata: " + dose +
                 "\n\tNote sulla somministrazione: " + notes +
                 "\n\tData della somministrazione: " + date +
                 "\n\tOra della somministrazione: " + timeStr
                );
    }

    public String toCsv() {
        return medicine + "," + dose + "," + notes + "," + date + "," + time;
    }

    public static String csvFormat() {
        return "medicine,dose,notes,date,time";
    }
}
