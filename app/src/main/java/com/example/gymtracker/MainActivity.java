package com.example.gymtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtracker.Control.DBDataSource;
import com.example.gymtracker.Control.DBHelper;
import com.example.gymtracker.Model.Satz;
import com.example.gymtracker.Model.TestDatenForTestingOnly;
import com.example.gymtracker.Model.Training;
import com.example.gymtracker.Model.Uebung;
import com.example.gymtracker.Model.User;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private DBDataSource dataSource;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new DBDataSource(this);
        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();


        TextView emailText = (TextView) findViewById(R.id.editTextTextEmailAddress);
        TextView passwordText  = (TextView) findViewById(R.id.editTextTextPassword);
        Button loginButton  = (Button) findViewById(R.id.loginButton);
        Button registierButton = (Button) findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eMail;
                String passwort = "";

                //Email Überprüfen
                if (emailText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie eine E-Mail-Adresse ein.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText.getText()).matches()) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie eine gültige E-Mail-Adresse ein.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!userExists(emailText.getText().toString() ) ){
                    Toast.makeText(getApplicationContext(), "Nutzer existiert nicht", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    eMail = emailText.getText().toString();

                }

                //Password Überprüfen
                if (passwordText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie ein Passwort ein.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(eMail== null){
                    Toast.makeText(getApplicationContext(), "Die E-mail ist falsch", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!getPWforUser(eMail).equals( passwordText.getText().toString().trim() )){
                    String dbPassword = getPWforUser(eMail);
                    String enteredPassword = passwordText.getText().toString().trim();
                    Log.d("EmailCheck", "Email: " + eMail);
                    Log.d("PasswordCheck", "Database password: " + dbPassword);
                    Log.d("PasswordCheck", "Entered password: " + enteredPassword);
                    Toast.makeText(getApplicationContext(), "Das Passwort ist falsch...", Toast.LENGTH_SHORT).show();
                    return;
                    //md5 für verschlüßelungen
                }else{
                    passwort = passwordText.getText().toString();
                    User.currentUser = new User(eMail, passwort);

                    int userID = getIDforUser(eMail);
                    User.currentUser.setId(userID);

                    String[] columns = {"id", "name", "userId"};
                    String selection = "userId = ?";
                    String[] selectionArgs = {String.valueOf(userID)};
                    Cursor trainingCursor = DBDataSource.database.query("Training", columns, selection, selectionArgs, null, null, null);

                    if (trainingCursor.moveToFirst()) {
                        int trainingIdIndex = trainingCursor.getColumnIndex("id");
                        int trainingNameIndex = trainingCursor.getColumnIndex("name");

                        do {
                            int trainingId = trainingCursor.getInt(trainingIdIndex);
                            String trainingName = trainingCursor.getString(trainingNameIndex);

                            Training training = new Training(trainingName);
                            training.setId(trainingId);
                            User.currentUser.addTraining(training);

                            // Abfrage für alle Übungen mit der bestimmten TrainingsId
                            String[] uebungColumns = {"id", "name", "anzahlSaetze"};
                            String uebungSelection = "TrainingsId = ?";
                            String[] uebungSelectionArgs = {String.valueOf(trainingId)};
                            Cursor uebungCursor = DBDataSource.database.query("Uebung", uebungColumns, uebungSelection, uebungSelectionArgs, null, null, null);

                            if (uebungCursor.moveToFirst()) {
                                int uebungIdIndex = uebungCursor.getColumnIndex("id");
                                int uebungNameIndex = uebungCursor.getColumnIndex("name");
                                int anzahlSaetzeIndex = uebungCursor.getColumnIndex("anzahlSaetze");

                                do {
                                    int uebungId = uebungCursor.getInt(uebungIdIndex);
                                    String uebungName = uebungCursor.getString(uebungNameIndex);
                                    int anzahlSaetze = uebungCursor.getInt(anzahlSaetzeIndex);

                                    Uebung uebung = new Uebung(uebungName, anzahlSaetze);
                                    uebung.setId(uebungId);
                                    training.addUebung(uebung);

                                    String[] satzColumns = {"id", "wdh", "gewicht"};
                                    String satzSelection = "UbungsId = ?";
                                    String[] satzSelectionArgs = {String.valueOf(uebungId)};
                                    Cursor satzCursor = DBDataSource.database.query("Satz", satzColumns, satzSelection, satzSelectionArgs, null, null, null);

                                    if (satzCursor.moveToFirst()) {
                                        int satzWdhIndex = satzCursor.getColumnIndex("wdh");
                                        int satzGewichtIndex = satzCursor.getColumnIndex("gewicht");

                                        do {
                                            int satzWdh = satzCursor.getInt(satzWdhIndex);
                                            int satzGewicht = satzCursor.getInt(satzGewichtIndex);

                                            uebung.Saetze.add(new Satz(satzWdh, satzGewicht));

                                        } while (satzCursor.moveToNext());
                                    }

                                    satzCursor.close();

                                } while (uebungCursor.moveToNext());
                            }

                            uebungCursor.close();

                        } while (trainingCursor.moveToNext());
                    }

                    trainingCursor.close();

                }

                //for Testing
                // User.currentUser = TestDatenForTestingOnly.getTestUser();
                Intent goToTrainingsAuswahl = new Intent( MainActivity.this , TrainingsAuswahlActivity.class);
                startActivity(goToTrainingsAuswahl);

            }
        });

        registierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToRegister=new Intent(MainActivity.this , RegisterActivity.class);
                goToRegister.putExtra("Email", emailText.getText().toString());
                goToRegister.putExtra("passwort", passwordText.getText().toString() );
                startActivity(goToRegister);
            }
        });

    }
    //zum schließen der Datenbankanbindung. Sollte spätestens am ende einmal ausgeführt werden.
    public void closeDataSource(){
        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }
    public boolean userExists(String eMail){
        String[] nameAlsArray = { "name" };
        int i  = DBDataSource.database.query( "User" ,nameAlsArray , "name" + "='" + eMail+ "'", null, null, null, null).getCount() ;
        if(i <= 1){
            return true;
        }
        return false;
    }

    /**
     * Gibt das Passwort für eine bestimmte E-mail addresse zurück. Bei dieser Methode hat ChatGPT mir geholfen. Meinen Ursprünglichen
     * ansatz ist in der getPWforUserAlt methode zu finden.
     * @param eMail
     * @return String Passwort
     */
    private String getPWforUser(String eMail) {
        String[] nameAlsArray = {"passwort"};
        String selection = "name = ?";
        String[] selectionArgs = {eMail};
        Cursor cursor = DBDataSource.database.query("User", nameAlsArray, selection, selectionArgs, null, null, null);

        String password = "";
        if (cursor.moveToFirst()) {
            int pwIndex = cursor.getColumnIndex("passwort");
            if (pwIndex != -1) {
                password = cursor.getString(pwIndex);
            }
        }

        cursor.close();
        return password;
    }

    private int getIDforUser(String eMail) {
        String[] idAlsArray = {"id"};
        String selection = "name = ?";
        String[] selectionArgs = {eMail};
        Cursor cursor = DBDataSource.database.query("User", idAlsArray, selection, selectionArgs, null, null, null);

        int userId = -1; // Standardwert, falls kein Eintrag gefunden wird!!
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            if (idIndex != -1) {
                userId = cursor.getInt(idIndex);
            }
        }

        cursor.close();
        return userId;
    }

    private String getPWforUserAlt(String eMail){
        String[] nameAlsArray = { "name" };
        Cursor cursor = DBDataSource.database.query( "User" ,nameAlsArray , "name" + "='" + eMail + "'", null, null, null, null);
        if(cursor.moveToFirst()){
            Log.d("MoveToFirst succesfull", "Hat geklappt");
        }
        String password = "";
        if (cursor.moveToFirst()) {
            int pwIndex = cursor.getColumnIndex("passwort");
            if (pwIndex != -1) {
                password = cursor.getString(pwIndex);
                Log.d("PasswordInMethode", "Database password: " + password);
            }else {
                Log.d("pwIndex=-1", "pwIndex scheit -1 zu sein");
            }
        }

        cursor.close();
        return password;
    }

}