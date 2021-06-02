package pt.unl.fct.campus.firstwebapp.data.Events;

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
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

import pt.unl.fct.campus.firstwebapp.R;

public class CreateEventPage extends AppCompatActivity {

    Uri selectedImage;
    ImageView image;

    private static final String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE,
                                                          Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final int PICK_IMAGE_REQUEST = 9544;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        image =  findViewById(R.id.imageUp);

    }


    public void Upload(View v) {

        verifyPermission(CreateEventPage.this);
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

    private void verifyPermission(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

}
