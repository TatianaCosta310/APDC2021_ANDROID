package pt.unl.fct.campus.firstwebapp.data.model;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

import pt.unl.fct.campus.firstwebapp.R;


public class UploadImageFromPhone extends AppCompatActivity implements StoragePics {

    private Bitmap bitmap;
    private Uri selectedImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
    }

    public void Upload(View v) {

        verifyPermission(UploadImageFromPhone.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Open Galery"), PICK_IMAGE_REQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {

            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();

                String[] imageProj = {MediaStore.Images.Media.DATA};

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    Cursor cursor = getContentResolver().query(selectedImage, imageProj, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();
                        // int indexImage = cursor.getColumnIndex(imageProj[0]);
                        //bitmap = null;
                        //path = cursor.getString(indexImage);
                        cursor.close();

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


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

    public Bitmap getBitmap(){
        return bitmap;
    }
}
