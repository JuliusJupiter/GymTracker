package com.example.gymtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtracker.Control.AustehendeUebungenListAdapter;
import com.example.gymtracker.Control.SaetzeListAdapter;
import com.example.gymtracker.Control.TrainingsListAdapter;
import com.example.gymtracker.Model.Satz;
import com.example.gymtracker.Model.Training;
import com.example.gymtracker.Model.Uebung;

import java.util.ArrayList;
import java.util.Locale;

public class TrainingsAblaufActivity extends AppCompatActivity {


    private Training aktuellesTraining;
    private int aktuelleUeubung = 0;
    private TextView vollendeteUebungText;
    private ListView saetzeListView;
    private ListView ausstehendeUebungListe;
    private TextView aktuelleUebungText;
    private TextView timerText;
    private CountDownTimer timer;
    private boolean timerRunning;

    private SoundPool soundPool;
    private int soundfileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings_ablauf);

       vollendeteUebungText =(TextView) findViewById(R.id.letzteUebungText);
       vollendeteUebungText.setText("");
       timerText = (TextView) findViewById(R.id.timerText);
       aktuellesTraining = (Training) getIntent().getSerializableExtra("training");

       aktuelleUebungText = (TextView) findViewById(R.id.aktuelleUebungText);
       aktuelleUebungText.setText(aktuellesTraining.getUebungListe().get(0).getName() );

       saetzeListView = (ListView) findViewById(R.id.SaetzeEinerUebung);
       SaetzeListAdapter saetzeAdapter = new SaetzeListAdapter( TrainingsAblaufActivity.this, aktuellesTraining.getUebungListe().get(aktuelleUeubung).Saetze, TrainingsAblaufActivity.this );
       saetzeListView.setAdapter(saetzeAdapter);

       ausstehendeUebungListe = (ListView) findViewById(R.id.nochAusstehendeUebungen);
       ArrayList<Uebung> restlicheUebungenListe = new ArrayList<Uebung>( aktuellesTraining.getUebungListe().subList(1, aktuellesTraining.getUebungListe().size() -1));
       AustehendeUebungenListAdapter ausstehendeUebunenAdapter = new AustehendeUebungenListAdapter(TrainingsAblaufActivity.this, restlicheUebungenListe );
       ausstehendeUebungListe.setAdapter( ausstehendeUebunenAdapter);

        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();

        soundfileId = soundPool.load(this, R.raw.ready_set_go, 1);
        /* die mp3 habe ich von https://freesound.org/people/dastudiospr/sounds/529843/
        kostenlos und urheberrechtsfrei bekommen. No Copyright
         */
    }

    public void naechsteUebung(ArrayList<Satz> ergebnisseDieserUebung){
        aktuellesTraining.getUebungListe().get(aktuelleUeubung).aktualisiereSatze(ergebnisseDieserUebung);
        //im Falle, dass noch Uebungen nicht gemacht wurden.
        if(aktuelleUeubung < aktuellesTraining.getUebungListe().size() -1 ) {
            vollendeteUebungText.setText(aktuellesTraining.getUebungListe().get(aktuelleUeubung).getName());
            aktuelleUeubung = aktuelleUeubung + 1;
            aktuelleUebungText.setText(aktuellesTraining.getUebungListe().get(aktuelleUeubung).getName() );
            saetzeListView.setAdapter(new SaetzeListAdapter(TrainingsAblaufActivity.this, aktuellesTraining.getUebungListe().get(aktuelleUeubung).Saetze, TrainingsAblaufActivity.this));
            //update 2. adapter
            ArrayList<Uebung> nochAusstehendeUebungen = new ArrayList<Uebung>( aktuellesTraining.getUebungListe().subList(aktuelleUeubung +1, aktuellesTraining.getUebungListe().size() ));
            ausstehendeUebungListe.setAdapter(new AustehendeUebungenListAdapter(TrainingsAblaufActivity.this, nochAusstehendeUebungen));
        } else /*if ( aktuelleUeubung >= aktuellesTraining.getUebungListe().size()) */ {  //falls alle Übungen gemacht wurden.
            Toast.makeText(getApplicationContext(), "Super, Training vollständig beendet", Toast.LENGTH_SHORT).show();
            Intent goToTrainingsAuswahl = new Intent( TrainingsAblaufActivity.this , TrainingsAuswahlActivity.class);
            startActivity(goToTrainingsAuswahl);
        }


    }

    public void startTimer(int sekunden){
        if(timerRunning){
            timer.cancel();
        }
        timerRunning = true;
        timer = new CountDownTimer(sekunden*1000, 1000) {
            @Override
            public void onTick(long l) {
               int minuten = (int)l /1000 / 60;
               int sekunden = (int)l /1000 % 60;
               String restZeit = String.format(Locale.getDefault(), "%02d:%02d", minuten, sekunden);
               timerText.setText(restZeit);
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                soundPool.play(soundfileId, 1,1,0,0, 1);
            }
        }.start();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}