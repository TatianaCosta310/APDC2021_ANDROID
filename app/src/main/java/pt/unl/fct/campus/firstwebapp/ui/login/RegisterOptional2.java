package pt.unl.fct.campus.firstwebapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.user.Main_Page;

public class RegisterOptional2 extends AppCompatActivity {

    CheckBox checkBoxUser, checkBoxCompany;
    private LoginViewModel loginViewModel;

    String userType;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register3);


        Intent oldIntent = getIntent();
        Bundle bundleExtra = oldIntent.getExtras();

        String username = bundleExtra.getString("username");
        String password = bundleExtra.getString("password");
        String email = bundleExtra.getString("email");
        String mainAdress = bundleExtra.getString("mainAdress");
        String optionalAdress = bundleExtra.getString("optionalAdress");
        String fixNumber = bundleExtra.getString("fixNumber");
        String mobileNumber = bundleExtra.getString("mobileNumber");

        checkBoxUser = findViewById(R.id.check1);
        checkBoxCompany = findViewById(R.id.check2);

        final Button finishButton = findViewById(R.id.finish);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);


        finishButton.setEnabled(true);
        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                loginViewModel.registrate(username,password,email,mainAdress,optionalAdress,fixNumber,mobileNumber,userType);

              //loginViewModel.login(usernameEditText.getText().toString(),
                //        passwordEditText.getText().toString());

                openPage();

            }
        });

    }

    public void controlCheckBoxes(View v){

        boolean checked = ((CheckBox)v).isChecked();

        switch (v.getId()){

            case R.id.check1:

                userType = "Volunteer";

                if(checked){
                    checkBoxCompany.setChecked(false);
                }
                break;


            case R.id.check2:

                userType = "Company";

                if(checked){
                    checkBoxUser.setChecked(false);
                }
                break;

        }

    }
    public void  openPage() {

        Intent intent = new Intent(this,Main_Page.class);
        startActivity(intent);
    }
}
