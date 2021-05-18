package pt.unl.fct.campus.firstwebapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import java.util.concurrent.Executor;

import pt.unl.fct.campus.firstwebapp.data.LoginRepository;
import pt.unl.fct.campus.firstwebapp.data.Result;
import pt.unl.fct.campus.firstwebapp.data.model.LoggedInUser;
import pt.unl.fct.campus.firstwebapp.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    private Executor executor;

    LoginViewModel(LoginRepository loginRepository,Executor executor) {

        this.loginRepository = loginRepository;
        this.executor = executor;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password ) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<LoggedInUser> result = loginRepository.login(username, password);

                if (result instanceof Result.Success) {
                    LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                    loginResult.postValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                } else {
                    loginResult.postValue(new LoginResult(R.string.login_failed));
                }

            }
        });
    }

    public void loginDataChanged(String username, String password, String confirmPassword) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null,null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password,null));
        } else if (confirmPassword != null && !isConfirmPasswordValid(password, confirmPassword)){
            loginFormState.setValue(new LoginFormState(null,null, R.string.must_match_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {

            return password != null && password.trim().length() > 5;
    }

    private boolean isConfirmPasswordValid(String password, String confirmPassword) {

             return  password.matches(confirmPassword);
        }


}