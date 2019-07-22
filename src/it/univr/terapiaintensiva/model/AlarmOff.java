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
        String strStatus = (getStatus() == ALARM_ON)? "On" : ( (getStatus() == ALARM_OFF_INT)? "Off in time" : "Off out of time" );
        return
                (
                        "\nName: " + getName() +
                        "\nLevel: " + getLevel() +
                        "\nStatus: " + strStatus +
                        "\nDate: " + getDate() +
                        "\nTime: " + getTime() +
                        "\nNotes: " + notes
                );
    }
}
