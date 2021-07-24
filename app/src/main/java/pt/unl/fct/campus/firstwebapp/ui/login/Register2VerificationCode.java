package pt.unl.fct.campus.firstwebapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.ChangeEmailArgs;
import pt.unl.fct.campus.firstwebapp.data.model.ChangePasswordArgs;
import pt.unl.fct.campus.firstwebapp.data.model.RegisterData;

public class Register2VerificationCode  extends AppCompatActivity {


    private LoginViewModel loginViewModel;
    private String username;
    private String  password;
    private String  email;
    private String change;
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_2_verification_code);


        Intent oldIntent = getIntent();

        Bundle params = oldIntent.getExtras();

        username = params.getString("username");
        email = params.getString("email");
        password = params.getString("password");
        change = params.getString("change");
        token = params.getString("token");

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);

        final EditText verificationCode = findViewById(R.id.verificationCode);

        final Button nextOptionsButton = findViewById(R.id.finish1);


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }

                nextOptionsButton.setEnabled(loginFormState.isCodeValid());

                if (loginFormState.getVerification_code() != null) {
                    verificationCode.setError(getString(loginFormState.getVerification_code()));
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
                loginViewModel.verificationCodeChanged(verificationCode.getText().toString());
            }
        };

        verificationCode.addTextChangedListener(afterTextChangedListener);

        nextOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(change.equalsIgnoreCase("password")) {

                    ChangePasswordArgs data = new ChangePasswordArgs(email, password, verificationCode.getText().toString());

                    loginViewModel.changePassword(verificationCode.getText().toString(), data);


                }else if(change.equals("email")){

                    ChangeEmailArgs changeEmailArgs = new ChangeEmailArgs();

                    changeEmailArgs.setNewEmail(email);
                    changeEmailArgs.setPassword(verificationCode.getText().toString());


                    loginViewModel.changeEmail(token,verificationCode.getText().toString(), changeEmailArgs);

                }else {

                    RegisterData data = new RegisterData();

                    data.setName(username);
                    data.setEmail(email);
                    data.setPassword(password);
                    data.setVcode(verificationCode.getText().toString());

                    loginViewModel.registrate(data, verificationCode.getText().toString());

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
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    openNextOptionalPage();
                      setResult(Activity.RESULT_OK);

                }

            }
        });

    }


    public void  openNextOptionalPage( ){


        if(change.equalsIgnoreCase("password")) {
            Toast.makeText(getApplicationContext(), "Changed Password !", Toast.LENGTH_SHORT).show();

        }else if(change.equalsIgnoreCase("email")){
                Toast.makeText(getApplicationContext(), "Email Changed !", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getApplicationContext(), "Registration Complete!", Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);


    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
