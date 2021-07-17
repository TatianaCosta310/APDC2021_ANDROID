package pt.unl.fct.campus.firstwebapp.data.Events;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Constantes;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;

public class SeeFullEventPage extends AppCompatActivity implements Constantes {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_full_event);

        Intent oldIntent = getIntent();
        Bundle params = oldIntent.getExtras();

        EventData2 eventData = gson.fromJson(params.getString("Event"), EventData2.class);


    }
}