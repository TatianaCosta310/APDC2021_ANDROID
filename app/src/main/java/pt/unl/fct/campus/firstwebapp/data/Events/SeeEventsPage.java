package pt.unl.fct.campus.firstwebapp.data.Events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.EventsAdapter;

import com.google.firebase.FirebaseApp;

public class SeeEventsPage extends AppCompatActivity {

    private EventViewModel eventViewModel, getEventViewModel;

    private ArrayList<EventData2> eventsList = new ArrayList<>();

    private List<JsonObject> list;

    private EventsAdapter adapter;

    EventData2 event = null;

    ListView listView;

    Bundle params;

    TextView text;

    String token;

    Intent oldIntent;

    Gson gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewadapter);


        text = findViewById(R.id.textListEvents);
        text.setText("Events");

        gson = new Gson();

        eventViewModel = new ViewModelProvider(this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(EventViewModel.class);

        getEventViewModel = new ViewModelProvider(SeeEventsPage.this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(EventViewModel.class);


        oldIntent = getIntent();
        params = oldIntent.getExtras();

        token = params.getString("token");

        list = new ArrayList<>();

        eventViewModel.seeEvent(token, token, "actual");

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

                    EventCreatedView model = eventResult.getSuccess();

                    addElements(model.getEventsList());


                    if (list != null) {
                        if (list.size() == 0) {
                            // alerta a dizer que nao existem Eventos ainda !
                            AlertDialog.Builder alert = new AlertDialog.Builder(SeeEventsPage.this);
                            alert.setTitle("no events");
                            alert.setMessage("There aren't Events created yet ");

                        } else {


                            for (int i = 0; i < list.size(); i++) {
                                //vai buscar os eventos completos (dados todos)
                                event = gson.fromJson(list.get(i).toString(), EventData2.class);
                                eventsList.add(event);
                               // getEventViewModel.getEvent(event.getEventId(), token);
                            }
                        }
                    }


                    showEvents(eventsList);
                    setResult(Activity.RESULT_OK);
                    //Complete and destroy  activity once successful
                    //finish();

      /*  getEventViewModel.getLoginResult().observe(this, new Observer<EventResult>() {

            @Override
            public void onChanged(EventResult eventResult) {
                if (eventResult == null) {
                    return;
                }

                if (eventResult.getError() != null) {

                    //showLoginFailed(loginResult.getError());
                }

                if (eventResult.getSuccess() != null) {

                    EventCreatedView model2 = eventResult.getSuccess();
                    EventData2 eventData2 = gson.fromJson(model2.getJsonObject().toString(), EventData2.class);
                    eventsList.add(eventData2);
                }


            }
        });

*/


                        }

                }
            });

        }

    public void addElements(List<JsonObject> m){
        for (JsonObject o : m)
            list.add(o);
    }
        public void showEvents (ArrayList < EventData2 > events){

            listView = findViewById(R.id.listViewEvents);


            adapter = new EventsAdapter(this, this, this, events, R.layout.actual_events, token, oldIntent);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(SeeEventsPage.this, "click to item: " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


