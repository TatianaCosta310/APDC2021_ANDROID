package pt.unl.fct.campus.firstwebapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;

public class RegisterOptional  extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register2);


        //mudar pos parametros registo opcional

        final EditText mainAdress = findViewById(R.id.Address1);
        final EditText optionalAdress = findViewById(R.id.Address2);
        final EditText fixNumber = findViewById(R.id.FixNumber);
        final EditText mobileNumber = findViewById(R.id.MobileNumber);
        final EditText locality = findViewById(R.id.locality);

        final Button nextOptionsButton = findViewById(R.id.next2);

        nextOptionsButton.setEnabled(true);


        nextOptionsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                openNextOptionalPage(mainAdress.getText().toString(),optionalAdress.getText().toString(),
                        fixNumber.getText().toString(), mobileNumber.getText().toString(),locality.toString());

            }
        });

    }

    public void  openNextOptionalPage(String mainAdress, String optionalAdress, String fixNumber,
                                      String mobileNumber, String locality){

        Intent intent = new Intent(this,RegisterOptional2.class);
        // mandar os argumentos para a outra atividade

        Intent oldIntent = getIntent();
        Bundle params;

        if(oldIntent != null)
            params = oldIntent.getExtras();
        else params=new Bundle();


        params.putString("mainAdress", mainAdress);
        params.putString("optionalAdress", optionalAdress);
        params.putString("fixNumber", fixNumber);
        params.putString("mobileNumber", mobileNumber);
        params.putString("locality",locality);

        intent.putExtras(params);
        startActivity(intent);
    }
}