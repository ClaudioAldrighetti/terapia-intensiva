package it.univr.terapiaintensiva.model;

import java.time.LocalTime;

public class Alarm implements CsvWritable {

    // Const alarm status
    public static final char ALARM_ON = 'n';
    public static final char ALARM_OFF_INT = 'i';
    public static final char ALARM_OFF_OUTT = 'o';

    private String name;
    private int level;
    private char status;
    private LocalTime time;

    public Alarm(String name, int level, char status, LocalTime time){
        this.name = name;
        this.level = level;
        this.status = status;
        this.time = time;
    }

    public Alarm(String name, int level, char status){
        this.name = name;
        this.level = level;
        this.status = status;
        this.time = LocalTime.now();
    }

    public Alarm(String name, int level, LocalTime time){
        this.name = name;
        this.level = level;
        this.status = ALARM_ON;
        this.time = time;
    }

    public Alarm(String name, int level){
        this.name = name;
        this.level = level;
        this.status = ALARM_ON;
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

    public void setLevel(int level) {
        this.level = level;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String toString() {
        String strStatus = (status == ALARM_ON)? "On" : ( (status == ALARM_OFF_INT)? "Off in time" : "Off out of time" );
        return
                (
                 "Name: " + name +
                 "\nLevel: " + level +
                 "\nStatus: " + strStatus +
                 "\nTime: " + time
                );
    }

    public String toCsv() {
        return name + "," + level + "," + status + "," + time;
    }

    public static String csvFormat() {
        return "name,level,status,time";
    }
}
