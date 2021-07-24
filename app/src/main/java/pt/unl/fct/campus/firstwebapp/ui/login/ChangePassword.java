package pt.unl.fct.campus.firstwebapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;

public class ChangePassword  extends AppCompatActivity {


    private LoginViewModel loginViewModel;
    private Bundle params;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    EditText emailEditText;

    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);

        emailEditText = findViewById(R.id.email);

        passwordEditText = findViewById(R.id.password21);
        confirmPasswordEditText = findViewById(R.id.password23);

        final Button nextOptionsButton = findViewById(R.id.next);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading2);

        Intent intent = getIntent();

        if(intent != null) {
            params = intent.getExtras();

            if (params != null) {

                if (params.getString("email") != null)
                    email = params.getString("email");

            } else {
                emailEditText.setVisibility(View.VISIBLE);
                params = new Bundle();
            }
        }
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }

                    nextOptionsButton.setEnabled(loginFormState.isDataValid());



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
                    setResult(Activity.RESULT_OK);
                }


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
                if(emailEditText.getVisibility() == View.VISIBLE){
                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString(),"");
            }else{

                    loginViewModel.onlyPasswordChanged(passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
                }
            }
        };


        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener);


        nextOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailEditText.getVisibility() == View.VISIBLE) {

                    loginViewModel.sendVerificationCode(emailEditText.getText().toString(),"");
                }else {
                    emailEditText.setText(email);
                    loginViewModel.sendVerificationCode(email,"");
                }


            }
        });
    }


    public void  openNextOptionalPage( ){

        Intent intent = new Intent(this, Register2VerificationCode.class);

        params.putString("email", emailEditText.getText().toString());
        params.putString("password", passwordEditText.getText().toString());
        params.putString("change","password");

        intent.putExtras(params);

        startActivity(intent);


    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
