package pt.unl.fct.campus.firstwebapp.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import pt.unl.fct.campus.firstwebapp.R;

public class Profile extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // como meter o nome do utilizador no perfil??

        final Button updatePorfile = findViewById(R.id.editProfileTxt);
        final ImageButton changeprofilePic=findViewById(R.id.buttoncamera);

        updatePorfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        changeprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //:TODO vai alterar a imagem de perfil do user
            }
        });

    }

    private void updateUser() {
        Intent intent = new Intent(this, UpdateUser.class);
        startActivity(intent);
    }

}
