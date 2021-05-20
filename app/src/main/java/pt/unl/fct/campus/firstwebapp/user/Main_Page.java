package pt.unl.fct.campus.firstwebapp.user;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import pt.unl.fct.campus.firstwebapp.R;

public class Main_Page extends AppCompatActivity {


    ImageButton openOptionsMenu;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main1);

        openOptionsMenu = findViewById(R.id.imageButton);

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
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void  openProfilePage(Class c){

        Intent intent = new Intent(this , c);

        startActivity(intent);
    }



}
