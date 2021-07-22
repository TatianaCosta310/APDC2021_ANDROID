package pt.unl.fct.campus.firstwebapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.data.Events.EventCreatedView;
import pt.unl.fct.campus.firstwebapp.data.Events.EventResult;
import pt.unl.fct.campus.firstwebapp.data.LoginRepository;
import pt.unl.fct.campus.firstwebapp.data.Result;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
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
                    loginResult.postValue(new LoginResult(new LoggedInUserView(data.getEmail(),data.getToken(),data.getProfilePictureURL())));
                } else {

                    String error = result.toString();

                    if (error.equals("Error[exception=java.lang.Exception: 401]"))
                        loginResult.postValue(new LoginResult(R.string.prompt_wrong_Data));

                    else loginResult.postValue(new LoginResult(R.string.login_failed));
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
                    loginResult.postValue(new LoginResult(new LoggedInUserView("Logout","","")));
                }else {
                    loginResult.postValue(new LoginResult(R.string.logout_failed));
                }
            }
        });
    }


    public void registrate(RegisterData data, String verification_code) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisterData> result = loginRepository.register(data, verification_code);


                if (result instanceof Result.Success) {
                    loginResult.postValue(new LoginResult(new LoggedInUserView(data.getName(),"","")));
                } else {

                    String error = result.toString();

                    if (error.equals("Error[exception=java.lang.Exception: 409]"))
                        loginResult.postValue(new LoginResult(R.string.user_already_exist));

                    else loginResult.postValue(new LoginResult(R.string.register_failed));

                }

            }
        });
    }

    public void sendVerificationCode(String email) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisterData> result = loginRepository.sendVerificationCode(email);


                if (result instanceof Result.Success) {
                    LoggedInUserView loggedInUserView = new LoggedInUserView();

                    loggedInUserView.setvCode(((Result.Success<String>) result).getData());
                    loginResult.postValue(new LoginResult(loggedInUserView));
                } else {

                    String error = result.toString();

                    if (error.equals("Error[exception=java.lang.Exception: 409]"))
                        loginResult.postValue(new LoginResult(R.string.user_already_exist));

                    else loginResult.postValue(new LoginResult(R.string.register_failed));

                }

            }
        });
    }

    public void updateInfo( String cookie,AdditionalAttributes atribs) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<AdditionalAttributes> result = loginRepository.updateInfo(cookie,atribs);

                if (result instanceof Result.Success) {
                    loginResult.postValue(new LoginResult(new LoggedInUserView("success","","")));
                } else {
                    String error = result.toString();
                    if (error.equals("Error[exception=java.lang.Exception: 401]"))
                        loginResult.postValue(new LoginResult(R.string.session_expired));

                    else loginResult.postValue(new LoginResult(R.string.update_failed));
                }

            }
        });
    }

    public void updateProfilePicture(String token, Map<String, RequestBody> map) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<String> result = loginRepository.updateProfilePicture( token,map);

                if (result instanceof Result.Success) {

                    String s = ((Result.Success<String>) result).getData();

                    loginResult.postValue(new LoginResult(new LoggedInUserView("s","",s)));
                } else {
                    loginResult.postValue(new LoginResult(R.string.Failed_to_update_profile_pic));
                }

            }
        });
    }

    public void getInfos(String token,String userid) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<AdditionalAttributes> result = loginRepository.getInfos(token,userid);

                if (result instanceof Result.Success) {
                    AdditionalAttributes data = ((Result.Success<AdditionalAttributes>) result).getData();
                    loginResult.postValue(new LoginResult(new LoggedInUserView(data)));
                } else {
                    loginResult.postValue(new LoginResult(R.string.get_infos_failed));
                }

            }
        });
    }

    public void removeAccount(String token,String password) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<String> result = loginRepository.removeAccount(token,password);

                if (result instanceof Result.Success) {
                    loginResult.postValue(new LoginResult(new LoggedInUserView("","","")));
                } else {
                    loginResult.postValue(new LoginResult(R.string.remove_failed));
                }

            }
        });
    }

    public void loginDataChanged(String username, String password, String confirmPassword,String name) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null, null,null,null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password, null,null,null));
        } else if (confirmPassword != null && !isConfirmPasswordValid(password, confirmPassword)) {
            loginFormState.setValue(new LoginFormState(null, null, R.string.must_match_password,null,null));
        }else if(name!= null && !isValidName(name)){
            new LoginFormState(null, null, null,R.string.invalid_name,null);
        }else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void verificationCodeChanged(String verification_code) {
        if (!iscodeValid(verification_code)) {
            loginFormState.setValue(new LoginFormState(null, null, null, null,R.string.invalide_code));
        }else {
        loginFormState.setValue(new LoginFormState(true,"a"));
    }
    }

    private boolean iscodeValid(String verification_code) {

        if(verification_code == null ){
            return false;
        }else if(verification_code.isEmpty() || verification_code.length() > 7 || verification_code.length() < 7 || verification_code.charAt(0) != '0'){
            return false;
        }

        return true;
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

            return password != null && password.trim().length() > 6 && password.trim().length() < 12
                        && password.matches(".*[a-zA-Z]+.*");
                      //  && password.matches(".*[0-9]+.*"); //retirar maximo de length
    }

    private boolean isConfirmPasswordValid(String password, String confirmPassword) {

             return  password.matches(confirmPassword);
        }



}