package it.univr.terapiaintensiva.model;

public class Person {
    private final String name;
    private final String surname;

    public Person(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public String toString(){
        return (this.name + " " + this.surname);
    }
}
