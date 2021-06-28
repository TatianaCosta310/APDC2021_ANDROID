package pt.unl.fct.campus.firstwebapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.EventCreatedView;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginResult;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginViewModel;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginViewModelFactory;
import pt.unl.fct.campus.firstwebapp.ui.login.RegisterOptional2;

public class UpdateUser extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    String token;
    Bundle bundleExtra;
    AdditionalAttributes atribs;

    private EditText mainAdress;
    private EditText optionalAdress;
    private EditText fixNumber;
    private EditText mobileNumber;
    private EditText locality;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);

        mainAdress = findViewById(R.id.new_address);
        optionalAdress = findViewById(R.id.new_address2);
        fixNumber = findViewById(R.id.new_fixNumber);
        mobileNumber = findViewById(R.id.new_mobileNumber);
        locality = findViewById(R.id.local);

        final Button update=findViewById(R.id.update);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);


        Intent oldIntent = getIntent();

        token = null;

        if(oldIntent != null) {
            bundleExtra = oldIntent.getExtras();
            token = bundleExtra.getString("token");
        }

        loginViewModel.getInfos(token);

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {

            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }

                if (loginResult.getError() != null) {
                    showGetInfosFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {

                    LoggedInUserView model = loginResult.getSuccess();

                    atribs =  (AdditionalAttributes) model.getObject();

                    putInfos(atribs);
                }


            }
        });


        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                bundleExtra.putString("mainAdress",mainAdress.getText().toString());
                bundleExtra.putString("optionalAdress",optionalAdress.getText().toString());
                bundleExtra.putString("fixNumber",fixNumber.getText().toString());
                bundleExtra.putString("mobileNumber",mobileNumber.getText().toString());
                bundleExtra.putString("locality",locality.getText().toString());

                openNextPage(bundleExtra);

            }
        });

    }

    private void putInfos(AdditionalAttributes atribs) {

        if(atribs.getAddress() != null){
            mainAdress.setText(atribs.getAddress());
        }

        if(atribs.getMore_address() != null){
            optionalAdress.setText(atribs.getMore_address());
        }

        if(atribs.getLocality()!= null){
            locality.setText(atribs.getLocality());
        }

        if(atribs.getCellphone()!= null){
            mobileNumber.setText(atribs.getCellphone());
        }

        if(atribs.getTelephone() != null){
            fixNumber.setText(atribs.getTelephone());
        }
    }

    private void showGetInfosFailed(@StringRes Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }


    public void openNextPage(Bundle bundleExtra){

        Intent intent = new Intent(this, RegisterOptional2.class);

        intent.putExtras(bundleExtra);
        startActivity(intent);
    }
}