package com.example.gymtracker.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.gymtracker.Control.DBDataSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Uebung implements Serializable {
    private final String name;
    private final int anzahlSaetze;
    private int id;
    public ArrayList<Satz> Saetze = new ArrayList<Satz>();

    public Uebung(String name, int anzahlSaetze){
        this.name = name;
        this.anzahlSaetze = anzahlSaetze;
    }

    public void aktualisiereSatze(ArrayList<Satz> Saetze){
        if(Saetze.size() == this.anzahlSaetze){
            //Maybe: alten eintrag in Datenbank speichern.
            this.Saetze =Saetze;

            //Datenbank Aktualisieren: Id´s der Satze finden.
            String[] spalten = {"id"};
            String bedingung = "UbungsId = ?";
            String[] bedingungsArgumente = {String.valueOf(this.id)};

            Cursor cursor = DBDataSource.database.query("Satz", spalten, bedingung, bedingungsArgumente, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                for (int i = 0; i < anzahlSaetze; i++) {
                    int id = cursor.getInt(idIndex);
                    Log.d("Satz Aktuallisiert", "Der " + i + ". Satz der Uebung "+ this.id + " wurde aktualisiert");
                    //update
                    ContentValues satzValues = new ContentValues();
                    satzValues.put("wdh", Saetze.get(i).wiederholung );
                    satzValues.put( "gewicht" , Saetze.get(i).gewicht );
                    DBDataSource.database.update(
                            "Satz",
                            satzValues,
                            "id = ?",
                            new String[]{String.valueOf(id)}
                    );
                    cursor.moveToNext();
                }
            }
            if (cursor != null) {
                cursor.close();
            }


        }
    }

    public int getAnzahlSaetze() {
        return anzahlSaetze;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
