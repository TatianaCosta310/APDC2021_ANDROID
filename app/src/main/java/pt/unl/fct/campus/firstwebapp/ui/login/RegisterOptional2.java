package pt.unl.fct.campus.firstwebapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import pt.unl.fct.campus.firstwebapp.R;

public class RegisterOptional2 extends AppCompatActivity {

    CheckBox checkBoxUser, checkBoxCompany;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register3);

        checkBoxUser = findViewById(R.id.check1);
        checkBoxCompany = findViewById(R.id.check2);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);

        final Button finishButton = findViewById(R.id.finish);

        finishButton.setEnabled(true);
        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                openPage();

            }
        });

    }

    public void controlCheckBoxes(View v){

        boolean checked = ((CheckBox)v).isChecked();

        switch (v.getId()){

            case R.id.check1:

                if(checked){
                    checkBoxCompany.setChecked(false);
                }
                break;


            case R.id.check2:

                if(checked){
                    checkBoxUser.setChecked(false);
                }
                break;

        }

    }
    public void  openPage() {
        Intent intent = new Intent(this, Main_Page.class);
        startActivity(intent);
    }
}
