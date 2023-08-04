package com.example.gymtracker.Control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "GymTracker.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context){
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        Log.d(LOG_TAG, "DBHElper hat die Datenbank: " + getDatabaseName() + " Erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE User (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, passwort TEXT)");


        db.execSQL("CREATE TABLE Training (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,  userId INTEGER, FOREIGN KEY (userId) REFERENCES User(id))");


        db.execSQL("CREATE TABLE Uebung (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, anzahlSaetze INTEGER, TrainingsId INTEGER, FOREIGN KEY(TrainingsId) REFERENCES Training(id) ON DELETE CASCADE)");


        db.execSQL("CREATE TABLE Satz (id INTEGER PRIMARY KEY AUTOINCREMENT, wdh INTEGER, gewicht INTEGER, UbungsId INTEGER, FOREIGN KEY(UbungsId) REFERENCES Uebung(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
