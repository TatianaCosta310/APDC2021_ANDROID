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



public class SuperUserToolsPage extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    Button usersButton, reportedEventsButton;
    Bundle params ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.superusertools_page);

        params = new Bundle();
        Intent oldIntent = getIntent();

        usersButton = findViewById(R.id.usersbutton);
        reportedEventsButton = findViewById(R.id.eventsbutton);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);


        usersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage(Users.class);

            }
        });

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