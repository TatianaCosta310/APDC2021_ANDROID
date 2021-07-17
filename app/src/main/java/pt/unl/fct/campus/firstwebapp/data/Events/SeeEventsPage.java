package pt.unl.fct.campus.firstwebapp.data.Events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import pt.unl.fct.campus.firstwebapp.GoogleMaps.MapsActivity;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Constantes;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.RecyclerViewAdapter;
import pt.unl.fct.campus.firstwebapp.data.model.UpcomingEventsArgs;


public class SeeEventsPage extends AppCompatActivity implements Constantes {

    private EventViewModel eventViewModel;

    private ArrayList<EventData2> eventsList = new ArrayList<>();

    private List<JsonObject> list;

    //private EventsAdapter adapter;

    EventData2 event = null;

    //ListView listView;

    Bundle params;

    TextView text;

    String token;

    Intent oldIntent;

    Gson gson;

    Boolean getEvent;

    Button changePlaceButton;

    int i,page = 1,limit = 10;

   // Bundle savedInstanceState;

    //-------\\ ---GridViewCard---||

    ProgressBar progressBar;
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {

       // this.savedInstanceState = savedInstanceState;

        super.onCreate(savedInstanceState);
      // setContentView(R.layout.listviewadapter);
        setContentView(R.layout.list_pagination);

        //recyclerViewAdapter = new RecyclerViewAdapter(SeeEventsPage.this,eventsList);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(recyclerViewAdapter);

        i = 0;

        oldIntent = getIntent();
        params = oldIntent.getExtras();

        String location = params.getString("location");
        String postal_code = params.getString("POSTAL_CODE");
        String country_name = params.getString("COUNTRY_NAME");
        String locality = params.getString("LOCALITY");


        UpcomingEventsArgs upcomingEventsArgs = new UpcomingEventsArgs();

        upcomingEventsArgs.setCountry_name(country_name);
        upcomingEventsArgs.setLocality(locality);
        upcomingEventsArgs.setPostal_code(postal_code);


        changePlaceButton = findViewById(R.id.PlaceId);

        text = findViewById(R.id.textListEvents);
        text.setText("Events In " + locality );

        gson = new Gson();
        getEvent = false;

        eventViewModel = new ViewModelProvider(this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(EventViewModel.class);

        token = params.getString("token");

        list = new ArrayList<>();

        nestedScrollView = findViewById(R.id.nested_scroll_view);

        eventViewModel.seeEvent(token, token, "actual",upcomingEventsArgs);

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
                            eventsList.add(event);
                        }

                    if (list != null) {
                        if (list.size() == 0) {
                            // alerta a dizer que nao existem Eventos ainda !
                            AlertDialog.Builder alert = new AlertDialog.Builder(SeeEventsPage.this);
                            alert.setTitle("no events");
                            alert.setMessage("There aren't Events created yet ");

                        } else {

                            if(i == 0)
                                doSomething();

                            if(i <= list.size() - 1) {

                                //vai buscar os eventos completos (dados todos)
                                event = gson.fromJson(list.get(i).toString(), EventData2.class);

                                eventViewModel.getEvent(event.getEventId(), token);

                                i++;

                            }else if(i == list.size()){
                                showEvents(eventsList);
                              }
                            }
                        }
                    }

                    setResult(Activity.RESULT_OK);

                }
            });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(scrollY == v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        changePlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SeeEventsPage.this , MapsActivity.class);

                Intent oldIntent = getIntent();

                if(oldIntent != null) {
                    params = oldIntent.getExtras();
                    params.putString("Page", "SeeEvents");

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

            eventViewModel = new ViewModelProvider(SeeEventsPage.this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                                .get(EventViewModel.class);
        }

    public void addElements(List<JsonObject> m){
        for (JsonObject o : m)
            list.add(o);

    }
        public void showEvents (ArrayList < EventData2 > events){

          /*  listView = findViewById(R.id.listViewEvents);

            adapter = new EventsAdapter(savedInstanceState,true,this, this, this, events, R.layout.actual_events, token, oldIntent);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(SeeEventsPage.this, "click to item: " + position, Toast.LENGTH_SHORT).show();
                }
            });*/


            recyclerViewAdapter = new RecyclerViewAdapter(SeeEventsPage.this,events,params);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(recyclerViewAdapter);
        }

    }


