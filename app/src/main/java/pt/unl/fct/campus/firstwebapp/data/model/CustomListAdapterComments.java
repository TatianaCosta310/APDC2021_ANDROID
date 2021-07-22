package pt.unl.fct.campus.firstwebapp.data.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Constantes;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CustomListAdapterComments extends ArrayAdapter<CommentObject2> implements Constantes {

    private static final String TAG = "CustomListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView userName,date,comment;
        ImageView image;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public CustomListAdapterComments(Context context, int resource, ArrayList<CommentObject2> objects) {
        super(context, resource, objects);


        mContext = context;
        mResource = resource;
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

                result = convertView;

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
                result = convertView;
            }



            Animation animation = AnimationUtils.loadAnimation(mContext,
                    (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
            result.startAnimation(animation);
            lastPosition = position;

            holder.userName.setText(user);
            holder.comment.setText(comment);
            holder.date.setText(date);

            //set the imageCover

            if(imgUrl != null) {

                File f = new File(mContext.getCacheDir(), user + ".png");
                try {
                    f.createNewFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                String[] split = imgUrl.split("/");


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
                            Blob blob = storage.get(BlobId.of(BLOB_PIC_ID_PROJECT, split[4]));
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

            return convertView;

        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }
    }

}
