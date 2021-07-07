package pt.unl.fct.campus.firstwebapp.ui.login;


import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import pt.unl.fct.campus.firstwebapp.GoogleMaps.MapsActivity;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.CreateEventPage;
import pt.unl.fct.campus.firstwebapp.data.Events.SeeEventsPage;
import pt.unl.fct.campus.firstwebapp.data.Events.SeeFinishedEventsPage;

public class Main_Page extends AppCompatActivity {


    ImageButton openOptionsMenu;
    ImageView image;
    Button createEventButton,seeEventButton,seeFinishedEventsButton;
    Bundle params ;
    String profilePic;

    private LoginViewModel loginViewModel;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main1);

        params = new Bundle();

        Intent oldIntent = getIntent();

        if(oldIntent != null)
            params = oldIntent.getExtras();


        if(params != null)
            profilePic = params.getString("profile_pic");

        openOptionsMenu = findViewById(R.id.imageButton);
        createEventButton = findViewById(R.id.button);
        seeEventButton = findViewById(R.id.button2);
        seeFinishedEventsButton = findViewById(R.id.button3);
        image = findViewById(R.id.person);


        if(profilePic != null){


            String[] split = profilePic.split("/");
            String imageName = split[4];

            File f = new File(Main_Page.this.getCacheDir(), imageName + ".png");
            try {
                f.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


            Storage storage = StorageOptions.newBuilder()
                    .setProjectId("daniel1624401699897")
                    // .setCredentials(GoogleCredentials.fromStream(new FileInputStream(f)))
                    .build()
                    .getService();

            Thread thread = new Thread(new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {

                    try {
                        Blob blob = storage.get(BlobId.of("daniel1624401699897", imageName));
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

            image.setImageBitmap(decodedByte);
        }

    loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);

       openOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(Main_Page.this,v);
                popupMenu.getMenuInflater().inflate(R.menu.main_popmenu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.profile:
                                openProfilePage(Profile.class);
                              // startActivity(new Intent(Main_Page.this,Profile.class));
                                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/")));
                                return true;
                            case R.id.settings:
                               openProfilePage(Settings.class);
                                return true;
                            case R.id.logout:
                                //fazer logout do user
                                doLogout();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });



        createEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openProfilePage(MapsActivity.class);
        }
        });


        seeEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openProfilePage(SeeEventsPage.class);
            }
        });

        seeFinishedEventsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openProfilePage(SeeFinishedEventsPage.class);
            }
        });
    }

    public void  openProfilePage(Class c){

        Intent intent = new Intent(this , c);

        if(params != null){
            intent.putExtras(params);
    } else {
        params = new Bundle();
    }

        startActivity(intent);
    }

    public void  doLogout(){

        loginViewModel.logout();
        openProfilePage(MainActivity.class);


    }



}
