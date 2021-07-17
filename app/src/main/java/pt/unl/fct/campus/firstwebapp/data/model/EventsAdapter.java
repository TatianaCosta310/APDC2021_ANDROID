package pt.unl.fct.campus.firstwebapp.data.model;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import pt.unl.fct.campus.firstwebapp.GoogleMaps.MapsActivity;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.EventResult;
import pt.unl.fct.campus.firstwebapp.data.Events.EventViewModel;
import pt.unl.fct.campus.firstwebapp.data.Events.EventViewModelFactory;
import pt.unl.fct.campus.firstwebapp.ui.login.EventsUserLibrary;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageOptions;



public class EventsAdapter extends BaseAdapter {

    Activity context;
    ArrayList<EventData2> events;
  // private static LayoutInflater inflater = null;
    LatLng resLatLng = null;
    private EventViewModel eventViewModel;
    int layout;
    String token;
    LifecycleOwner lifeCicleOwner;
    ViewModelStoreOwner viewModelStoreOwner;
    Bundle params;
    Intent intent;
    AlertDialog.Builder alert;
    String participateButton;
    String resultMessage;
    Boolean seeActual;

    Double lat;
    Double longitude;

    EventData2 event;

    //EventsAdapterBaseAdapter adapterBaseAdapter;


    public EventsAdapter(Bundle savedInstanceState,Boolean seeActual,LifecycleOwner lifeCicleOwner, ViewModelStoreOwner viewModelStoreOwner,Activity context, ArrayList<EventData2> events, int layout, String token, Intent intent){


        this.seeActual = seeActual;

        this.context = context;
        this.events = events ;

        this.layout = layout;

        this.token = token;

        this.lifeCicleOwner = lifeCicleOwner;

        this.viewModelStoreOwner = viewModelStoreOwner;

        this.intent = intent;


        alert = new AlertDialog.Builder(context);
        alert.setTitle("error alerts");

      //  adapterBaseAdapter = new EventsAdapterBaseAdapter();
        //adapterBaseAdapter.onCreate(savedInstanceState);

      //  inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        participateButton = "participate";

      //  eventViewModel = new ViewModelProvider(adapterBaseAdapter, new EventViewModelFactory(((LoginApp) adapterBaseAdapter.getApplication()).getExecutorService()))
        //       .get(EventViewModel.class);

       eventViewModel = new ViewModelProvider(viewModelStoreOwner, new EventViewModelFactory(((LoginApp) context.getApplication()).getExecutorService()))
                .get(EventViewModel.class);

    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public EventData2 getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
      // itemView = (itemView == null) ? inflater.inflate(layout, null): itemView;

       ViewHolder viewHolder;

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(layout, parent, false);
            viewHolder = new ViewHolder(itemView);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }

         event = events.get(position);


        //eventViewModel = new ViewModelProvider(viewModelStoreOwner, new EventViewModelFactory(((LoginApp) context.getApplication()).getExecutorService()))
          //     .get(EventViewModel.class);



        //Get String Geocoder Location

        Geocoder coder = new Geocoder(context);

        List<Address> address;

        try {

            address = coder.getFromLocationName(event.getLocation(), 5);

            /*if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                return null;
            }
*/
            if(address.size() > 0) {
                Address location = address.get(0);

                viewHolder.where.setText(location.getAddressLine(0));

                //to show on the  map
                lat = location.getLatitude();
                longitude = location.getLongitude();

                resLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            }

        } catch (IOException ex) {
            ex.printStackTrace();

        }

        String numMax = String.valueOf(event.volunteers);
        String interested = String.valueOf( event.currentParticipants);


        viewHolder.owner.setText(event.getOrganizer());
        viewHolder.name.setText(event.getName());
        viewHolder.description.setText(event.getDescription());

        viewHolder.when.setText( event.getStartDate() );
        viewHolder.until.setText( event.getEndDate());
        viewHolder.numMaxVol.setText( numMax);
        viewHolder.numParticipants.setText(interested);


       String image1 =  event.getImages();

