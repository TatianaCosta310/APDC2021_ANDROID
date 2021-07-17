package pt.unl.fct.campus.firstwebapp.data.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;

import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.SeeFullEventPage;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static ClickListener clickListener;

    private ArrayList<EventData2> events;
    private Activity activity;
    private Bundle params;
    private  EventData2 event;
    private Gson gson = new Gson();

    public RecyclerViewAdapter(Activity activity,  ArrayList<EventData2> events,Bundle params){
        this.activity = activity;
        this.events = events;
        this.params = params;
        event = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
             event = events.get(position);

            Glide.with(activity).load(event.getImg_cover())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);

            holder.textView.setText(event.getName());
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView imageView;
        TextView textView;



        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            imageView = itemView.findViewById(R.id.imageEventCard);
            textView = itemView.findViewById(R.id.textNameCard);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
            openFullEventPage();

        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            openFullEventPage();
            return false;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerViewAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }


    public void  openFullEventPage() {
        Intent intent = new Intent(activity.getApplicationContext(), SeeFullEventPage.class);

        Intent oldIntent = activity.getIntent();

        if (oldIntent != null) {
            params = oldIntent.getExtras();

            if (params != null)

                params.putString("Event", gson.toJson(event));
                intent.putExtras(params);
        } else {
            params = new Bundle();
        }

        activity.startActivity(intent);
    }
}
