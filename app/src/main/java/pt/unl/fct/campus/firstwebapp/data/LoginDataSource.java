package pt.unl.fct.campus.firstwebapp.data;

import android.app.Application;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.data.model.AddCookiesInterceptor;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;
import pt.unl.fct.campus.firstwebapp.data.model.ChangeEmailArgs;
import pt.unl.fct.campus.firstwebapp.data.model.ChangePasswordArgs;
import pt.unl.fct.campus.firstwebapp.data.model.ExecuteService;
import pt.unl.fct.campus.firstwebapp.data.model.ProfileResponse;
import pt.unl.fct.campus.firstwebapp.data.model.ReceivedCookiesInterceptor;
import pt.unl.fct.campus.firstwebapp.data.model.RegisterData;
import pt.unl.fct.campus.firstwebapp.data.model.LoginData;
import pt.unl.fct.campus.firstwebapp.data.model.UserService;
import pt.unl.fct.campus.firstwebapp.ui.login.Profile;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

public class LoginDataSource extends Application implements Constantes {

    private UserService service;

    OkHttpClient okHttpClient;

    public LoginDataSource() {

         okHttpClient = new OkHttpClient.Builder ()
                .addInterceptor ((Interceptor) new AddCookiesInterceptor())
                .addInterceptor ((Interceptor) new ReceivedCookiesInterceptor())
                .connectTimeout (30, TimeUnit.SECONDS) .build ();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client (okHttpClient)
                .build();


        this.service = retrofit.create(UserService.class);
    }

    public Result<LoginData> login(String username, String password) {

        LoginData a = new LoginData(username,password);


        Call<LoginData> userAuthenticatedCall = service.authenticateUser(a);
        try {

            Response<LoginData> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();


            if (!response.isSuccessful()){
                int errorCode=response.code();

                if(errorCode==401)
                    return new Result.Error(new Exception("401"));
            }

            return executeService.ExecuteService(response);

        } catch (Exception e) {

           return new Result.Error(new IOException("Error logging in", e));
        }

    }

    public Result<Void> logout( ) {


        Call<Void> logoutUser = service.logoutUser();


        try {

            Response<Void> response = logoutUser.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceLogout(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging out", e));
        }

    }


    public Result<RegisterData> register(RegisterData data, String verification_code) {


        Call<Void> registrate = service.register(data,verification_code);


        try {

            Response<Void> response = registrate.execute();

            ExecuteService executeService = new ExecuteService();

            if(!response.isSuccessful()){
                int code=response.code();

                if (code==409)
                    return new Result.Error(new Exception("409"));
            }


            return executeService.ExecuteServiceRegister(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error Making Register", e));
        }
    }


    public Result<RegisterData> sendVerificationCode(String email,String email2) {

        Call<Void> verification_code = service.verification_code(email,email2);


        try {

            Response<Void> response = verification_code.execute();

            ExecuteService executeService = new ExecuteService();

            if(!response.isSuccessful()){
                int code=response.code();

                if (code==409)
                    return new Result.Error(new Exception("409")); //email already exists
            }


            return executeService.ExecuteServiceRegister(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error Sending verification code", e));
        }
    }



    public Result<RegisterData> sendVerificationCodeEmail(String token, ChangeEmailArgs changeEmailArgs) {

        Call<Void> verification_code = service.verification_code_email(token,changeEmailArgs);


        try {

            Response<Void> response = verification_code.execute();

            ExecuteService executeService = new ExecuteService();

            if(!response.isSuccessful()){
                int code=response.code();

                if (code==409)
                    return new Result.Error(new Exception("409")); //email already exists

                if (code==403)
                    return new Result.Error(new Exception("403"));

            }


            return executeService.ExecuteServiceRegister(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error Sending verification code", e));
        }
    }


    public Result<AdditionalAttributes> updateInfo(String cookie,AdditionalAttributes atribs) {

        Call<Void> updateInfos = service.updateInfos(cookie,atribs);


        try {

            Response<Void> response = updateInfos.execute();


            ExecuteService executeService = new ExecuteService();

            int errorCode=response.code();

            if(errorCode==401)
                return new Result.Error(new Exception("401"));


            return executeService.ExecuteServiceUpdateInfo(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error Making Update", e));
        }
    }


    public Result<String> updateProfilePicture(String token, Map<String, RequestBody> map) {

        Call<String> userAuthenticatedCall = service.updateProfilePicture(token,map);
        try {

            Response<String> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceupdateProfilePicture(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error Creating Event", e));
        }
    }

    public Result<ProfileResponse> getInfos(String token, String userid) {

        Call<ProfileResponse> updateInfos = service.getInfos(token,userid);


        try {

            Response<ProfileResponse> response = updateInfos.execute();

            ExecuteService executeService = new ExecuteService();

            int errorCode=response.code();
            if(errorCode==404)
                return new Result.Error(new Exception("404"));

            return executeService.ExecuteServiceGetInfo(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error Making Update", e));
        }
    }


    public Result<String> removeAccount(String token,String password) {

        Call<Void> removeAccount = service.removeAccount(token,password);

        try {

            Response<Void> response = removeAccount.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceLRemoveAccount(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error Removing Account", e));
        }
    }


    public Result<Void> changePassword(String verificationCode, ChangePasswordArgs data) {
        Call<Void> changePassword = service.changePassword(verificationCode,data);

        try {

            Response<Void> response = changePassword.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceRemoveEvent(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error Removing Account", e));
        }
    }


    public Result<Void> changeEmail(String token, String verificationCode, ChangeEmailArgs changeEmailArgs) {

        Call<Void> changeEmail = service.changeEmail(token,verificationCode,changeEmailArgs);

        try {

            Response<Void> response = changeEmail.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceRemoveEvent(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error", e));
        }
    }

    public Result<Void> changeName(String token, String username) {
        Call<Void> changeName = service.changeName(token,username);

        try {

            Response<Void> response = changeName.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceRemoveEvent(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error", e));
        }
    }
}