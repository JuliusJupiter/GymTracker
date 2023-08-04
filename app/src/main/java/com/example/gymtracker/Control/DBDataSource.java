package com.example.gymtracker.Control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBDataSource {

    private static final String LOG_TAG = DBDataSource.class.getSimpleName();

    public static SQLiteDatabase database;
    private DBHelper dbHelper;

    public DBDataSource(Context context){
        Log.d(LOG_TAG, "Die DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new DBHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }
}
