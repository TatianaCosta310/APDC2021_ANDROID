package pt.unl.fct.campus.firstwebapp.data.Events;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.data.Result;
import pt.unl.fct.campus.firstwebapp.data.model.EventData;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.ExecuteService;
import pt.unl.fct.campus.firstwebapp.data.model.UserService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventDataSource {

    private UserService service;

    public EventDataSource() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apdc-project-310922.ew.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(UserService.class);


    }


    public Result<EventData2> createEvent(String token,Map<String,RequestBody> map) {

        Call<EventData2> userAuthenticatedCall = service.createEvent(token,map);
        try {

            Response<EventData2> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceCreateEvent(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error Creating Event", e));
        }

    }

    public Result<List<JsonObject>> seeEvents(String value, String token) {

        Call<List<JsonObject> > userAuthenticatedCall = service.seeEvents(value );
        try {

            Response<List<JsonObject>> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceEvents(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To see Events", e));
        }
    }


    public Result<List<JsonObject>> seeFinishedEvents(String value, String token) {


        Call<List<JsonObject>> userAuthenticatedCall = service.seeFinishedEvents(value);
        try {

            Response<List<JsonObject>> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceEvents(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To see Events", e));
        }
    }

    public Result<Void> participate(String token, long eventId) {

        Call<Void> userAuthenticatedCall = service.participate(token,eventId);
        try {

            Response<Void> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceParticipate(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To see Events", e));
        }
    }


    public Result<Void> removeParticipation(String token, long eventId) {

        Call<Void> userAuthenticatedCall = service.removeParticipation(token,eventId);
        try {

            Response<Void> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceParticipate(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To see Events", e));
        }
    }

    public Result<List<JsonObject>> seemyEvents(String value, String token) {

        Call<List<JsonObject>> userAuthenticatedCall = service.seeMyEvents(value);
        try {

            Response<List<JsonObject>> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceEvents(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To see Events", e));
        }
    }

    public Result<Void> doRemoveEvent(String eventId, String token) {

        Call<Void> userAuthenticatedCall = service.doRemoveEvent(eventId,token);
        try {

            Response<Void> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceRemoveEvent(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To Remove Event", e));
        }
    }


}
