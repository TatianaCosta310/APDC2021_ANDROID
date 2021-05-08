package pt.unl.fct.campus.firstwebapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pt.unl.fct.campus.firstwebapp.R;

public class RegisterOptional  extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register2);

        final Button nextOptionsButton = findViewById(R.id.next2);

        nextOptionsButton.setEnabled(true);
        nextOptionsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                openNextOptionalPage();

            }
        });
    }

    public void  openNextOptionalPage(){
        Intent intent = new Intent(this, RegisterOptional2.class);
        startActivity(intent);
    }
}