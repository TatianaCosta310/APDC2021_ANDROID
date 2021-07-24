package pt.unl.fct.campus.firstwebapp.data.Events;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pt.unl.fct.campus.firstwebapp.GoogleMaps.MapsActivity;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Constantes;
import pt.unl.fct.campus.firstwebapp.data.model.CustomListAdapter;
import pt.unl.fct.campus.firstwebapp.data.model.EventData;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.UpcomingEventsArgs;


public class SeeParticipatingEvents extends AppCompatActivity implements Constantes {

    private EventViewModel eventViewModel;

    private ArrayList<EventData2> eventsList = new ArrayList<>();

    private List<EventData2> list;


    EventData2 event = null;

    ListView listView;

    Bundle params;

    TextView text;

    String token,tokenEvent;

    Intent oldIntent;


    Boolean getEvent;

    Button changePlaceButton,removeParticipation;

    int i,page = 1,limit = 10;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewadapter);

        listView =  findViewById(R.id.listView);

        ArrayList<EventData2> listCard = new ArrayList<>();
        CustomListAdapter adapter = new CustomListAdapter(SeeParticipatingEvents.this, R.layout.card_view, listCard);

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
        changePlaceButton.setVisibility(View.INVISIBLE);

        text = findViewById(R.id.textListEvents);
        text.setText(" Interested Events ");
        getEvent = false;

        eventViewModel = new ViewModelProvider(this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(EventViewModel.class);

        token = params.getString("token");

        list = new ArrayList<>();


        eventViewModel.seeEvent(token, token, "participating",null);

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
                        tokenEvent = model.getToken();

                        String [] s = model.getListP();

                        Type fooType = new TypeToken<ArrayList<EventData2>>() {}.getType();

                        list = gson.fromJson(s[0],fooType);

                    } else {

                        EventCreatedView model = eventResult.getSuccess();

                        event = gson.fromJson(model.getJsonObject(), EventData2.class);
                        listCard.add(event);
                    }

                    if (list != null) {
                        if (list.size() == 0) {
                            // alerta a dizer que nao existem Eventos ainda !
                            AlertDialog.Builder alert = new AlertDialog.Builder(SeeParticipatingEvents.this);
                            alert.setTitle("no events");
                            alert.setMessage("You are not Participating on any Event yet ");

                        } else {

                            EventLocationResponse  eventData2;

                            if(i == 0)
                                doSomething();

                            if(i <= list.size() - 1) {

                                //vai buscar os eventos completos (dados todos)
                                //  eventData2 = gson.fromJson(list.get(i).toString(), EventLocationResponse .class);

                                eventViewModel.getEvent(list.get(i).getEventId(), token);

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

                Intent intent = new Intent(SeeParticipatingEvents.this , MapsActivity.class);

                Intent oldIntent = getIntent();

                if(oldIntent != null) {
                    params = oldIntent.getExtras();
                    params.putString("Page", "SeeParticipatingEvents");

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

        eventViewModel = new ViewModelProvider(SeeParticipatingEvents.this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(EventViewModel.class);
    }

    /*public void addElements(List<JsonObject> m){
        for (JsonObject o : m)
            list.add(o);

    }*/

    public void  openFullEventPage(EventData2 event) {
        Intent intent = new Intent(getApplicationContext(), SeeFullEventPage.class);

        Intent oldIntent = getIntent();

        if (oldIntent != null) {
            params = oldIntent.getExtras();

            if (params != null)
                params.putString("Page", "SeeParticipatingEvents");
            params.putString("Event", gson.toJson(event));
            intent.putExtras(params);
        } else {
            params = new Bundle();
        }

        startActivity(intent);
    }

}


