package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author mizukami
 * Encapsulates an {@link Alarm}, contains notes of operations performed to resolve it.
 */
public class AlarmOff extends Alarm {
    private String notes;

    public AlarmOff(String name, int level, char status, LocalDate date, LocalTime time, String notes){
        super(name, level, status, date, time);
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toString() {
        String strStatus = (getStatus() == ALARM_ON)? "Acceso" : ( (getStatus() == ALARM_OFF_INT)? "Spento in tempo" : "Spento fuori tempo" );
        return
                (
                        "\n\tNome allarme: " + getName() +
                        "\n\tLivello allarme: " + getLevel() +
                        "\n\tStatus allarme: " + strStatus +
                        "\n\tData dell'allarme: " + getDate() +
                        "\n\tOra dell'allarme: " + getTime() +
                        "\n\nOperazioni effettuate: " + notes
                );
    }
}
