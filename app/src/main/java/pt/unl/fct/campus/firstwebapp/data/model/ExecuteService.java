package pt.unl.fct.campus.firstwebapp.data.model;

import android.app.Application;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import pt.unl.fct.campus.firstwebapp.data.Result;
import retrofit2.Call;
import retrofit2.Response;

public class ExecuteService extends Application {

    public ExecuteService() {

    }


    public Result<LoggedInUser> ExecuteService(Response<LoginData> response){



            if (response.isSuccessful()) {
                LoginData ua = response.body();
                return new Result.Success<>(new LoggedInUser(ua.getUsername(), ua.getUsername()));
            }

            return new Result.Error(new Exception(response.errorBody().toString()));

    }

    public Result<RegisterData> ExecuteServiceRegister(Response<RegisterData> response) {

        if (response.isSuccessful()) {
            RegisterData ua = response.body();
            return new Result.Success<>(new LoggedInUser(ua.getUsername(), ua.getUsername()));
        }

        return new Result.Error(new Exception(response.errorBody().toString()));
    }
}
