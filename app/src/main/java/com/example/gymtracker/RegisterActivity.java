package com.example.gymtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtracker.Control.DBDataSource;
import com.example.gymtracker.Model.User;

public class RegisterActivity extends AppCompatActivity {


    //private DBDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView eMailText = (TextView) findViewById(R.id.editTextRegEmailAddress);
        TextView passwortTextReg = (TextView) findViewById(R.id.editTextRegPassword);
        Button registrierenButtonReg = (Button) findViewById(R.id.registerButtonReg);

        registrierenButtonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (eMailText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie eine E-Mail-Adresse ein.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(eMailText.getText()).matches()) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie eine gültige E-Mail-Adresse ein.", Toast.LENGTH_SHORT).show();
                    return;
                }else if ( userAlreadyRegistered(eMailText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "E-Mail-Adresse ist bereits registriert.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwortTextReg.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie ein Passwort ein.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwortTextReg.getText().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Das Passwort muss mindestens 8 Zeichen lang sein.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String eMail = eMailText.getText().toString();
                    String passwort = passwortTextReg.getText().toString();

                    //Nutzer anlegen
                    ContentValues values = new ContentValues();
                    values.put( "name", eMail);
                    values.put( "passwort" , passwort);
                    long insertId = DBDataSource.database.insert( "User", null, values);
                    User.currentUser = new User( eMail, passwort, (int)insertId );


                    Intent goToTrainingsAuswahl = new Intent( RegisterActivity.this , TrainingsAuswahlActivity.class);
                    startActivity(goToTrainingsAuswahl);
                }

            }
        });



    }
    private boolean userAlreadyRegistered(String email){
        String[] nameAlsArray = { "name" };
        int i  = DBDataSource.database.query( "User" ,nameAlsArray , "name" + "='" + email + "'", null, null, null, null).getCount() ;
        if(i == 1){
            return true;
        }
        return false;
    }

}