       if(image1 != null) {

           File f = new File(context.getCacheDir(), event.getName() + ".png");
           try {
               f.createNewFile();
           } catch (IOException ioException) {
               ioException.printStackTrace();
           }

           String [] split = image1.split("/");

           Storage storage  = StorageOptions.newBuilder()
                         .setProjectId("daniel1624401699897")
                       // .setCredentials(GoogleCredentials.fromStream(new FileInputStream(f)))
                         .build()
                         .getService();


           Thread thread = new Thread(new Runnable() {
               @Override
               public void run() {

                   try  {
                       Blob blob = storage.get(BlobId.of("daniel1624401699897", split[4]));
                       blob.downloadTo(Paths.get(f.toString()));

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           });

           thread.start();

           byte[] decodedString  = new byte[0];
           try {
               decodedString = Files.readAllBytes(f.toPath());
           } catch (IOException e) {
               e.printStackTrace();
           }

           Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

           viewHolder.image.setImageBitmap(decodedByte);
       }


        if( viewHolder.doParticipate != null) {

            viewHolder.doParticipate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    participateButton = "participate";
                    eventViewModel.participate(token, event.getEventId());
                    resultMessage =  "Now Participating";
                }
            });
        }

        if( viewHolder.removeParticipation != null) {
            viewHolder.removeParticipation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    participateButton = "remove";
                    eventViewModel.removeParticipation(token, event.getEventId());
                    resultMessage = "Participation Removed";
                }
            });
        }

        if(viewHolder.removeEvent != null) {
            viewHolder.removeEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String eventId = String.valueOf(event.getEventId());
                    eventViewModel.doRemoveEvent(eventId, token);
                    resultMessage = "Event removed";

                }
            });
        }


        viewHolder.showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsActivity();
            }
        });


/*        eventViewModel.getLoginResult().observe(adapterBaseAdapter, new Observer<EventResult>() {

            @Override
            public void onChanged(@Nullable EventResult eventResult) {
                if (eventResult == null) {
                    return;
                }

                if (eventResult.getError() != null) {
                   showFailedMessage(eventResult.getError());
                }
                if (eventResult.getSuccess() != null) {

                    openPage(resultMessage + " With Success");

                }

            }
        });*/

            return  itemView;
    }


    private class ViewHolder {

    TextView owner;
    TextView name;
    TextView description;
    TextView where;
    TextView when;
    TextView until;
    TextView numMaxVol;
    TextView numParticipants;


    Button showOnMap;
    Button doParticipate;
    Button removeEvent;
    Button removeParticipation;


    ImageView image;


    public ViewHolder(View view) {
         owner = view.findViewById(R.id.textOwner);
         name = view.findViewById(R.id.textName);
         description = view.findViewById(R.id.textDescription);
         where = view.findViewById(R.id.Where);
         when = view.findViewById(R.id.When);
         until = view.findViewById(R.id.Until);
         numMaxVol = view.findViewById(R.id.maxVolunt);
         numParticipants = view.findViewById(R.id.interested);


         showOnMap = view.findViewById(R.id.ButtonShowOnMap);
         doParticipate = view.findViewById(R.id.ButtonDoParticipate);
         removeEvent = view.findViewById(R.id.ButtonRemoveEvent);
         removeParticipation = view.findViewById(R.id.ButtonDoRemoveParticipate);


         image = view.findViewById(R.id.imageEvent);



    }
}

    public void openMapsActivity(){

        Intent intent1 = new Intent(context,MapsActivity.class);

        params = intent.getExtras();

        params.putDouble("latitude", lat);
        params.putDouble("longitude", longitude);
        params.putString("Next_Null","next_null");

        intent1.putExtras(params);

        context.startActivity(intent1);

    }

    public void openPage(String message){

        alert.setMessage(message);
        alert.show();

        Intent intent1 = new Intent(context, EventsUserLibrary.class);

        params = intent.getExtras();

        context.startActivity(intent1);

    }

    private void showFailedMessage(@StringRes Integer errorString) {
        Toast.makeText(context.getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
