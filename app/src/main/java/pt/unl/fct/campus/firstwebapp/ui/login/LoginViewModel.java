package pt.unl.fct.campus.firstwebapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;
import android.widget.Toast;

import java.util.concurrent.Executor;

import pt.unl.fct.campus.firstwebapp.data.LoginRepository;
import pt.unl.fct.campus.firstwebapp.data.Result;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;
import pt.unl.fct.campus.firstwebapp.data.model.LoggedInUser;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.LoginData;
import pt.unl.fct.campus.firstwebapp.data.model.RegisterData;
import pt.unl.fct.campus.firstwebapp.data.model.UserAuthenticated;

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
                Result<LoginData> result = loginRepository.login(username, password);

                if (result instanceof Result.Success) {
                    LoginData data = ((Result.Success<LoginData>) result).getData();
                    loginResult.postValue(new LoginResult(new LoggedInUserView(data.getUsername(),data.getToken())));
                } else {
                    loginResult.postValue(new LoginResult(R.string.login_failed));
                }

            }
        });
    }


    public void logout() {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<Void> result = loginRepository.logout();

                if (result instanceof Result.Success) {
                    loginResult.postValue(new LoginResult(new LoggedInUserView("Logout","")));
                }else {
                    loginResult.postValue(new LoginResult(R.string.logout_failed));
                }
            }
        });
    }


    public void registrate(String name, String password, String email) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisterData> result = loginRepository.register(name, password,email);

                if (result instanceof Result.Success) {
                    loginResult.postValue(new LoginResult(new LoggedInUserView(name,"")));
                } else {
                    loginResult.postValue(new LoginResult(R.string.register_failed));
                }

            }
        });
    }


    public void updateInfo(String userType, String fixNumber, String mobileNumber, String address, String cAddress,String locality) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<AdditionalAttributes> result = loginRepository.updateInfo(userType, fixNumber,mobileNumber,address,cAddress,locality);

                if (result instanceof Result.Success) {
                    loginResult.postValue(new LoginResult(new LoggedInUserView(userType,"")));
                } else {
                    loginResult.postValue(new LoginResult(R.string.update_failed));
                }

            }
        });
    }


    public void removeAccount(String token,String password) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<LoginData> result = loginRepository.removeAccount(token,password);

                if (result instanceof Result.Success) {
                    loginResult.postValue(new LoginResult(new LoggedInUserView("","")));
                } else {
                    loginResult.postValue(new LoginResult(R.string.remove_failed));
                }

            }
        });
    }

    public void loginDataChanged(String username, String password, String confirmPassword,String name) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null, null,null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password, null,null));
        } else if (confirmPassword != null && !isConfirmPasswordValid(password, confirmPassword)) {
            loginFormState.setValue(new LoginFormState(null, null, R.string.must_match_password,null));
        }else if(name!= null && !isValidName(name)){
            new LoginFormState(null, null, null,R.string.invalid_name);
        }else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isValidName(String name) {
        if (name == null)
            return false;

        return true;
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {

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