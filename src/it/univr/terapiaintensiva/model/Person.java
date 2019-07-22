package it.univr.terapiaintensiva.model;

/**
 * @author mizukami
 * A person with name and surname.
 * Class CsvWritable.
 */
public class Person implements CsvWritable {
    private String name;
    private String surname;

    public Person(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public Person(){}

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public String toString(){
        return (this.name + " " + this.surname);
    }

    public String toCsv() {
        return name + "," + surname;
    }

    public static String csvFormat() {
        return "name,surname";
    }
}
