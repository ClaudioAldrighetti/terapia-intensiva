package it.univr.terapiaintensiva.model;

public class Vitals {
    public int heartBeat;
    public double temperature;
    public int sbp;
    public int dbp;

    public Vitals(int heartBeat, double temperature, int sbp, int dbp){
        this.heartBeat = heartBeat;
        this.temperature = temperature;
        this.sbp = sbp;
        this.dbp = dbp;
    }

    public Vitals(){
    }

    public int getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(int heartBeat) {
        this.heartBeat = heartBeat;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getSbp() {
        return sbp;
    }

    public void setSbp(int sbp) {
        this.sbp = sbp;
    }

    public int getDbp() {
        return dbp;
    }

    public void setDbp(int dbp) {
        this.dbp = dbp;
    }


    public String toString() {
        return
                (
                 "Heartbeat: " + heartBeat +
                 "\nTemperature: " + temperature +
                 "\nSbp: " + sbp +
                 "\nDbp: " + dbp
                );
    }

    public String toCsv() {
        return
                (
                 heartBeat + "," + temperature + "," + sbp + "," + dbp + "\n"
                );
    }
}
