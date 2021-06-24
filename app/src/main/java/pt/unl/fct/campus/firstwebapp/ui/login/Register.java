package pt.unl.fct.campus.firstwebapp.ui.login;

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

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.Serializable;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;

public class Register  extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username2);
        final EditText passwordEditText = findViewById(R.id.password21);
        final EditText confirmPasswordEditText = findViewById(R.id.password23);
        final EditText emailEditText = findViewById(R.id.email);

        final Button nextOptionsButton = findViewById(R.id.next);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading2);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }

                nextOptionsButton.setEnabled(loginFormState.isDataValid());

                if (loginFormState.getname() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }

                if (loginFormState.getUsernameError() != null) {
                    emailEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }

                if (loginFormState.getConfirmPassError() != null) {
                    confirmPasswordEditText.setError(getString(loginFormState.getConfirmPassError()));
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
                    openNextOptionalPage();

                    /*updateUiWithUser(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            confirmPasswordEditText.getText().toString(),
                            emailEditText.getText().toString());*/


                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
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
                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString(),usernameEditText.getText().toString());
            }
        };


        usernameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener);

        confirmPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.registrate(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),emailEditText.getText().toString());

                }
                return false;
            }
        });

        nextOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginViewModel.registrate(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), emailEditText.getText().toString());

            }
        });
    }


    public void  openNextOptionalPage( ){

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);


    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}