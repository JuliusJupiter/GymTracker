package com.example.gymtracker.Model;

import java.io.Serializable;

public class Satz implements Serializable {
    public int wiederholung;
    public int gewicht;

    public Satz(int wiederholungsAnzahl, int gewicht) {
        this.wiederholung = wiederholungsAnzahl;
        this.gewicht = gewicht;
    }
}
