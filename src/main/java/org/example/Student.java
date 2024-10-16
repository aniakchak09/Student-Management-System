package org.example;

import java.util.ArrayList;

public class Student {
    public String nume;
    public double medie;
    public ArrayList<Curs> preferinte;
    public boolean assigned;
    public Student() {}

    public Student(String nume) {
        this.nume = nume;
        this.medie = 0;
        this.preferinte = new ArrayList<>();
        this.assigned = false;
    }

    public void setMedie(double nota) {
        this.medie = nota;
    }

    public String getNume() {
        return this.nume;
    }

    public double getMedie() {
        return this.medie;
    }
}
