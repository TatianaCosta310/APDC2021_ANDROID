package pt.unl.fct.campus.firstwebapp.ui.login;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.ui.login.Activity_login_page2;
import pt.unl.fct.campus.firstwebapp.ui.login.LoggedInUserView;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginFormState;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginResult;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginViewModel;
import pt.unl.fct.campus.firstwebapp.ui.login.LoginViewModelFactory;
import pt.unl.fct.campus.firstwebapp.ui.login.Register;

import android.app.Activity;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
            .get(LoginViewModel.class);

    final EditText usernameEditText = findViewById(R.id.username);
    final EditText passwordEditText = findViewById(R.id.password);
    final Button loginButton = findViewById(R.id.login);
    final Button signUpButton = findViewById(R.id.register);
    final ProgressBar loadingProgressBar = findViewById(R.id.loading);



        signUpButton.setEnabled(true);
        signUpButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            loadingProgressBar.setVisibility(View.VISIBLE);
            openRegisterPage();

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
            }
            setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful
            finish();
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
                    passwordEditText.getText().toString(), null);
        }
    };


            usernameEditText.addTextChangedListener(afterTextChangedListener);
            passwordEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),null);
            }
            return false;
        }
    });

            loginButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString(),null);


        }
    });
}

    public void  openRegisterPage(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }



    public void openActivity_login2(){

        Intent intent = new Intent(this, Activity_login_page2.class);
        startActivity(intent);
    }

    private void updateUiWithUser(LoggedInUserView model) {

        openActivity_login2();


        // String welcome = getString(R.string.welcome) + model.getDisplayName();
        //Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}