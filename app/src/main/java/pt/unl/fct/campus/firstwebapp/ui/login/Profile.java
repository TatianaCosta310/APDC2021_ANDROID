package pt.unl.fct.campus.firstwebapp.ui.login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.CreateEventPage;
import pt.unl.fct.campus.firstwebapp.data.Events.MyEvents;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;
import pt.unl.fct.campus.firstwebapp.data.model.StoragePics;

public class Profile extends AppCompatActivity implements StoragePics {

    Uri selectedImage;
    ImageView image;
    Bitmap bitmap;
    String token;
    Button imagePicButton;

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        bitmap = null;

        final Button updatePorfile = findViewById(R.id.editProfileTxt);
        final Button getEventsLibrary = findViewById(R.id.button4);
        final ImageButton changeprofilePic=findViewById(R.id.buttoncamera);

        image = findViewById(R.id.imageProfilePic);
        imagePicButton = findViewById(R.id.button5);


        imagePicButton.setVisibility(View.INVISIBLE);

        Intent oldIntent = getIntent();
        Bundle bundleExtra = oldIntent.getExtras();

        token = bundleExtra.getString("token");

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);

        updatePorfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(bundleExtra);
            }
        });


        getEventsLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEventsLibary(bundleExtra);
            }
        });


        imagePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bitmap != null){

                    File f = new File(Profile.this.getCacheDir(), "image.png");
                    try {
                        f.createNewFile();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/ , bos);
                    byte[] bitmapdata = bos.toByteArray();


                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f);
                        fos.write(bitmapdata);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }


                    Map<String, RequestBody> map = new HashMap<>();

                    RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), f);

                    map.put("profilePicture",fbody);


                    loginViewModel.updateProfilePicture(token,map);

                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(Profile.this);

                    alert.setTitle("Profile Pic");
                    alert.setMessage("Must Choose Profile Picture");
                }


            }
        });


        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {

            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }

                if (loginResult.getError() != null) {
                    showMessage(loginResult.getError().toString());
                }
                if (loginResult.getSuccess() != null) {

                    LoggedInUserView model = loginResult.getSuccess();
                    showMessage("Profile Pic Updated");
                    // atribs =  (AdditionalAttributes) model.getObject();

                    //putInfos(atribs);
                }


            }
        });
}

    private void showMessage(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    private void getEventsLibary(Bundle bundleExtra) {

        Intent intent = new Intent(this, EventsUserLibrary.class);

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
                      //  Bitmap bitmap = null;

                        try{
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                            imagePicButton.setVisibility(View.VISIBLE);
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




