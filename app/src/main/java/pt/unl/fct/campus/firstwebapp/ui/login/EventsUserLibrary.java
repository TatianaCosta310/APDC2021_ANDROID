package pt.unl.fct.campus.firstwebapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.MyEvents;
import pt.unl.fct.campus.firstwebapp.data.Events.SeeParticipatingEvents;

public class EventsUserLibrary extends AppCompatActivity {

    Button seeParticipatingEventsButton,seeCreatedEventsButton;

    private Bundle bundleExtra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.events_library);

        Intent oldIntent = getIntent();
         bundleExtra = new Bundle();

        if(oldIntent != null) {
            bundleExtra = oldIntent.getExtras();

        }


        seeParticipatingEventsButton = findViewById(R.id.ButtonSeeParticipatingEvents);
        seeCreatedEventsButton = findViewById(R.id.ButtonSeeCreatedEvents);


        seeParticipatingEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(bundleExtra, SeeParticipatingEvents.class);
            }
        });

        seeCreatedEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(bundleExtra, MyEvents.class);
            }
        });

    }


    private void openPage(Bundle params, Class c){

        Intent intent = new Intent(this, c);

        Intent oldIntent = getIntent();

        if(oldIntent != null) {
            params = oldIntent.getExtras();
            intent.putExtras(params);
        }

        startActivity(intent);

    }

}
