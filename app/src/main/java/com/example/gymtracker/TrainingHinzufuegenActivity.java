package com.example.gymtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtracker.Control.DBDataSource;
import com.example.gymtracker.Model.AlleUebungen;
import com.example.gymtracker.Model.Satz;
import com.example.gymtracker.Model.Training;
import com.example.gymtracker.Model.Uebung;
import com.example.gymtracker.Model.User;

import java.util.ArrayList;

public class TrainingHinzufuegenActivity extends AppCompatActivity {
    private ArrayList<Uebung> hinzugefuegteUbungen = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_hinzufuegen);

        EditText trainingsbezeichnung = (EditText) findViewById(R.id.trainingsbezeichnungTextEdit);
        MultiAutoCompleteTextView uebungsAuswahl = (MultiAutoCompleteTextView) findViewById(R.id.multiCompleteTextUebungen);
        EditText gewichtsTextEdit = (EditText) findViewById(R.id.editTextStartGewicht);
        EditText wdhTextEdit = (EditText) findViewById(R.id.editTextAngestrebteWdh);
        Button wenigerButton = (Button) findViewById(R.id.buttonWeniger);
        Button mehrButton = (Button) findViewById(R.id.buttonMehr);
        TextView satzAnzahlText = (TextView) findViewById(R.id.SatzAnzahlAnzeige);
        Button hinzufuegenButton = (Button) findViewById(R.id.UebungHinzufuegenButton);
        Button speichernButton  = (Button) findViewById(R.id.speichernButton);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, AlleUebungen.alleErdenklichenUebungen);
        uebungsAuswahl.setAdapter(adapter);
        uebungsAuswahl.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        wenigerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(satzAnzahlText.getText().toString()) > 1) {
                    int weniger = Integer.parseInt(satzAnzahlText.getText().toString()) - 1;
                    satzAnzahlText.setText(String.valueOf(weniger));
                }
            }
        });

        mehrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mehr = Integer.parseInt(satzAnzahlText.getText().toString()) +1 ;
                satzAnzahlText.setText(String.valueOf(mehr));
            }
        });

        hinzufuegenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uebungsAuswahl.getText().toString() != "" ) {
                    String uebungsName = uebungsAuswahl.getText().toString().replace(",", "");
                    Uebung uebung = new Uebung(uebungsName, Integer.parseInt(satzAnzahlText.getText().toString()));
                    if (!wdhTextEdit.getText().toString().isEmpty() && !gewichtsTextEdit.getText().toString().isEmpty()) {
                        for (int i = 0; i < Integer.parseInt(satzAnzahlText.getText().toString()); i++) {
                            Satz neuerSatz = new Satz(Integer.parseInt(wdhTextEdit.getText().toString()), Integer.parseInt(gewichtsTextEdit.getText().toString()));
                            uebung.Saetze.add(neuerSatz);
                        }
                    }
                    hinzugefuegteUbungen.add(uebung);
                    Toast.makeText(TrainingHinzufuegenActivity.this, uebungsAuswahl.getText().toString() +" wurde Hinzugefügt", Toast.LENGTH_SHORT).show();

                    gewichtsTextEdit.setText("");
                    wdhTextEdit.setText("");
                    uebungsAuswahl.setText("");
                    uebungsAuswahl.requestFocus();
                }
                return;
            }
        });

        speichernButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trainingsbezeichnung.getText().toString().isEmpty() ){
                    Toast.makeText(TrainingHinzufuegenActivity.this , "Bitte Trage eine Bezeichnung für dieses Training ein!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Training training = new Training(trainingsbezeichnung.getText().toString());
                training.setUebungListe(hinzugefuegteUbungen);

                ContentValues trainingValues = new ContentValues();
                trainingValues.put( "name", trainingsbezeichnung.getText().toString());
                trainingValues.put( "userId" , User.currentUser.getId());
                long insertId = DBDataSource.database.insert( "Training", null, trainingValues);
                training.setId((int)insertId);

                for (Uebung u: hinzugefuegteUbungen) {
                    ContentValues uebungsValues = new ContentValues();
                    uebungsValues.put("name", u.getName().toString());
                    uebungsValues.put("TrainingsId", (int)insertId);
                    long uebungsId = DBDataSource.database.insert("Uebung" , null, uebungsValues);
                    for (Satz s: u.Saetze) {
                        ContentValues satzValues = new ContentValues();
                        satzValues.put("gewicht", s.gewicht);
                        satzValues.put("wdh" , s.wiederholung);
                        satzValues.put( "UbungsId" , (int)uebungsId);
                        DBDataSource.database.insert("Satz", null, satzValues);
                    }

                }
                User.currentUser.addTraining(training);

                Intent goToTrainingsAuswahl = new Intent(TrainingHinzufuegenActivity.this, TrainingsAuswahlActivity.class);
                startActivity(goToTrainingsAuswahl);
            }
        });
    }
}