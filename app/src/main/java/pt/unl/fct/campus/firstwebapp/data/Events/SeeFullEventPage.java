package pt.unl.fct.campus.firstwebapp.data.Events;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.unl.fct.campus.firstwebapp.GoogleMaps.MapsActivity;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Constantes;
import pt.unl.fct.campus.firstwebapp.data.model.CommentObject;
import pt.unl.fct.campus.firstwebapp.data.model.CommentObject2;
import pt.unl.fct.campus.firstwebapp.data.model.CustomListAdapter;
import pt.unl.fct.campus.firstwebapp.data.model.CustomListAdapterComments;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.LoadCommentsResponse;
import pt.unl.fct.campus.firstwebapp.ui.login.Main_Page;

public class SeeFullEventPage extends AppCompatActivity implements Constantes {

    private EventViewModel eventViewModel;

    private ListView listView;

    private JsonObject list;

    List<CommentObject2> listCard;

    ImageView imageView,organizerpicView;

    TextView eventName,owner,description,where,when,until,numMaxVol,numIntered,commentBoard;
    EditText commentText;
    Button showOnMap,addComment,loadComents,participate,remove;

    Bundle params;

    String message,tokenEvent;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_full_event);

        listView =  findViewById(R.id.listView2);

        listCard = new ArrayList<>();
        CustomListAdapterComments adapter = new CustomListAdapterComments(SeeFullEventPage.this, R.layout.comments_card_view, (ArrayList<CommentObject2>) listCard);

        imageView = findViewById(R.id.imageEvent1);
        organizerpicView = findViewById(R.id.imageEventCard3);

        commentText = findViewById(R.id.CommentText);
        commentBoard = findViewById(R.id.textShowComments);

        eventName = findViewById(R.id.textShowEvent);
        owner = findViewById(R.id.textOwner);
        description = findViewById(R.id.textDescription);
        where = findViewById(R.id.Where);
        when = findViewById(R.id.When);
        until = findViewById(R.id.Until);
        numMaxVol = findViewById(R.id.maxVolunt);
        numIntered = findViewById(R.id.interested);

        showOnMap = findViewById(R.id.ButtonShowOnMap);
        addComment = findViewById(R.id.ButtonAddComments);
        loadComents = findViewById(R.id.ButtonShowOnComments);
        participate = findViewById(R.id.ButtonDoParticipate);
        remove = findViewById(R.id.ButtonRemoveEvent);

        Intent oldIntent = getIntent();
         params = oldIntent.getExtras();

        String token =  params.getString("token");

        String type = params.getString("Page");

        tokenEvent = params.getString("tokenEvent");

        if(type.equalsIgnoreCase("SeeEventsFinished")){
            addComment.setVisibility(View.INVISIBLE);
            loadComents.setVisibility(View.INVISIBLE);
            participate.setVisibility(View.INVISIBLE);
            addComment.setVisibility(View.INVISIBLE);
            commentText.setVisibility(View.INVISIBLE);
            commentBoard.setVisibility(View.INVISIBLE);

        }

        if(type.equalsIgnoreCase("SeeMyEvents")) {
            remove.setVisibility(View.VISIBLE);
        }

        EventData2 eventData = gson.fromJson(params.getString("Event"), EventData2.class);

       Coords coords =  eventData.getEventCoords();

        eventName.setText(eventData.getName());
        owner.setText(eventData.getOrganizer());
        description.setText(eventData.getDescription());
        where.setText(eventData.getEventAddress());
        when.setText(eventData.getStartDate()  );
        until.setText(eventData.getEndDate() );
        numMaxVol.setText(String.valueOf(eventData.getVolunteers()));
        numIntered.setText(String.valueOf(eventData.getCurrentParticipants()));

        String image = eventData.getImages();
        String organizerCover = eventData.getImgUrl();

        File f1;
        String[] split1;
        Storage storage1;
        Bitmap bitmap;

        if(image != null){
            split1 = image.split("/");

            f1 = createNewFile(this,split1[4]);

            storage1 = getStorage(BLOB_ID_PROJECT);

           bitmap = getBitmap(storage1,BLOB_ID_PROJECT,split1[4],f1);

           imageView.setImageBitmap(bitmap);

        }

        if(organizerCover != null) {
            split1 = organizerCover.split("/");

            f1 = createNewFile(this,split1[4]);

            storage1 = getStorage(BLOB_PIC_ID_PROJECT);

            bitmap = getBitmap(storage1,BLOB_PIC_ID_PROJECT,split1[4],f1);

            organizerpicView.setImageBitmap(bitmap);

        }

        eventViewModel = new ViewModelProvider(this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(EventViewModel.class);


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "Removing";
                eventViewModel.doRemoveEvent(String.valueOf(eventData.getEventId()),token);
            }
        });

        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsActivity(coords);
            }
        });

        participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "Now Participating";
                eventViewModel.participate(token, eventData.getEventId());
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();

                CommentObject commentObject = new CommentObject(eventData.getEventId(),commentText.getText().toString(),
                                                                cal.getTime().toString(),0);

                eventViewModel.postComment(token, commentObject);

                message = "Post";
            }
        });


        loadComents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "loaded";

                eventViewModel.loadComments(token, eventData.getEventId(),"");
            }
        });

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

                    if (message.equals("Post")) {
                        showMessage("Comment added");
                        eventViewModel.loadComments(token, eventData.getEventId(), "");

                    }else if(message.equals("Removing")){

                        showMessage("Event Removed");
                        Intent intent = new Intent(getApplicationContext(), Main_Page.class);
                        intent.putExtras(params);
                        startActivity(intent);


                    }else if(message.equals("loaded")){

                        EventCreatedView model = eventResult.getSuccess();

                        list = model.getJsonObject();

                        LoadCommentsResponse result = gson.fromJson(list, LoadCommentsResponse .class);

                        if (result.getList().size() == 0) {
                            // alerta a dizer que nao existem comentarios neste evento ainda !
                            AlertDialog.Builder alert = new AlertDialog.Builder(SeeFullEventPage.this);
                            alert.setTitle("no comments");
                            alert.setMessage("There aren't Comments  yet ");

                        } else {

                            listView.setVisibility(View.VISIBLE);
                            listCard = result.getList();
                            listView.setAdapter(adapter);

                        }
                    } else {
                        showMessage(message);
                    }
                }
            }
        });


    }

    public void openMapsActivity(Coords coords){

        Intent intent1 = new Intent(this, MapsActivity.class);

        params.putDouble("latitude", coords.getLat());
        params.putDouble("longitude", coords.getLng());
        params.putString("Next_Null","next_null");

        intent1.putExtras(params);
        startActivity(intent1);

    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private File createNewFile(Context context,String image){

        File file = new File(context.getCacheDir(), image + ".png");
        try {
            file.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return file;
    }

    private Storage getStorage(String id) {

        Storage storage = StorageOptions.newBuilder()
                .setProjectId(id)
                .build()
                .getService();

        return storage;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap getBitmap(Storage storage, String id, String name, File f){
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                try {
                    Blob blob = storage.get(BlobId.of(id, name));
                    blob.downloadTo(Paths.get(f.toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        byte[] decodedString = new byte[0];
        try {
            decodedString = Files.readAllBytes(f.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }
}