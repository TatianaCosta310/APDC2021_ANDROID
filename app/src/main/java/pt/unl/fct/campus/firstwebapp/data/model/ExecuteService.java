package pt.unl.fct.campus.firstwebapp.data.model;

import android.app.Application;
import android.widget.Toast;

import pt.unl.fct.campus.firstwebapp.data.Result;
import retrofit2.Response;

public class ExecuteService extends Application {

    public ExecuteService() {

    }


    public Result<LoginData> ExecuteService(Response<LoginData> response){

            if (response.isSuccessful()) {
                LoginData ua = response.body();
                return new Result.Success<>(new LoginData(ua.email, ua.password));
            }

            return new Result.Error(new Exception(response.errorBody().toString()));

    }

    public Result<RegisterData> ExecuteServiceRegister(Response<Void> response) {

        if (response.isSuccessful()) {
          //  RegisterData ua = response.body();
            return new Result.Success<>("Success");
        }
        return new Result.Error(new Exception(response.errorBody().toString()));
    }

    public Result<AdditionalAttributes> ExecuteServiceUpdateInfo(Response<Void> response) {

        if (response.isSuccessful()) {
            return new Result.Success<>("Success");
        }
        return new Result.Error(new Exception(response.errorBody().toString()));
    }

    public Result<Void> ExecuteServiceLogout(Response<Void> response) {

        if (response.isSuccessful()) {
            return new Result.Success<>("Success, Logging out");
        }
        return new Result.Error(new Exception(response.errorBody().toString()));
    }

    public Result<LoginData> ExecuteServiceLRemoveAccount(Response<Void> response) {

        if (response.isSuccessful()) {
            return new Result.Success<>("Success, Account Removed");
        }
        return new Result.Error(new Exception(response.errorBody().toString()));
    }
}
