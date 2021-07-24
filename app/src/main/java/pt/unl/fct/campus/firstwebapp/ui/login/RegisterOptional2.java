package pt.unl.fct.campus.firstwebapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;

public class RegisterOptional2 extends AppCompatActivity {

    CheckBox checkBoxUser, checkBoxCompany;
    private LoginViewModel loginViewModel;

    Bundle bundleExtra;

    String userType = "Volunteer";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register3);


        Intent oldIntent = getIntent();
        bundleExtra = oldIntent.getExtras();



        String about = bundleExtra.getString("about");
        String quote = bundleExtra.getString("quote");
        String facebook = bundleExtra.getString("facebook");
        String instagram = bundleExtra.getString("instagram");
        String twitter = bundleExtra.getString("twitter");

        String cookie = bundleExtra.getString("token");


        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);

        checkBoxUser = findViewById(R.id.check1);
        checkBoxCompany = findViewById(R.id.check2);

        final Button finishButton = findViewById(R.id.finish);

        finishButton.setEnabled(true);
        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){


                AdditionalAttributes atribs = new AdditionalAttributes();

                atribs.setBio(about);
                atribs.setQuote(quote);
                atribs.setFacebook(facebook);
                atribs.setInstagram(instagram);
                atribs.setTwitter(twitter);


                /*atribs.setAddress(address);
                atribs.setMore_address(c_address);
                atribs.setCellphone(mobileNumber);
                atribs.setTelephone(fixNumber);
                atribs.setLocality(locality);
                atribs.setPerfil(userType);
*/
                loginViewModel.updateInfo(cookie,atribs);



            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {

            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }

                if (loginResult.getError() != null) {
                    showRegisterFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    setResult(Activity.RESULT_OK);
                }


                //Complete and destroy register activity once successful
                finish();
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

        Intent intent = new Intent(this, Main_Page.class);


        Intent oldIntent = getIntent();

        if(oldIntent != null) {
            bundleExtra = oldIntent.getExtras();
            intent.putExtras(bundleExtra);
        }

        startActivity(intent);
    }

    private void updateUiWithUser(LoggedInUserView model) {

        openPage();
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}