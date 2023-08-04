package com.example.gymtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.gymtracker.Control.DBDataSource;
import com.example.gymtracker.Control.OnSwipeTouchListener;
import com.example.gymtracker.Control.TrainingsListAdapter;
import com.example.gymtracker.Model.Training;
import com.example.gymtracker.Model.User;

import java.util.ArrayList;

public class TrainingsAuswahlActivity extends AppCompatActivity {

    private User currentUser = User.currentUser;
    ArrayList<Training> trainingsListe = currentUser.getTrainingsOfUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings_auswahl);

        Button neuesTrainingButton = (Button) findViewById(R.id.neuesTrainingButton);
        neuesTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTrainingHinzufuegen = new Intent(TrainingsAuswahlActivity.this, TrainingHinzufuegenActivity.class);
                startActivity(goToTrainingHinzufuegen);
            }
        });
        ListView listViewTrainings = findViewById(R.id.listeMitTrainings);
        TrainingsListAdapter adapter = new TrainingsListAdapter( TrainingsAuswahlActivity.this, currentUser.getTrainingsOfUser());
        
        listViewTrainings.setAdapter(adapter);
        listViewTrainings.setClickable(true);
        listViewTrainings.setElevation(8f);   //das ist geraten und müsste evl. man angepasst werden
        listViewTrainings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent goToTrainingsAblaufActivity = new Intent(TrainingsAuswahlActivity.this, TrainingsAblaufActivity.class);
                goToTrainingsAblaufActivity.putExtra( "training", trainingsListe.get(i) );
                startActivity(goToTrainingsAblaufActivity);

            }
        });

        listViewTrainings.setOnTouchListener(new OnSwipeTouchListener(this, listViewTrainings) {
            @Override
            public void onSwipeLeft(int position) {
                int trainingIdToDelete = trainingsListe.get(position).getId();
                User.currentUser.getTrainingsOfUser().remove(position);

                String selection = "id = ?";
                String[] selectionArgs = {String.valueOf(trainingIdToDelete)};
                DBDataSource.database.delete("Training", selection, selectionArgs);

                adapter.notifyDataSetChanged();
            }
        });
        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setBackgroundColor(Color.rgb(205,55,0));
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.currentUser = null;
                Intent goToLogin = new Intent(TrainingsAuswahlActivity.this, MainActivity.class);
                startActivity(goToLogin);
            }
        });
    }
}