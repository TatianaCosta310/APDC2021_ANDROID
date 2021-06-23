package pt.unl.fct.campus.firstwebapp.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.StoragePics;

public class Profile extends AppCompatActivity implements StoragePics {

    Uri selectedImage;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // como meter o nome do utilizador no perfil??

        final Button updatePorfile = findViewById(R.id.editProfileTxt);
        final ImageButton changeprofilePic=findViewById(R.id.buttoncamera);

        image = findViewById(R.id.person);

        Intent oldIntent = getIntent();
        Bundle bundleExtra = oldIntent.getExtras();

       String a = bundleExtra.getByteArray("pic").toString();
      //  image =  (ImageView) a ;  ;

        updatePorfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        changeprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload(v);
            }

        });
}


    private void updateUser() {
        Intent intent = new Intent(this, UpdateUser.class);
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
