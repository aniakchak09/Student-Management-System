package org.example;

import java.util.ArrayList;

public class Curs<Student> {
    public String nume;
    public int capacitate;
    public ArrayList<Student> studenti;

    public Curs(String nume, int capacitate) {
        this.nume = nume;
        this.capacitate = capacitate;
        this.studenti = new ArrayList<>();
    }

    public String getNume() {
        return this.nume;
    }
}
