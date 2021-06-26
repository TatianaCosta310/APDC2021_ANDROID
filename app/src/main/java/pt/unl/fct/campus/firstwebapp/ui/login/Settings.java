package pt.unl.fct.campus.firstwebapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginResult;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginViewModel;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginViewModelFactory;
import pt.unl.fct.campus.firstwebapp.ui.login.MainActivity;
import retrofit2.http.Field;

public class Settings extends AppCompatActivity {

    ListView listView;
    String [] options;
    TextView textView,textAlert;
    EditText passwordEditText;
    Button doRemove;
    String token;

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        passwordEditText = findViewById(R.id.editTextTextPassword);
        textView = findViewById(R.id.editTextTextPassword);
        textAlert = findViewById(R.id.textAlert);
        listView = findViewById(R.id.listview);
        options = getResources().getStringArray(R.array.options_List);
        doRemove = findViewById(R.id.doRemoveButton);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);

         ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
               android.R.id.text1,options);

        listView.setAdapter(adapter);


        Intent oldIntent = getIntent();
        Bundle params;
         token = null;

        if(oldIntent != null) {
            params = oldIntent.getExtras();
            token = params.getString("token");
        }

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.removeAccount(token,textView.toString());
                    openPage();

                }
                return false;
            }
        });

        doRemove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                loginViewModel.removeAccount(token,passwordEditText.getText().toString());




            }
        });


        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {

            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }

                if (loginResult.getError() != null) {
                    showRemoveFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    setResult(Activity.RESULT_OK);
                }


                //Complete and destroy login activity once successful
                finish();
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

           @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String val = adapter.getItem(position);

                if(position == 0) {
                   // loginViewModel.changePassword();
                    Toast.makeText(getApplicationContext(),val,Toast.LENGTH_SHORT).show();
                }

                if(position == 1){

                    // fica visivel
                    textAlert.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    doRemove.setVisibility(View.VISIBLE);
                    doRemove.setEnabled(true);




                }

               if(position == 2){
                    //missing help settings, maybe later
               }


           }
        });


    }

    public void  openPage() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateUiWithUser(LoggedInUserView model) {

        openPage();
    }

    private void showRemoveFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


}
