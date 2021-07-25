package pt.unl.fct.campus.firstwebapp.data.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import pt.unl.fct.campus.firstwebapp.GoogleMaps.MapsActivity;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Constantes;
import pt.unl.fct.campus.firstwebapp.data.Events.EventCreatedView;
import pt.unl.fct.campus.firstwebapp.data.Events.EventResult;
import pt.unl.fct.campus.firstwebapp.data.Events.EventViewModel;
import pt.unl.fct.campus.firstwebapp.data.Events.EventViewModelFactory;
import pt.unl.fct.campus.firstwebapp.data.Events.SeeFullEventPage;
import pt.unl.fct.campus.firstwebapp.ui.login.Main_Page;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class CustomListAdapterComments extends ArrayAdapter<CommentObject2> implements Constantes {

    private static final String TAG = "CustomListAdapter";

    private EventViewModel eventViewModel;
    private LifecycleOwner lifecycleOwner;

    private Bundle params;
    private String  imageName,token;

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView userName,date,comment;
        ImageView image;
        Button removeComment;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public CustomListAdapterComments(Bundle params,LifecycleOwner lifecycleOwner, EventViewModel eventViewModel, Context context, int resource, ArrayList<CommentObject2> objects, String token) {
        super(context, resource, objects);

        this.params = params;

      //  this.lifecycleOwner = lifecycleOwner;



        mContext = context;
        mResource = resource;

        this.token = token;

        this.eventViewModel = eventViewModel;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //sets up the image loader library
        // setupImageLoader();

        //get the persons information
        String imgUrl = getItem(position).getUrlProfilePicture();
        String user = getItem(position).getOwnerName();
        String comment =  getItem(position).getComment();
        String date =  getItem(position).getDate();
        Boolean isOwner = getItem(position).isOwner();

        long commentId = getItem(position).getEventid();

        try{

            //create the view result for showing the animation
            final View result;

            //ViewHolder object
            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);
                holder= new ViewHolder();
                holder.userName = convertView.findViewById(R.id.textNameCard2);
                holder.image =  convertView.findViewById(R.id.imageEventCard2);
                holder.comment =  convertView.findViewById(R.id.textNameCard3);
                holder.date =  convertView.findViewById(R.id.textNameCard4);
                holder.removeComment =  convertView.findViewById(R.id.ButtonRemoveComment);

                result = convertView;

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
                result = convertView;
            }


            holder.removeComment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    eventViewModel.deleteComment(token,commentId);

                    /*Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                        openpage();*/

                }
            });



            Animation animation = AnimationUtils.loadAnimation(mContext,
                    (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
            result.startAnimation(animation);
            lastPosition = position;

            holder.userName.setText(user);
            holder.comment.setText(comment);
            holder.date.setText(date);

            if(isOwner)
                holder.removeComment.setVisibility(View.VISIBLE);

            //set the imageCover

            if(imgUrl != null) {



                File f = new File(mContext.getCacheDir(), user + ".png");
                try {
                    f.createNewFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                String[] split = imgUrl.split("/");
                 imageName = imgUrl;

                if(imageName != null && imageName.contains("google")) {
                    imageName = split[4];


                    Storage storage = StorageOptions.newBuilder()
                            .setProjectId(BLOB_PIC_ID_PROJECT)
                            // .setCredentials(GoogleCredentials.fromStream(new FileInputStream(f)))
                            .build()
                            .getService();


                    Thread thread = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {

                            try {
                                Blob blob = storage.get(BlobId.of(BLOB_PIC_ID_PROJECT, imageName));
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

                    holder.image.setImageBitmap(decodedByte);
                }
            }

            return convertView;

        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }
    }

    private void openpage(){
        Intent intent = new Intent(mContext.getApplicationContext(), SeeFullEventPage.class);
        intent.putExtras(params);
        mContext.getApplicationContext().startActivity(intent);
    }
}
