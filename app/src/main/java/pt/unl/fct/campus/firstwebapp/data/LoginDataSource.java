package pt.unl.fct.campus.firstwebapp.data;

import android.app.Application;
import pt.unl.fct.campus.firstwebapp.data.model.LoggedInUser;
import pt.unl.fct.campus.firstwebapp.data.model.UserAuthenticated;
import pt.unl.fct.campus.firstwebapp.data.model.UserCredentials;
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

    public LoginDataSource(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://silver-device-307912.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(UserService.class);
    }

    public Result<LoggedInUser> login(String username, String password ) {

        Call<UserAuthenticated> userAuthenticatedCall = service.authenticateUser(new UserCredentials(username,password));

        try {

            Response<UserAuthenticated> response = userAuthenticatedCall.execute();

            if (response.isSuccessful()) {
                UserAuthenticated ua = response.body();
                return new Result.Success<>(new LoggedInUser(ua.getTokenID(), ua.getUsername()));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        }catch (IOException e){
            return new Result.Error(new IOException("Error Logging in",e));

        }
            /*LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }*/

    }

    public void logout() {

    }
}