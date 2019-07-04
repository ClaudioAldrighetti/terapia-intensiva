package it.univr.terapiaintensiva.model;

import java.time.LocalDate;

public class Prescription {
    private final LocalDate date;
    private final int duration;
    private final String medicine;
    private final int nDoses;
    private final float dose;

    public Prescription(LocalDate date, int duration, String medicine, int nDoses, float dose){
        this.date = date;
        this.duration = duration;
        this.medicine = medicine;
        this.nDoses = nDoses;
        this.dose = dose;
    }

    public String toString() {
        return
               (
                "\nDate of prescription: " + date +
                "\nDuration of prescription: " + duration +
                "\nMedicine: " + medicine +
                "\nDaily doses: " + nDoses +
                "\nQuantity: " + dose +
                "\n"
               );
    }
}
