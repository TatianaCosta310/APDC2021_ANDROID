package pt.unl.fct.campus.firstwebapp.data.Events;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.gson.JsonObject;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.data.Result;

import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.CommentObject;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.UpcomingEventsArgs;


public class EventViewModel extends ViewModel {

    private MutableLiveData<EventFormState> eventFormState = new MutableLiveData<>();
    private MutableLiveData<EventResult> eventResult = new MutableLiveData<>();
    private EventRepository eventRepository;

    private Executor executor;

    EventViewModel(EventRepository eventRepository, Executor executor) {

        this.eventRepository = eventRepository;
        this.executor = executor;
    }

    LiveData<EventFormState> getEventFormState() {
        return eventFormState;
    }

    public LiveData<EventResult> getLoginResult() {
        return eventResult;
    }



    public void createEvent(String token, Map<String,RequestBody> map) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<EventData2> result = eventRepository.createEvent( token,map);

                if (result instanceof Result.Success) {

                    List<JsonObject> a = new ArrayList<>();
                    eventResult.postValue(new EventResult(new EventCreatedView(a)));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_Create_Event));
                }

            }
        });
    }

    public void getEvent(long eventId, String token) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<JsonObject> result = eventRepository.getEvent( eventId,token);

                if (result instanceof Result.Success) {

                    JsonObject a = ((Result.Success<JsonObject>) result).getData();

                    eventResult.postValue(new EventResult(new EventCreatedView(a)));
                } else {

                    String error = result.toString();

                    if (error.equals("Error[exception=java.lang.Exception: 401]"))
                        eventResult.postValue(new EventResult(R.string.session_invalid));

                   else eventResult.postValue(new EventResult(R.string.Failed_Create_Event));
                }

            }
        });
    }


    public void seeEvent(String value, String token, String actual, UpcomingEventsArgs upcomingEventsArgs) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Result<EventCreatedView> result = null;
                try {
                    result = eventRepository.seeEvents(value,token,actual,upcomingEventsArgs);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                if (result instanceof Result.Success) {
                   // ArrayList<JsonObject> j = ((Result.Success<ArrayList<JsonObject>>) result).getData();
                    // loginResult.postValue(new LoginResult(new LoggedInUserView(data.getUsername(),data.getToken())));
                    eventResult.postValue(new EventResult(((Result.Success<EventCreatedView>) result).getData()));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_See_Event));
                }

            }
        });
    }

    public void getMyEvents(String token, String token1, String b) {

       /* executor.execute(new Runnable() {
            @Override
            public void run() {

                Result<List<JsonObject>> result = eventRepository.seeEvents(token,token1,b);

                if (result instanceof Result.Success) {
                    List<JsonObject> list = ((Result.Success<List<JsonObject>>) result).getData();
                    eventResult.postValue(new EventResult(new EventCreatedView(list)));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_See_Event));
                }

            }
        });*/
    }


    public void getParticipatingEvents(String token, String token1, String participating) {

         /* executor.execute(new Runnable() {
            @Override
            public void run() {

              Result<List<JsonObject>> result = eventRepository.seeEvents(token,token1,participating);

                if (result instanceof Result.Success) {
                    List<JsonObject> list = ((Result.Success<List<JsonObject>>) result).getData();
                    eventResult.postValue(new EventResult(new EventCreatedView(list)));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_See_Event));
                }

            }
        });*/
    }

    public void participate(String token, long eventId) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Result<Void> result = eventRepository.participate(token,eventId);

                if (result instanceof Result.Success) {
                    String r = ((Result.Success<String>) result).getData();
                    // loginResult.postValue(new LoginResult(new LoggedInUserView(data.getUsername(),data.getToken())));
                    eventResult.postValue(new EventResult(new EventCreatedView(r)));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_See_Event));
                }

            }
        });
    }

    public void removeParticipation(String token, long eventId) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Result<Void> result = eventRepository.removeParticipation(token,eventId);

                if (result instanceof Result.Success) {
                    String r = "Done with succsess";
                    // loginResult.postValue(new LoginResult(new LoggedInUserView(data.getUsername(),data.getToken())));
                    eventResult.postValue(new EventResult(new EventCreatedView(r)));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_Remove_Participation));
                }

            }
        });
    }

    public void doRemoveEvent(String eventId, String token) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Result<Void> result = eventRepository.doRemoveEvent(eventId,token);

                if (result instanceof Result.Success) {
                    eventResult.postValue(new EventResult(new EventCreatedView("SUCCSEESS")));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_Remove_See_Event));
                }

            }
        });
    }

    public void postComment(String token, CommentObject commentText) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Result<JsonObject> result = eventRepository.postCommet(token,commentText);

                if (result instanceof Result.Success) {
                    eventResult.postValue(new EventResult(new EventCreatedView(((Result.Success<JsonObject>) result).getData())));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_To_Post_Comment));
                }

            }
        });
    }

    public void loadComments(String token, long eventId, String o) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Result<JsonObject> result = eventRepository.loadComments(token,eventId,o);

                if (result instanceof Result.Success) {
                    eventResult.postValue(new EventResult(new EventCreatedView(((Result.Success<JsonObject>) result).getData())));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_To_Load_Comment));
                }

            }
        });
    }

    public void deleteComment(String token, long commentId) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Result<JsonObject> result = null;// = eventRepository.deleteComment(token,commentId);

                if (result instanceof Result.Success) {
                    eventResult.postValue(new EventResult(new EventCreatedView(((Result.Success<JsonObject>) result).getData())));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_To_Load_Comment));
                }

            }
        });
    }
}