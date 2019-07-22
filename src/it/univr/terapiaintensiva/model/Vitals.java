package it.univr.terapiaintensiva.model;

/**
 * @author mizukami
 * Contains patient's vital parameters: heart beat, body temperature, sistolic and diastolic pressure.
 * Class CsvWritable.
 */
public class Vitals implements CsvWritable {
    public static final int nullVital = -1;

    private int heartBeat;
    private double temperature;
    private int sbp;
    private int dbp;

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
                 "\nHeartbeat: " + heartBeat +
                 "\nTemperature: " + temperature +
                 "\nSbp: " + sbp +
                 "\nDbp: " + dbp
                );
    }

    public String toCsv() {
        return heartBeat + "," + temperature + "," + sbp + "," + dbp;
    }

    public static String csvFormat() {
        return "heartBeat,temperature,sbp,dbp";
    }
}
