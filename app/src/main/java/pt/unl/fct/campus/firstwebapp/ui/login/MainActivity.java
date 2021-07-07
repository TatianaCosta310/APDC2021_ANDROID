package pt.unl.fct.campus.firstwebapp.ui.login;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    TextView aboutUsText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);



    loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
            .get(LoginViewModel.class);

    final EditText usernameEditText = findViewById(R.id.email);
    final EditText passwordEditText = findViewById(R.id.password);
    final Button loginButton = findViewById(R.id.login);
    final Button signUpButton = findViewById(R.id.register);
    final ProgressBar loadingProgressBar = findViewById(R.id.loading);


        aboutUsText = findViewById(R.id.textAbout1);
        aboutUsText.setPaintFlags(aboutUsText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        signUpButton.setEnabled(true);
        signUpButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            loadingProgressBar.setVisibility(View.VISIBLE);

            doSignup();

        }
    });

          loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
        @Override
        public void onChanged(@Nullable LoginFormState loginFormState) {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        }
    });

            loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {

        @Override
        public void onChanged(@Nullable LoginResult loginResult) {
            if (loginResult == null) {
                return;
            }


            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
                setResult(Activity.RESULT_OK);
            }


            //Complete and destroy login activity once successful
           // finish();
        }
    });

    TextWatcher afterTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // ignore
        }

        @Override
        public void afterTextChanged(Editable s) {
            loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString(), null,null);
        }
    };


            usernameEditText.addTextChangedListener(afterTextChangedListener);
            passwordEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                       passwordEditText.getText().toString());

            }
            return false;
        }
    });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Username: " + usernameEditText.getText().toString(),
                        Toast.LENGTH_LONG).show();

                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

            }
        });

}

    public void  openPage(Class c, LoggedInUserView model) {

        Intent intent = new Intent(this, c);

        if(model != null){


            Bundle params = new Bundle();
            params.putString("token", model.getToken());
            params.putString("profile_pic",model.getProfile_pic());

            intent.putExtras(params);
        }

        startActivity(intent);

    }


    private void doSignup(){
      openPage( Register.class, null);

    }
    private void updateUiWithUser(LoggedInUserView model) {

         openPage(Main_Page.class,model);
    }


    public void OpenAboutPage(View v) {
        openPage(AboutUs.class,null);
    }
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}