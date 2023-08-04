package com.example.gymtracker.Control;

import static com.example.gymtracker.R.layout.training_list_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gymtracker.Model.Training;
import com.example.gymtracker.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TrainingsListAdapter extends ArrayAdapter<Training> {
    private Context context;
    private ArrayList<Training> trainings;

    public TrainingsListAdapter(@NonNull Context context, ArrayList<Training> trainingsArrayList) {
        super(context, training_list_item, trainingsArrayList);
        this.context = context;
        this.trainings = trainingsArrayList;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(training_list_item, parent, false);
        }

        TextView trainingsBezeichnungTextView = convertView.findViewById(R.id.trainingsBezeichnungsText);
        Training item = trainings.get(position);
        trainingsBezeichnungTextView.setText(item.trainingsbezeichnung);

        TextView anzahlUebungenText = convertView.findViewById(R.id.anzahlSaetzeText);
        anzahlUebungenText.setText(item.getUebungListe().size() + " Ubungen" );

        return convertView;
    }

    @Override
    public int getCount() {
        return trainings.size();
    }

    @Override
    public Training getItem(int position) {
        return trainings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
