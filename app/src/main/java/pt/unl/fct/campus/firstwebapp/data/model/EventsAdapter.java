package pt.unl.fct.campus.firstwebapp.data.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.unl.fct.campus.firstwebapp.R;

public class EventsAdapter extends BaseAdapter {

    Activity context;
    ArrayList<EventData2> events; //= new ArrayList<>();
    private static LayoutInflater inflater = null;


    public EventsAdapter(Activity context,ArrayList<EventData2> events){

        this.context = context;
        this.events = events ;

        int size = events.size();

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
        itemView = (itemView == null) ? inflater.inflate(R.layout.finished_events, null): itemView;


        TextView owner = itemView.findViewById(R.id.textOwner);
        TextView name = itemView.findViewById(R.id.textName);
        TextView description = itemView.findViewById(R.id.textDescription);
        TextView from = itemView.findViewById(R.id.textFrom);
        TextView where = itemView.findViewById(R.id.Where);
        TextView when = itemView.findViewById(R.id.When);
        TextView until = itemView.findViewById(R.id.Until);
        TextView numMaxVol = itemView.findViewById(R.id.maxVolunt);
        TextView numParticipants = itemView.findViewById(R.id.interested);


        EventData2 event = events.get(position);

        String numMax = String.valueOf(event.getVolunteers());
        String interested = String.valueOf( event.currentParticipants);

        owner.setText("Organizer: " + event.getOrganizer());
        name.setText(event.getName());
       description.setText("Description: " + event.getDescription());
        from.setText("From: " + event.getLocation());
        where.setText("Where: " + event.getMeetingPlace());
        when.setText("When: " + event.getStartDate() );
        until.setText("Until: " + event.getEndDate());
        numMaxVol.setText("Max Volunteers: " + numMax);
        numParticipants.setText("Interested: " + interested);

        return  itemView;
    }

   /* @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public EventData2 getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.finished_events,null): itemView;

        TextView owner = itemView.findViewById(R.id.textOwner);
        TextView name = itemView.findViewById(R.id.textName);
        TextView description = itemView.findViewById(R.id.textDescription);
        TextView from = itemView.findViewById(R.id.textFrom);
        TextView where = itemView.findViewById(R.id.Where);
        TextView when = itemView.findViewById(R.id.When);
        TextView until = itemView.findViewById(R.id.Until);
        TextView numMaxVol = itemView.findViewById(R.id.maxVolunt);
        TextView numParticipants = itemView.findViewById(R.id.interested);

        ImageView image = itemView.findViewById(R.id.imageEvent);

        EventData2 event = events.get(position);

        String numMax = String.valueOf(event.getVolunteers());
        String interested = String.valueOf( event.currentParticipants);

         owner.setText("Organizer" + event.getOrganizer());
         name.setText(event.getName());
         description.setText("Description" + event.getDescription());
         from.setText("From: " + event.getLocation());
         where.setText("Where: " + event.getMeetingPlace());
         when.setText("When: " + event.getStartDate() );
         until.setText("Until: " + event.getEndDate());
         numMaxVol.setText("Max Volunteers: " + numMax);
         numParticipants.setText("Interested: " + interested);

        //setImageViewWithByteArray(image, event.getImages().getBytes());

        return itemView;
    }


*/



    public static void setImageViewWithByteArray(ImageView view, byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        view.setImageBitmap(bitmap);
    }
}
