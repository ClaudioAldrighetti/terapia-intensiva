package it.univr.terapiaintensiva.model;

public interface CsvWritable {
    String toCsv();
    static String csvFormat(){
        return "";
    }
}
