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

    ArrayList<CommentObject2> listCard;

    ImageView imageView,organizerpicView;

    TextView eventName,owner,description,where,when,until,numMaxVol,numIntered,commentBoard;
    EditText commentText;
    Button showOnMap,addComment,loadComents, loadmoreComments,participate,remove;


    Bundle params;

    String message,tokenEvent, cursor, image;

    Boolean doParticipate = true;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_full_event);

        listView =  findViewById(R.id.listView2);

        listCard = new ArrayList<>();

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
        loadmoreComments = findViewById(R.id.ButtonLoadMoreComments);
        participate = findViewById(R.id.ButtonDoParticipate);
        remove = findViewById(R.id.ButtonRemoveEvent);

        Intent oldIntent = getIntent();
         params = oldIntent.getExtras();

        String token =  params.getString("token");

        String type = params.getString("Page");

        tokenEvent = params.getString("tokenEvent");

        EventData2 eventData = gson.fromJson(params.getString("Event"), EventData2.class);

        if(eventData.isOwner())
            remove.setVisibility(View.VISIBLE);


        if(type.equalsIgnoreCase("SeeEventsFinished")){

            addComment.setVisibility(View.INVISIBLE);
            loadComents.setVisibility(View.INVISIBLE);
            participate.setVisibility(View.INVISIBLE);
            addComment.setVisibility(View.INVISIBLE);
            commentText.setVisibility(View.INVISIBLE);
            commentBoard.setVisibility(View.INVISIBLE);

        }

        if(type.equalsIgnoreCase("SeeParticipatingEvents")){
            doParticipate = false;
            participate.setText("Remove Participation");

        }

        Coords coords =  eventData.getEventCoords();

        eventName.setText(eventData.getName());
        owner.setText(eventData.getOrganizer());
        description.setText(eventData.getDescription());
        where.setText(eventData.getEventAddress());
        when.setText(eventData.getStartDate()  );
        until.setText(eventData.getEndDate() );
        numMaxVol.setText(String.valueOf(eventData.getVolunteers()));
        numIntered.setText(String.valueOf(eventData.getCurrentParticipants()));

         image = eventData.getImages();
        String organizerCover = eventData.getImgUrl();

        File f1;
        String[] split1;
        Storage storage1;
        Bitmap bitmap = null;


        if(image != null){


            split1 = image.split("/");


            String blob2 = split1[4];

            split1 = split1[5].split("]");


            StringBuffer sb = new StringBuffer(split1[0]);

            sb.deleteCharAt(sb.length()-1);

            image = blob2 + "/" + sb;

         /*   File f = new File(getCacheDir(), image + ".png");
            try {
                f.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


            Storage storage = StorageOptions.newBuilder()
                    .setProjectId(BLOB_ID_PROJECT)
                    .build()
                    .getService();*/

//          f1 = createNewFile(SeeFullEventPage.this.getCacheDir(),"lalala");

        /*    f1 = new File(SeeFullEventPage.this.getCacheDir(), image + ".png");
            try {
                f1.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }*/


            //storage1 = getStorage(BLOB_ID_PROJECT);

          // bitmap = getBitmap(storage1,BLOB_ID_PROJECT,image,f1);



            Storage storage = StorageOptions.newBuilder()
                    .setProjectId(BLOB_ID_PROJECT)
                    .build()
                    .getService();

           File f11 = new File(SeeFullEventPage.this.getCacheDir(), "lala" + ".png");
            try {
                f11.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

                Thread thread = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {

                        try {
                            Blob blob = storage.get(BlobId.of(BLOB_ID_PROJECT, image));
                            blob.downloadTo(Paths.get(f11.toString()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();


                byte[] decodedString = new byte[0];
                try {
                    decodedString = Files.readAllBytes(f11.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


           if(bitmap == null) {

                    File f = f11;

                   Thread thread1 = new Thread(new Runnable() {
                       @RequiresApi(api = Build.VERSION_CODES.O)
                       @Override
                       public void run() {

                           try {
                               Blob blob = storage.get(BlobId.of(BLOB_ID_PROJECT, image));
                               blob.downloadTo(Paths.get(f.toString()));

                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       }
                   });

                   thread1.start();


                   try {
                       decodedString = Files.readAllBytes(f.toPath());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

                   bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

               }
             /*  Storage storage = StorageOptions.newBuilder()
                       .setProjectId(BLOB_ID_PROJECT)
                       .build()
                       .getService();

               File f = new File(SeeFullEventPage.this.getCacheDir(), "lalala" + ".png");
               try {
                   f.createNewFile();
               } catch (IOException ioException) {
                   ioException.printStackTrace();
               }


               Thread thread = new Thread(new Runnable() {
                   @RequiresApi(api = Build.VERSION_CODES.O)
                   @Override
                   public void run() {

                       try {
                           Blob blob = storage.get(BlobId.of(BLOB_ID_PROJECT, blob2 + "/" + image));
                           blob.downloadTo(Paths.get(f.toString()));

                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
               });

               thread.start();


               byte[] decodedString = new byte[0];
               try {
                   decodedString = Files.readAllBytes(f1.toPath());
               } catch (IOException e) {
                   e.printStackTrace();
               }


               bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
*/


            imageView.setImageBitmap(bitmap);

        }

        if(organizerCover != null && organizerCover.contains("google")) {
            split1 = organizerCover.split("/");
            image = split1[4];

          //  f1 = createNewFile(SeeFullEventPage.this.getCacheDir(),split1[4]);
            //storage1 = getStorage(BLOB_PIC_ID_PROJECT);

          /*  bitmap = getBitmap(storage1,BLOB_PIC_ID_PROJECT,split1[4],f1);

            if(bitmap == null)
                bitmap = getBitmap(storage1,BLOB_ID_PROJECT,image,f1);
*/

            Storage storage = StorageOptions.newBuilder()
                    .setProjectId(BLOB_PIC_ID_PROJECT)
                    .build()
                    .getService();

            File f11 = new File(SeeFullEventPage.this.getCacheDir(), "lele" + ".png");
            try {
                f11.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            Thread thread = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {

                    try {
                        Blob blob = storage.get(BlobId.of(BLOB_PIC_ID_PROJECT, image));
                        blob.downloadTo(Paths.get(f11.toString()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();


            byte[] decodedString = new byte[0];
            try {
                decodedString = Files.readAllBytes(f11.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


            if(bitmap == null) {

                File f = f11;

                Thread thread1 = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {

                        try {
                            Blob blob = storage.get(BlobId.of(BLOB_PIC_ID_PROJECT, image));
                            blob.downloadTo(Paths.get(f.toString()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread1.start();


                try {
                    decodedString = Files.readAllBytes(f.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            }

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

                if(doParticipate == false){
                    message = "No longer Participating";
                    eventViewModel.removeParticipation(token, eventData.getEventId());
                }else {
                    message = "Now Participating";
                    eventViewModel.participate(token, eventData.getEventId());
                }
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();

                CommentObject commentObject = new CommentObject(eventData.getEventId(),commentText.getText().toString(),
                                                                cal.getTime().toString());

                eventViewModel.postComment(token, commentObject);

               // listView.setVisibility(View.VISIBLE);

                message = "Post";
            }
        });


        loadComents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "loaded";

                //listView.setVisibility(View.VISIBLE);
                eventViewModel.loadComments(token, eventData.getEventId(),"");
            }
        });


        loadmoreComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "loaded";

                eventViewModel.loadComments(token, eventData.getEventId(),cursor);
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
                       // eventViewModel.loadComments(token, eventData.getEventId(), "");

                    }else if(message.equals("Removing")){

                        showMessage("Event Removed");
                        openPage();

                    }else if(message.equals("loaded")){

                        EventCreatedView model = eventResult.getSuccess();


                        list = model.getJsonObject();

                        LoadCommentsResponse result = gson.fromJson(list, LoadCommentsResponse .class);
                         cursor = result.getCursor();

                        if (result.getList().size() == 0) {
                            // alerta a dizer que nao existem comentarios neste evento ainda !
                            AlertDialog.Builder alert = new AlertDialog.Builder(SeeFullEventPage.this);
                            alert.setTitle("no comments");
                            alert.setMessage("There aren't Comments  yet ");

                        } else {

                            loadmoreComments.setVisibility(View.VISIBLE);


                            if(listCard.size() > 0){
                                ArrayList<CommentObject2> a = (ArrayList<CommentObject2>) result.getList();

                                for(CommentObject2 c: a){
                                    listCard.add(c);

                                }

                            }else{
                                listCard = (ArrayList<CommentObject2>) result.getList();
                            }

                            CustomListAdapterComments adapter = new CustomListAdapterComments(params,SeeFullEventPage.this,eventViewModel,SeeFullEventPage.this, R.layout.comments_card_view,  listCard,token);


                            listView.setAdapter(adapter);

                        }
                   } else {
                        showMessage(message);
                        openPage();
                    }
                }
            }
        });


    }

    private void openPage() {

        Intent intent = new Intent(getApplicationContext(), Main_Page.class);
        intent.putExtras(params);
        startActivity(intent);

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

    private File createNewFile(File f,String image){

        File file = new File(f, image + ".png");
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

        final byte[][] decodedString = {new byte[0]};


        Thread thread = new Thread(new  Runnable() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                byte[] decodedString2 = new byte[0];

                try {
                    Blob blob = storage.get(BlobId.of(id, name));
                    blob.downloadTo(Paths.get(f.toString()));

                    try {
                        decodedString2 = Files.readAllBytes(f.toPath());

                        decodedString[0] = decodedString2;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

      //  thread.setPriority(10);
        thread.start();


        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString[0], 0, decodedString[0].length);

        return decodedByte;
    }
}