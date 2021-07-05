package pt.unl.fct.campus.firstwebapp.data.model;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.net.CookieStore;
import java.util.List;

import okhttp3.Cookie;
import pt.unl.fct.campus.firstwebapp.data.Result;
import retrofit2.Response;

public class ExecuteService extends Application {

    public ExecuteService() {

    }


    public Result<LoginData> ExecuteService(Response<LoginData> response){

            if (response.isSuccessful()) {
                LoginData ua = response.body();

                String token = response.headers().get("Set-Cookie");

                LoginData data = new LoginData(ua.getUsername(),ua.password);
                data.setToken(token);
                //data.setProfilePicUrl(ua.getProfilePicUrl());

                return new Result.Success<>(data);
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


    public Result<AdditionalAttributes> ExecuteServiceGetInfo(Response<AdditionalAttributes> response) {

        if (response.isSuccessful()) {

            AdditionalAttributes ua = response.body();

            return new Result.Success<>(ua);
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

    public Result<EventData2> ExecuteServiceCreateEvent(Response<EventData2> response){

        if (response.isSuccessful()) {
            return new Result.Success<>("SUCCESS, EVENT CREATED");
        }

        return new Result.Error(new Exception(response.errorBody().toString()));

    }

    public Result<List<JsonObject>> ExecuteServiceEvents(Response<List<JsonObject>> response) {

        if (response.isSuccessful()) {
            List<JsonObject> ua = response.body();
            int size = ua.size();

            return new Result.Success<>(ua);
        }
        return new Result.Error(new Exception(response.errorBody().toString()));

    }


    public Result<Void> ExecuteServiceParticipate(Response<Void> response) {
        if (response.isSuccessful()) {

            return new Result.Success<>("SUCCESS");
        }


        return new Result.Error(new Exception(response.errorBody().toString()));
    }

    public Result<Void> ExecuteServiceRemoveEvent(Response<Void> response) {

        if (response.isSuccessful()) {

            return new Result.Success<>("SUCCESS");
        }
        return new Result.Error(new Exception(response.errorBody().toString()));
    }
}
