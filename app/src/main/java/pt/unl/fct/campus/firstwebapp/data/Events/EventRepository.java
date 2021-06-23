package pt.unl.fct.campus.firstwebapp.data.Events;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import pt.unl.fct.campus.firstwebapp.data.Result;
import pt.unl.fct.campus.firstwebapp.data.model.EventData;
import pt.unl.fct.campus.firstwebapp.data.model.LoginData;

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



    public Result<Void> createEvent(String token,String name, String description, String goal,String location,
                                    String  meetingPlace, String startDate, String endDate,
                                    String startTime, String  endTime, String volunteers){//, MultipartBody.Part images){

        Long v1 = new Long(30);

        Long v = v1.valueOf(volunteers);

        Result<Void> result = dataSource.createEvent(token,name, description,goal,  location,
                                                     meetingPlace,  startDate,  endDate,
                                                     startTime, endTime,v);//, images);

        return result;
    }

    public Result<List<JsonObject>> seeEvents(String value, String token,Boolean actual) {


        Result<List<JsonObject> > result;

        if(actual == true){
            result = dataSource.seeEvents(value, token);
        }else {
            result = dataSource.seeFinishedEvents(value, token);
        }

        return result;
    }
}

