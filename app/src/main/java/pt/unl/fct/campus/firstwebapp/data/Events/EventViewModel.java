package pt.unl.fct.campus.firstwebapp.data.Events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.data.Result;

import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.EventData;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;


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


    public void seeEvent(String value,String token,String actual) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Result<List<JsonObject>> result = eventRepository.seeEvents(value,token,actual);

                if (result instanceof Result.Success) {
                    List<JsonObject> list = ((Result.Success<List<JsonObject>>) result).getData();
                    // loginResult.postValue(new LoginResult(new LoggedInUserView(data.getUsername(),data.getToken())));
                    eventResult.postValue(new EventResult(new EventCreatedView(list)));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_See_Event));
                }

            }
        });
    }

    public void getMyEvents(String token, String token1, String b) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Result<List<JsonObject>> result = eventRepository.seeEvents(token,token1,b);

                if (result instanceof Result.Success) {
                    List<JsonObject> list = ((Result.Success<List<JsonObject>>) result).getData();
                    // loginResult.postValue(new LoginResult(new LoggedInUserView(data.getUsername(),data.getToken())));
                    eventResult.postValue(new EventResult(new EventCreatedView(list)));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_See_Event));
                }

            }
        });
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

    public void CreateDataChanged(String name,Date startDay, Date finalDay,String startHour,String finalHour,String numVolunteers,String goal, String description) {
        if (!isNameValid(name)) {
            eventFormState.setValue(new EventFormState(R.string.invalid_name, null, null,null,null,null,null,null));

        } else if (!isHourValid(startHour)) {
        eventFormState.setValue(new EventFormState(null, null, null,R.string.inavlid_hour,null,null,null,null));
    } else if (!isHourValid(finalHour)) {
            eventFormState.setValue(new EventFormState(null, null, null, null, R.string.inavlid_hour, null, null,null));
        }else if (!isNameValid(numVolunteers)) {
                eventFormState.setValue(new EventFormState(null, null, null,null,null,R.string.invalid_number,null,null));
        }else if (!isNameValid(goal)) {
            eventFormState.setValue(new EventFormState(null, null, null,null,null,null,R.string.required,null));
        }else if (!isNameValid(description)) {
            eventFormState.setValue(new EventFormState(null, null, null,null,null,null,null,R.string.required));
        } else if (!isStartDayValid(startDay)) {
            eventFormState.setValue(new EventFormState(null, R.string.invalid_day, null,null,null,null,null,null));
        } else if (!isFinishDayValid(startDay,finalDay)) {
            eventFormState.setValue(new EventFormState(null, null, R.string.invalid_day,null,null,null,null,null));
        }else{
        eventFormState.setValue(new EventFormState(true));
        }
    }

    private boolean isNameValid(String name) {
        if (name == null || name.isEmpty())
            return false;

        return true;
    }

    private boolean isStartDayValid(Date day) {

        Calendar cal =  Calendar.getInstance();

        if(day == null)
            return false;

       if(day.before( cal.getTime()))
            return false;

            return true ;
    }

    private boolean isFinishDayValid(Date startDay, Date finalDay) {

        if(finalDay == null )
            return false;

        if(finalDay.before( startDay))
            return false;

        return true ;
    }

    private boolean isHourValid(String hour) {

        String h = "Hour";

        if(hour == null || hour.isEmpty() || hour.contains(h) )
            return false;

        return true ;
    }



}