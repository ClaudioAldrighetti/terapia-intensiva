package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author mizukami
 * Prescription of a medicine made by a doctor to a patient.
 * Class CsvWritable.
 */
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

    public int getDuration() {
        return duration;
    }

    public String getMedicine() {
        return medicine;
    }

    public int getnDoses() {
        return nDoses;
    }

    public double getDose() {
        return dose;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String toString() {
        return
                (
                 "\n\tDurata della prescrizione: " + duration + " giorni" +
                 "\n\tFarmaco prescritto: " + medicine +
                 "\n\tDosi giornaliere: " + nDoses +
                 "\n\tQuantità per dose: " + dose + " cl" +
                 "\n\tData della prescrizione: " + date +
                 "\n\tOra della prescrizione: " + time
                );
    }

    public String toCsv() {
        return duration + "," + medicine + "," + nDoses + "," + dose + "," + date + "," + time;
    }

    public static String csvFormat() {
        return "duration,medicine,nDoses,dose,date,time";
    }
}
