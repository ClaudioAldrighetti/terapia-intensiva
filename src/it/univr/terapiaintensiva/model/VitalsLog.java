package it.univr.terapiaintensiva.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class VitalsLog extends Vitals {
    private LocalDate date;
    private LocalTime time;

    public VitalsLog(int heartBeat, double temperature, int sbp, int dbp, LocalDate date, LocalTime time) {
        super(heartBeat, temperature, sbp, dbp);
        this.date = date;
        this.time = time;
    }

    public VitalsLog(Vitals vitals, LocalDate date, LocalTime time) {
        super(vitals.getHeartBeat(), vitals.getTemperature(), vitals.getSbp(), vitals.getDbp());
        this.date = date;
        this.time = time;
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
        return
                (
                        "Heartbeat: " + getHeartBeat() +
                        "\nTemperature: " + getTemperature() +
                        "\nSbp: " + getSbp() +
                        "\nDbp: " + getDbp() +
                        "\nDate: " + date +
                        "\nTime: " + time
                );
    }

    public String toCsv() {
        return getHeartBeat() + "," + getTemperature() + "," + getSbp() + "," + getDbp() + "," + date + "," + time;
    }

    public static String csvFormat() {
        return "heartBeat,temperature,sbp,dbp,date,time";
    }

}
