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


import org.w3c.dom.Text;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;

public class Settings extends AppCompatActivity {

    ListView listView;
    String [] options;
    TextView textAlert;
    EditText passwordEditText;
    Button doRemove;
    String token;
    Bundle params = new Bundle();
    Boolean change = false;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        passwordEditText = findViewById(R.id.editTextTextPassword);
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

         token = null;

        if(oldIntent != null) {
            params = oldIntent.getExtras();
            token = params.getString("token");
        }

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if(change == false) {
                        loginViewModel.removeAccount(token, passwordEditText.getText().toString());
                        openPage(MainActivity.class, null);
                    }
                }
                return false;
            }
        });

        doRemove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    if(change){
                       loginViewModel.changeName(token, passwordEditText.getText().toString());
                    }else {
                        loginViewModel.removeAccount(token, passwordEditText.getText().toString());
                    }

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

                    if(change){

                        params.putString("name",passwordEditText.getText().toString());
                        Toast.makeText(Settings.this,"Username Changed!",Toast.LENGTH_SHORT).show();
                        openPage(Main_Page.class,params);
                    }else {
                        updateUiWithUser(loginResult.getSuccess());
                        setResult(Activity.RESULT_OK);
                    }
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
                   // changePassword();
                    params.putString("email",params.getString("email"));
                    openPage(ChangePassword.class,params);
                }

               if(position == 1) {
                   // changeEmail();
                   openPage(ChangeEmail.class,params);
               }

               if(position == 2) {
                   // chanheUsername();



                   change = true;

                   textAlert.setVisibility(View.VISIBLE);
                   textAlert.setText("Insert new Name");

                   passwordEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                   passwordEditText.setVisibility(View.VISIBLE);
                   passwordEditText.setHint("insert new username");

                   doRemove.setText("Change");
                   doRemove.setVisibility(View.VISIBLE);
                   doRemove.setEnabled(true);


               }

                if(position == 3){
                    //removeAccount
                    // fica visivel

                    if(change) {
                        textAlert.setText("Insert Password to remove Account");
                        passwordEditText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordEditText.setHint("password");
                        doRemove.setText("Remove Account");
                    }

                    passwordEditText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                    change = false;

                    textAlert.setVisibility(View.VISIBLE);
                    passwordEditText.setVisibility(View.VISIBLE);
                    doRemove.setVisibility(View.VISIBLE);
                    doRemove.setEnabled(true);


                }

               if(position == 4){
                    //missing help settings, maybe later
               }


           }
        });


    }



    public void  openPage(Class c,Bundle params) {

        Intent intent = new Intent(this, c);

        if(params!= null)
            intent.putExtras(params);

        startActivity(intent);
    }

    private void updateUiWithUser(LoggedInUserView model) {

        openPage(MainActivity.class,null);
    }

    private void showRemoveFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


}
