package pt.unl.fct.campus.firstwebapp.user;


import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.CreateEventPage;
import pt.unl.fct.campus.firstwebapp.data.Events.SeeEventsPage;
import pt.unl.fct.campus.firstwebapp.data.Events.SeeFinishedEventsPage;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginViewModel;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginViewModelFactory;
import pt.unl.fct.campus.firstwebapp.ui.login.MainActivity;
import pt.unl.fct.campus.firstwebapp.ui.login.Settings;

public class Main_Page extends AppCompatActivity {


    ImageButton openOptionsMenu;
    ImageView image;
    Button createEventButton,seeEventButton,seeFinishedEventsButton;

    private LoginViewModel loginViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main1);


        openOptionsMenu = findViewById(R.id.imageButton);
        createEventButton = findViewById(R.id.button);
        seeEventButton = findViewById(R.id.button2);
        seeFinishedEventsButton = findViewById(R.id.button3);
        image = findViewById(R.id.person);

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
                openProfilePage(CreateEventPage.class);
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

        Bundle params = new Bundle();

        Intent intent = new Intent(this , c);


        Intent oldIntent = getIntent();

        if(oldIntent != null)
            params = oldIntent.getExtras();
        else params=new Bundle();


        //params.putByteArray("pic",image.toString().getBytes());

        intent.putExtras(params);

        startActivity(intent);
    }

    public void  doLogout(){

        loginViewModel.logout();
        openProfilePage(MainActivity.class);


    }



}
