package com.example.gymtracker.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Training implements Serializable {
    public String trainingsbezeichnung;
    private int id;
    private ArrayList<Uebung> uebungListe = new ArrayList<Uebung>();

    public Training(String name){
        this.trainingsbezeichnung = name;
    }

    public ArrayList<Uebung> getUebungListe() {
        return uebungListe;
    }

    public void setUebungListe(ArrayList<Uebung> ListeUebungen){
        uebungListe = ListeUebungen;
    }

    public void addUebung (Uebung uebung){
        this.uebungListe.add(uebung);
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}
