package pt.unl.fct.campus.firstwebapp.data.Events;

import com.google.gson.JsonObject;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.data.Constantes;
import pt.unl.fct.campus.firstwebapp.data.Result;
import pt.unl.fct.campus.firstwebapp.data.model.CommentObject;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.ExecuteService;
import pt.unl.fct.campus.firstwebapp.data.model.UpcomingEventsArgs;
import pt.unl.fct.campus.firstwebapp.data.model.UserService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventDataSource implements Constantes {

    private UserService service;

    public EventDataSource() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(UserService.class);

    }


    public Result<EventData2> createEvent(String token,Map<String,RequestBody> map) {

        Call<EventData2> userAuthenticatedCall = service.createEvent(token,map);
        try {

            Response<EventData2> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            if (!response.isSuccessful()){
                int errorCode=response.code();

                if(errorCode==401)
                    return new Result.Error(new Exception("401"));
            }


            return executeService.ExecuteServiceCreateEvent(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error Creating Event", e));
        }

    }

    public Result<JsonObject> getEvent(long eventId, String token) {

        Call<JsonObject> userAuthenticatedCall = service.getEvent(eventId,token);
        try {

            Response<JsonObject> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceGetEvent(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error Creating Event", e));
        }
    }

    public Result<EventCreatedView> seeEvents(String value, String token, UpcomingEventsArgs upcomingEventsArgs)  {

        Call<List<JsonObject> > userAuthenticatedCall = service.seeEvents(null, token, upcomingEventsArgs ,ACCEPT_CHARSET,HEADER_CONTENT_TYPE_JSON);
        try {

            Response<List<JsonObject>> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceEvents(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To see Events", e));
        }
    }


    public Result<EventCreatedView> seeFinishedEvents(String value, String token) {

        Call<List<JsonObject>> userAuthenticatedCall = service.seeFinishedEvents(value,value);
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

    public Result<EventCreatedView> seemyEvents(String value, String token) {

        Call<String[]> userAuthenticatedCall = service.seeMyEvents(value,value,"");
        try {

            Response<String[]> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceMyEvents(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To see Events", e));
        }
    }

    public Result<EventCreatedView> seeParticipatingEvents(String userid,String cursor, String token) {

        Call<String[]> userAuthenticatedCall = service.seeParticipatingEvents(userid,cursor,token);
        try {

            Response<String[]> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceMyEvents(response);

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


    public Result<JsonObject> postCommet(String token, CommentObject commentText) {

        Call<JsonObject > userAuthenticatedCall = service.postComment(token,commentText);
        try {

            Response<JsonObject> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceGetEvent(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To see Events", e));
        }
    }

    public Result<JsonObject> loadComments(String token, long eventId, String o) {

        Call<JsonObject > userAuthenticatedCall = service.loadComments(token,eventId,o);
        try {

            Response<JsonObject> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceGetEvent(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To see Events", e));
        }
    }

    public Result<Void> deleteComment(String token, long commentId) {

        Call<Void > userAuthenticatedCall = service.deleteComment(token,commentId);
        try {

            Response<Void> response = userAuthenticatedCall.execute();

            ExecuteService executeService = new ExecuteService();

            return executeService.ExecuteServiceRemoveEvent(response);

        } catch (Exception e) {

            return new Result.Error(new IOException("Error To see Events", e));
        }
    }
}
