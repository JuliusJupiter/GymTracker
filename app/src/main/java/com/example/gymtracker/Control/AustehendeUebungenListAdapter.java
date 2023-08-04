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

import com.example.gymtracker.Model.Uebung;
import com.example.gymtracker.R;

import java.util.ArrayList;

public class AustehendeUebungenListAdapter extends ArrayAdapter<Uebung> {

    private Context context;
    private ArrayList<Uebung> ausstehendeUebungen;

    public AustehendeUebungenListAdapter(@NonNull Context context, ArrayList<Uebung> ausstehendeUebungen) {
        super(context, R.layout.ausstehendeuebungen_list_item , ausstehendeUebungen );
        this.ausstehendeUebungen = ausstehendeUebungen;
        this.context = context;

    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.ausstehendeuebungen_list_item, parent, false);
        }

        // Ab hier: Anpassungen am Layout
        TextView naechsteUbungTextView = convertView.findViewById(R.id.naechsteUebungItem);
        naechsteUbungTextView.setText(ausstehendeUebungen.get(position).getName() );


        return convertView;
    }

    @Override
    public int getCount() {
        return ausstehendeUebungen.size();
    }

    @Override
    public Uebung getItem(int position) {
        return ausstehendeUebungen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
