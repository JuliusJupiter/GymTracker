package com.example.gymtracker.Control;

import static com.example.gymtracker.R.layout.training_list_item;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gymtracker.Model.Satz;
import com.example.gymtracker.Model.Training;
import com.example.gymtracker.R;
import com.example.gymtracker.TrainingsAblaufActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SaetzeListAdapter extends ArrayAdapter<Satz> {

    private Context context;
    private ArrayList<Satz> saetze;
    private ArrayList<Satz> aktuallisierteSaetze = new ArrayList<>();
    Satz neuerSatz;

    private TrainingsAblaufActivity referensAufActivity;

    public SaetzeListAdapter(@NonNull Context context, ArrayList<Satz> saetze, TrainingsAblaufActivity refAufActivity) {
        super(context, R.layout.saetze_list_item, saetze );
        this.saetze = saetze;
        this.context = context;
        this.referensAufActivity = refAufActivity;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.saetze_list_item, parent, false);
        }

        EditText gewichtTextEdit = convertView.findViewById(R.id.gewichtTextEdit);
        Satz item = saetze.get(position);
        gewichtTextEdit.setText( "" + item.gewicht + "");

        EditText wdhTextEdit = convertView.findViewById(R.id.wiederholungTextEdit);
        wdhTextEdit.setText( "" + item.wiederholung + "");

        TextView gewichtText = (TextView) convertView.findViewById(R.id.gewichtText);
        TextView wdhText = (TextView) convertView.findViewById(R.id.wiederholungenText);
        CheckBox satzabgeschlossenCheckbox = convertView.findViewById(R.id.SatzGeschaftCheckBox);
        satzabgeschlossenCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(satzabgeschlossenCheckbox.isChecked()){
                    gewichtTextEdit.setBackgroundColor(Color.rgb(163,163,163));
                    wdhTextEdit.setBackgroundColor(Color.rgb(163,163,163));
                    gewichtText.setBackgroundColor(Color.rgb(163,163,163));
                    wdhText.setBackgroundColor(Color.rgb(163,163,163));
                    referensAufActivity.startTimer(30);//sekunden könnten Variabel sein und irgendwie eingestellt werden
                    try {
                        int wiederholung = Integer.parseInt(wdhTextEdit.getText().toString());
                        int gewicht = Integer.parseInt(gewichtTextEdit.getText().toString());
                        neuerSatz = new Satz( wiederholung, gewicht);
                        aktuallisierteSaetze.add(neuerSatz);

                    }catch (NumberFormatException nfe){
                        Toast.makeText(context, "Bitte trage Gewicht UND Wiederholungsanzahl ein bevor du die Checkbox antippst", Toast.LENGTH_SHORT).show();
                    }
                    gewichtTextEdit.setEnabled(false);
                    wdhTextEdit.setEnabled(false);
                }else{
                    aktuallisierteSaetze.remove(aktuallisierteSaetze.size() -1);
                    gewichtTextEdit.setEnabled(true);
                    wdhTextEdit.setEnabled(true);
                    gewichtText.setBackgroundColor(Color.TRANSPARENT);
                    gewichtTextEdit.setBackgroundColor(Color.TRANSPARENT);
                    wdhText.setBackgroundColor(Color.TRANSPARENT);
                    wdhTextEdit.setBackgroundColor(Color.TRANSPARENT);
                }
                //ab hier für den Fall, dass es sich um den Letzten Satz dieser Uebung handelt
                if (position == saetze.size() -1){
                    referensAufActivity.naechsteUebung(aktuallisierteSaetze);
                }
            }
        });


        return convertView;
    }

    @Override
    public int getCount() {
        return saetze.size();
    }

    @Override
    public Satz getItem(int position) {
        return saetze.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
