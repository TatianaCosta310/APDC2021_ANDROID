package pt.unl.fct.campus.firstwebapp.data.Events;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.data.Result;
import pt.unl.fct.campus.firstwebapp.data.model.CommentObject;
import pt.unl.fct.campus.firstwebapp.data.model.EventData;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.LoginData;
import pt.unl.fct.campus.firstwebapp.data.model.UpcomingEventsArgs;

public class EventRepository extends AppCompatActivity {

    private static volatile EventRepository instance = null;

    private EventDataSource dataSource;

    // private constructor : singleton access
    private EventRepository(EventDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static EventRepository getInstance(EventDataSource dataSource) {
        if (instance == null) {
            instance = new EventRepository(dataSource);
        }
        return instance;
    }



    public Result<Long> createEvent(String token ,Map<String,RequestBody> map){

        //Long v1 = new Long(30);

        //Long v = v1.valueOf(volunteers);

        Result<Long> result = dataSource.createEvent(token,map);

        return result;
    }

    public Result<EventCreatedView> seeEvents(String value, String token, String actual, UpcomingEventsArgs upcomingEventsArgs) throws MalformedURLException {


        Result<EventCreatedView> result = null;

        if(actual.equals("actual")){
            result = dataSource.seeEvents(value, token, upcomingEventsArgs);
        }else if(actual.equals("finished")){
            result = dataSource.seeFinishedEvents(value, token);
        }else if(actual.equals("mine")){
            result = dataSource.seemyEvents(value, token);
        }else if(actual.equals("participating")){
            result = dataSource.seeParticipatingEvents(value,"",token);
        }

        return result;
    }

    public Result<JsonObject> getEvent(long eventId, String token) {

        Result<JsonObject >  result = dataSource.getEvent(eventId,token);

        return result;
    }

    public Result<Void> participate(String token, long eventId) {

        Result<Void > result = dataSource.participate(token, eventId);


        return result;
    }

    public Result<Void> removeParticipation(String token, long eventId) {

        Result<Void > result = dataSource.removeParticipation(token, eventId);


        return result;
    }

    public Result<Void> doRemoveEvent(String eventId, String token) {

        Result<Void > result = dataSource.doRemoveEvent(eventId,token);

        return result;
    }


    public Result<JsonObject> postCommet(String token, CommentObject commentText) {

        Result<JsonObject > result = dataSource.postCommet(token,commentText);

        return result;
    }

    public Result<JsonObject> loadComments(String token, long eventId, String o) {
        Result<JsonObject > result = dataSource.loadComments(token,eventId,o);

        return result;
    }

    public Result<Void> deleteComment(String token, long commentId) {

        Result<Void > result = dataSource.deleteComment(token,commentId);

        return result;
    }
}

