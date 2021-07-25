package pt.unl.fct.campus.firstwebapp.ui.login;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;



public class Users extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    String token;

    Button usersButton, reportedEventsButton;
    Bundle params ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_su_page);

        params = new Bundle();
        Intent oldIntent = getIntent();

        Bundle bundleExtra = oldIntent.getExtras();
        token = bundleExtra.getString("token");


        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);




    }

    public void  nextPage(Class c){

        Intent intent = new Intent(this , c);

        if(params != null){
            params.remove("Next_Null");
            intent.putExtras(params);
        } else {
            params = new Bundle();
        }

        startActivity(intent);
    }

}