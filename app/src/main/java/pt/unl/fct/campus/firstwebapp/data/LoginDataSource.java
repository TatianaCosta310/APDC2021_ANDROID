package pt.unl.fct.campus.firstwebapp.data;

import android.app.Application;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;
import pt.unl.fct.campus.firstwebapp.data.model.ExecuteService;
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

       /* OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("parameter", "value").build();
                return chain.proceed(request);
            }
        });
*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apdc-project-310922.ew.r.appspot.com/")
                //.baseUrl("https://silver-device-307912.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
               // .client(httpClient.build())
                .build();

        this.service = retrofit.create(UserService.class);
    }

    public Result<LoginData> login(String username, String password) {

        Call<LoginData> userAuthenticatedCall = service.authenticateUser(new LoginData(username, password));
        try {

            Response<LoginData> response = userAuthenticatedCall.execute();


           /* if(response.isSuccessful()){
                LoginData ua = response.body();
                return new Result.Success<>(new LoggedInUser(ua.getUsername(), ua.getPassword()));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        }catch (IOException e){
            return new Result.Error(new IOException("Error logging in", e));
        }*/
            ExecuteService executeService = new ExecuteService();

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
            return new Result.Error(new IOException("Error logging in", e));
        }

    }


    public Result<RegisterData> register(String name, String password, String email) {

        Call<Void> registrate = service.register(new RegisterData(name, password, email));


        try {

            Response<Void> response = registrate.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceRegister(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error Making Register", e));
        }
    }

    public Result<AdditionalAttributes> updateInfo(String perfil,String telephone, String cellphone, String address, String more_address, String locality) {

        Call<Void> updateInfos = service.updateInfos(new AdditionalAttributes(perfil, telephone, cellphone,address,more_address,locality));


        try {

            Response<Void> response = updateInfos.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceUpdateInfo(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error Making Update", e));
        }
    }

    public Result<LoginData> removeAccount(String password) {

        Call<Void> removeAccount = service.removeAccount(password);


        try {

            Response<Void> response = removeAccount.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceLRemoveAccount(response);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error Removing Account", e));
        }
    }
}