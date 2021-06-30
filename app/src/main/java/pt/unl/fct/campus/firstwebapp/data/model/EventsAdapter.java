package pt.unl.fct.campus.firstwebapp.data.model;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.ClipData;
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
import android.widget.ArrayAdapter;
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
import androidx.recyclerview.widget.RecyclerView;

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
import pt.unl.fct.campus.firstwebapp.ui.login.Main_Page;

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
      // itemView = (itemView == null) ? inflater.inflate(layout, null): itemView;

       ViewHolder viewHolder;

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(layout, parent, false);
            viewHolder = new ViewHolder(itemView);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }

        EventData2 event = events.get(position);


       // viewHolder.itemName.setText(currentItem.getItemName());
        //viewHolder.itemDescription.setText(currentItem.getItemDescription());

        eventViewModel = new ViewModelProvider(owner, new EventViewModelFactory(((LoginApp) context.getApplication()).getExecutorService()))
               .get(EventViewModel.class);


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

           //byte[] decodedString = Base64.decode(image1, Base64.URL_SAFE);
           byte[] decodedString = Base64.decode(image1, Base64.DEFAULT);
           Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

           viewHolder.image.setImageBitmap(decodedByte);
       }


        if( viewHolder.doParticipate != null) {

            viewHolder.doParticipate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventViewModel.participate(token, event.getEventId());
                }
            });


            viewHolder.removeParticipation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventViewModel.removeParticipation(token, event.getEventId());
                    openPage();
                }
            });

        }

        if(viewHolder.removeEvent != null) {
            viewHolder.removeEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String eventId = String.valueOf(event.getEventId());
                    eventViewModel.doRemoveEvent(eventId, token);

                    openPage();
                }
            });
        }

       /* eventViewModel.getLoginResult().observe((LifecycleOwner) this, new Observer<EventResult>() {

            @Override
            public void onChanged(@Nullable EventResult eventResult) {
                if (eventResult == null) {
                    return;
                }

                if (eventResult.getError() != null) {
                    showMessage(String.valueOf(eventResult.getError()));
                }
                if (eventResult.getSuccess() != null) {
                    showMessage("Congrats, you are now volunteering");
                }
            }
        }); */

        viewHolder.showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsActivity();
            }
        });



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

        intent1.putExtras(params);

        context.startActivity(intent1);

    }

    public void showMessage( String errorString) {
            Toast.makeText(context.getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
            openPage();

    }


    public void openPage(){

        Intent intent1 = new Intent(context, Main_Page.class);

        params = intent.getExtras();

        context.startActivity(intent1);

    }
}
