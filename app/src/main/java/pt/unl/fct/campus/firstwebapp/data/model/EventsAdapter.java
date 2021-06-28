package pt.unl.fct.campus.firstwebapp.data.model;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import org.w3c.dom.Text;

import java.io.IOException;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

import pt.unl.fct.campus.firstwebapp.GoogleMaps.MapsActivity;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.EventResult;
import pt.unl.fct.campus.firstwebapp.data.Events.EventViewModel;
import pt.unl.fct.campus.firstwebapp.data.Events.EventViewModelFactory;

public class EventsAdapter extends BaseAdapter {

    Activity context;
    ArrayList<EventData2> events; //= new ArrayList<>();
    private static LayoutInflater inflater = null;
    LatLng resLatLng = null;
    private EventViewModel eventViewModel;
    int layout;
    String token;
    ViewModelStoreOwner owner;
    Bundle params;
    Intent intent;

    Double lat;
    Double longitude;

    public EventsAdapter(ViewModelStoreOwner owner,Activity context, ArrayList<EventData2> events, int layout, String token, Intent intent){

        this.context = context;
        this.events = events ;

        this.layout = layout;

        this.token = token;

        this.owner = owner;

        this.intent = intent;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(layout, null): itemView;

        eventViewModel = new ViewModelProvider(owner, new EventViewModelFactory(((LoginApp) context.getApplication()).getExecutorService()))
               .get(EventViewModel.class);

        TextView owner = itemView.findViewById(R.id.textOwner);
        TextView name = itemView.findViewById(R.id.textName);
        TextView description = itemView.findViewById(R.id.textDescription);
        TextView where = itemView.findViewById(R.id.Where);
        TextView when = itemView.findViewById(R.id.When);
        TextView until = itemView.findViewById(R.id.Until);
        TextView numMaxVol = itemView.findViewById(R.id.maxVolunt);
        TextView numParticipants = itemView.findViewById(R.id.interested);


        Button showOnMap = itemView.findViewById(R.id.ButtonShowOnMap);
        Button doParticipate = itemView.findViewById(R.id.ButtonDoParticipate);



        // EXPERIMENTTTT DONEEE

        ImageView image = itemView.findViewById(R.id.imageEvent);

        EventData2 event = events.get(position);

        String numMax = String.valueOf(event.volunteers);
        String interested = String.valueOf( event.currentParticipants);

        //Get String Geocoder Location

        Geocoder coder = new Geocoder(context);

        List<Address> address;
        try {

            address = coder.getFromLocationName(event.getLocation(), 5);

            if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);

            where.setText(location.getAddressLine(0));

            //to show on the  map
             lat = location.getLatitude();
             longitude = location.getLongitude();

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();

        }


        owner.setText(event.getOrganizer());
        name.setText(event.getName());
        description.setText(event.getDescription());

        when.setText( event.getStartDate() );
        until.setText( event.getEndDate());
        numMaxVol.setText( numMax);
        numParticipants.setText(interested);


       String image1 =  event.getImages();

       if(image1 != null) {

           byte[] decodedString = Base64.decode(image1, Base64.URL_SAFE);
           Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

           image.setImageBitmap(decodedByte);
       }


        // EXPERIMENTTTT

        doParticipate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventViewModel.participate(token,event.getEventId());
            }
        });

       /* eventViewModel.getLoginResult().observe((LifecycleOwner) this, new Observer<EventResult>() {

            @Override
            public void onChanged(@Nullable EventResult eventResult) {
                if (eventResult == null) {
                    return;
                }

                if (eventResult.getError() != null) {
                    showMessage(String.valueOf(eventResult.getError().toString()));
                }
                if (eventResult.getSuccess() != null) {
                    showMessage("Congrats, you are now volunteering");
                }
            }
        });*/

        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsActivity();
            }
        });


        return  itemView;
    }



    public void openMapsActivity(){

        Intent intent1 = new Intent(context,MapsActivity.class);

        params = intent.getExtras();

        params.putDouble("latitude", lat);
        params.putDouble("longitude", longitude);

        intent1.putExtras(params);

        context.startActivity(intent1);

    }

    public void showMessage( String errorString) {
            Toast.makeText(context.getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();

    }


    public static void setImageViewWithByteArray(ImageView view, byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        view.setImageBitmap(bitmap);
    }
}
