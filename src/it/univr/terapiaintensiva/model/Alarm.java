package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author mizukami
 * Alarm generated by a critical situation registered in an hospitalized patient.
 * Alarms could be on or setted off in time or out of time.
 * Doctors must describe the operations performed on patient to set off an alarm.
 */
public class Alarm implements CsvWritable {

    // Const alarm status
    static final char ALARM_ON = 'n';
    public static final char ALARM_OFF_INT = 'i';
    public static final char ALARM_OFF_OUTT = 'o';

    private String name;
    private int level;
    private char status;
    private LocalDate date;
    private LocalTime time;

    public Alarm(String name, int level, char status, LocalDate date, LocalTime time){
        this.name = name;
        this.level = level;
        this.status = status;
        this.date = date;
        this.time = time;
    }

    public Alarm(String name, int level, char status){
        this.name = name;
        this.level = level;
        this.status = status;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public Alarm(String name, int level, LocalDate date, LocalTime time){
        this.name = name;
        this.level = level;
        this.status = ALARM_ON;
        this.date = date;
        this.time = time;
    }

    public Alarm(String name, int level){
        this.name = name;
        this.level = level;
        this.status = ALARM_ON;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String toString() {
        String strStatus = (status == ALARM_ON)? "Acceso" : ( (status == ALARM_OFF_INT)? "Spento in tempo" : "Spento fuori tempo" );
        return
                (
                 "\n\tNome allarme: " + name +
                 "\n\tLivello allarme: " + level +
                 "\n\tStatus allarme: " + strStatus +
                 "\n\tData dell'allarme: " + date +
                 "\n\tOra dell'allarme: " + time
                );
    }

    public String toCsv() {
        return name + "," + level + "," + status + "," + date + "," + time;
    }

    public static String csvFormat() {
        return "name,level,status,date,time";
    }
}
