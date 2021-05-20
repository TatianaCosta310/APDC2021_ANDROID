package pt.unl.fct.campus.firstwebapp.data;

import android.app.Application;

import pt.unl.fct.campus.firstwebapp.data.model.ExecuteService;
import pt.unl.fct.campus.firstwebapp.data.model.LoggedInUser;
import pt.unl.fct.campus.firstwebapp.data.model.RegisterData;
import pt.unl.fct.campus.firstwebapp.data.model.LoginData;
import pt.unl.fct.campus.firstwebapp.data.model.UserService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

public class LoginDataSource extends Application {

    private UserService service;

    public LoginDataSource() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://silver-device-307912.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(UserService.class);
    }

    public Result<LoggedInUser> login(String username, String password) {

        Call<LoginData> userAuthenticatedCall = service.authenticateUser(new LoginData(username, password));

        try {

            Response<LoginData> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteService(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }

    }

    public Result<LoggedInUser> logout(String username, String password) {


        Call<LoginData> logoutUser = service.authenticateUser(new LoginData(username, password));


        try {

            Response<LoginData> response = logoutUser.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteService(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }

    }


    public Result<RegisterData> register(String username, String password, String email, String address, String cAddress,
                                         String fixNumber, String mobileNumber, String userType) {

        Call<RegisterData> registrate = service.register(new RegisterData(username, password, email, address, cAddress, fixNumber, mobileNumber, userType));


        try {

            Response<RegisterData> response = registrate.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceRegister(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error Making Register", e));
        }


    }
}