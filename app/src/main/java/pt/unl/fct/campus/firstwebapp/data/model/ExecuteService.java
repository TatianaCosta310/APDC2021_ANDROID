package pt.unl.fct.campus.firstwebapp.data.model;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONObject;

import java.net.CookieStore;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import pt.unl.fct.campus.firstwebapp.data.Constantes;
import pt.unl.fct.campus.firstwebapp.data.Events.EventCreatedView;
import pt.unl.fct.campus.firstwebapp.data.Events.EventLocationResponse;
import pt.unl.fct.campus.firstwebapp.data.Result;
import retrofit2.Response;

public class ExecuteService extends Application implements Constantes {

    public ExecuteService() {

    }


    public Result<LoginData> ExecuteService(Response<LoginData> response){

            if (response.isSuccessful()) {
                LoginData ua = response.body();

                String token = response.headers().get("Set-Cookie");

                LoginData data = new LoginData(ua.getEmail(),ua.password);
                data.setName(ua.getName());
                data.setToken(token);
                data.setProfilePictureURL(ua.getProfilePictureURL());
                

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

    public Result<String> ExecuteServiceupdateProfilePicture(Response<String> response) {
        if (response.isSuccessful()) {

            String data = response.body();

            return new Result.Success<>(data);
        }
        return new Result.Error(new Exception(response.errorBody().toString()));
    }

    public Result<ProfileResponse> ExecuteServiceGetInfo(Response<ProfileResponse> response) {

        if (response.isSuccessful()) {

            ProfileResponse ua = response.body();

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

    public Result<String> ExecuteServiceLRemoveAccount(Response<Void> response) {

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

    public Result<EventCreatedView> ExecuteServiceEvents(Response<List<JsonObject>> response) {

        if (response.isSuccessful()) {
            List<JsonObject> ua = response.body();

            String token = response.headers().get("Set-Cookie");

            EventCreatedView a = new EventCreatedView(ua);
            a.setToken(token);

           // int size = ua.size();

            return new Result.Success<>(a);
        }
        return new Result.Error(new Exception(response.errorBody().toString()));

    }

    public Result<EventCreatedView> ExecuteServiceMyEvents(Response<String[]> response) {

        if (response.isSuccessful()) {
            String[] ua = response.body();

            String token = response.headers().get("Set-Cookie");

            EventCreatedView a = new EventCreatedView();
            a.setListP(ua);
            a.setToken(token);

            // int size = ua.size();

            return new Result.Success<>(a);
        }
        return new Result.Error(new Exception(response.errorBody().toString()));

    }


    public Result<JsonObject> ExecuteServiceGetEvent(Response<JsonObject> response) {

        if (response.isSuccessful()) {
            JsonObject ua = response.body();


           // int size = ua.size();

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

    public Result<String> ExecuteSHandleRole(Response<String> response) {
        if (response.isSuccessful()) {

            String data = response.body();

            return new Result.Success<>(data);
        }
        return new Result.Error(new Exception(response.errorBody().toString()));
    }

}
