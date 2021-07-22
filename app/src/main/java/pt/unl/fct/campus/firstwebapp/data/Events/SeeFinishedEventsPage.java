package pt.unl.fct.campus.firstwebapp.data.Events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import pt.unl.fct.campus.firstwebapp.GoogleMaps.MapsActivity;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.CustomListAdapter;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.EventsAdapter;
import pt.unl.fct.campus.firstwebapp.data.model.UpcomingEventsArgs;

public class SeeFinishedEventsPage extends AppCompatActivity {

    private EventViewModel eventViewModel;

    private ArrayList<EventData2> eventsList = new ArrayList<>();

    private List<JsonObject> list;

    //private EventsAdapter adapter;

    EventData2 event = null;

    ListView listView;

    Bundle params;

    TextView text;

    String token;

    Intent oldIntent;

    Gson gson;

    Boolean getEvent;

    Button changePlaceButton;

    int i,page = 1,limit = 10;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewadapter);

        listView =  findViewById(R.id.listView);

        ArrayList<EventData2> listCard = new ArrayList<>();
        CustomListAdapter adapter = new CustomListAdapter(SeeFinishedEventsPage.this, R.layout.card_view, listCard);

        i = 0;

        oldIntent = getIntent();
        params = oldIntent.getExtras();

        Double lat = params.getDouble("Latitude");
        Double lng = params.getDouble("Longitude");
        // String postal_code = params.getString("POSTAL_CODE");
        //String country_name = params.getString("COUNTRY_NAME");
        String locality = params.getString("LOCALITY");


        UpcomingEventsArgs upcomingEventsArgs = new UpcomingEventsArgs();

        upcomingEventsArgs.setLat(lat);
        upcomingEventsArgs.setLng(lng);

        changePlaceButton = findViewById(R.id.PlaceId);

        text = findViewById(R.id.textListEvents);
        text.setText("Events near/in " + locality );

        gson = new Gson();
        getEvent = false;

        eventViewModel = new ViewModelProvider(this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(EventViewModel.class);

        token = params.getString("token");

        list = new ArrayList<>();


        eventViewModel.seeEvent(token, token, "finished",upcomingEventsArgs);

        eventViewModel.getLoginResult().observe(this, new Observer<EventResult>() {
            @Override
            public void onChanged(EventResult eventResult) {
                if (eventResult == null) {
                    return;
                }

                if (eventResult.getError() != null) {

                    //showLoginFailed(loginResult.getError());
                }
                if (eventResult.getSuccess() != null) {

                    if(getEvent == false) {
                        EventCreatedView model = eventResult.getSuccess();

                        addElements(model.getEventsList());

                    } else {

                        EventCreatedView model = eventResult.getSuccess();
                        event = gson.fromJson(model.getJsonObject(), EventData2.class);
                        listCard.add(event);
                    }

                    if (list != null) {
                        if (list.size() == 0) {
                            // alerta a dizer que nao existem Eventos ainda !
                            AlertDialog.Builder alert = new AlertDialog.Builder(SeeFinishedEventsPage.this);
                            alert.setTitle("no events");
                            alert.setMessage("There aren't Events created yet ");

                        } else {

                            EventLocationResponse  eventData2;

                            if(i == 0)
                                doSomething();

                            if(i <= list.size() - 1) {

                                //vai buscar os eventos completos (dados todos)
                                eventData2 = gson.fromJson(list.get(i).toString(), EventLocationResponse .class);

                                eventViewModel.getEvent(eventData2.getEventId(), token);

                                i++;

                            }else if(i == list.size()){


                                listView.setAdapter(adapter);

                            }

                        }
                    }
                }


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EventData2 val = adapter.getItem(position);

                openFullEventPage(val);

            }
        });


        changePlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SeeFinishedEventsPage.this , MapsActivity.class);

                Intent oldIntent = getIntent();

                if(oldIntent != null) {
                    params = oldIntent.getExtras();
                    params.putString("Page", "SeeEventsFinished");

                    if(params != null)
                        intent.putExtras(params);
                } else {
                    params = new Bundle();
                }

                startActivity(intent);

            }
        });

    }

    private void doSomething (){

        getEvent = true;

        eventViewModel = new ViewModelProvider(SeeFinishedEventsPage.this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(EventViewModel.class);
    }

    public void addElements(List<JsonObject> m){
        for (JsonObject o : m)
            list.add(o);

    }

    public void  openFullEventPage(EventData2 event) {
        Intent intent = new Intent(getApplicationContext(), SeeFullEventPage.class);

        Intent oldIntent = getIntent();

        if (oldIntent != null) {
            params = oldIntent.getExtras();

            if (params != null)

                params.putString("Event", gson.toJson(event));
            intent.putExtras(params);
        } else {
            params = new Bundle();
        }

        startActivity(intent);
    }

}

