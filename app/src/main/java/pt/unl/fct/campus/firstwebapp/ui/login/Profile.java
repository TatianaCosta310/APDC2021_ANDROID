package pt.unl.fct.campus.firstwebapp.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.MyEvents;
import pt.unl.fct.campus.firstwebapp.data.model.StoragePics;

public class Profile extends AppCompatActivity implements StoragePics {

    Uri selectedImage;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        final Button updatePorfile = findViewById(R.id.editProfileTxt);
        final Button getMyEvents = findViewById(R.id.button4);
        final ImageButton changeprofilePic=findViewById(R.id.buttoncamera);

        image = findViewById(R.id.person);

        Intent oldIntent = getIntent();
        Bundle bundleExtra = oldIntent.getExtras();

        updatePorfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(bundleExtra);
            }
        });


        getMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyEvents(bundleExtra);
            }
        });

        changeprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload(v);
            }

        });
}

    private void getMyEvents(Bundle bundleExtra) {

        Intent intent = new Intent(this, MyEvents.class);

        Intent oldIntent = getIntent();

        if(oldIntent != null) {
            bundleExtra = oldIntent.getExtras();
            intent.putExtras(bundleExtra);
        }

        startActivity(intent);
    }


    private void updateUser(Bundle bundleExtra) {
        Intent intent = new Intent(this, UpdateUser.class);

        Intent oldIntent = getIntent();

        if(oldIntent != null) {
            bundleExtra = oldIntent.getExtras();
            intent.putExtras(bundleExtra);
        }

        startActivity(intent);
    }

    public void Upload(View v) {

        verifyPermission(Profile.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Open Galery"),PICK_IMAGE_REQUEST);

    }


    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST){

            if(resultCode == RESULT_OK){
                selectedImage = data.getData();

                String [] imageProj = {MediaStore.Images.Media.DATA};

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Cursor cursor = getContentResolver().query(selectedImage,imageProj,null,null);

                    if(cursor != null){
                        cursor.moveToFirst();
                        // int indexImage = cursor.getColumnIndex(imageProj[0]);
                        Bitmap bitmap = null;

                        try{
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                        image.setImageBitmap(bitmap);

                    }
                }

            }
        }

    }

    public void verifyPermission(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }


}
