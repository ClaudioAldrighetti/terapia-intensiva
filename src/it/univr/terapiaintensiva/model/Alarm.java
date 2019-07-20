package it.univr.terapiaintensiva.model;

public class Alarm implements CsvWritable {

    private String name;
    private int level;

    public Alarm(String name, int level){
        this.name = name;
        this.level = level;
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


    public String toString() {
        return
                (
                 "Name: " + name +
                 "\nLevel: " + level
                );
    }

    public String toCsv() {
        return name + "," + level;
    }

    public static String csvFormat() {
        return "name,level";
    }
}
