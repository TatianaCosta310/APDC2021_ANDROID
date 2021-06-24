package pt.unl.fct.campus.firstwebapp.data.Events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.data.Result;

import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.EventData;


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

    LiveData<EventResult> getLoginResult() {
        return eventResult;
    }



    public void createEvent(String token, RequestBody event) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<Void> result = eventRepository.createEvent( token,event);

                if (result instanceof Result.Success) {

                    List<JsonObject> a = new ArrayList<>();
                    eventResult.postValue(new EventResult(new EventCreatedView(a)));
                } else {
                    eventResult.postValue(new EventResult(R.string.Failed_Create_Event));
                }

            }
        });
    }


    public void seeEvent(String value,String token,Boolean actual) {

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


    public void CreateDataChanged(String name,Date startDay, Date finalDay,String startHour,String finalHour,String origin, String place) {
        if (!isNameValid(name)) {
            eventFormState.setValue(new EventFormState(R.string.invalid_name, null, null,null,null,null,null));
        } else if (!isStartDayValid(startDay)) {
            eventFormState.setValue(new EventFormState(null, R.string.invalid_day, null,null,null,null,null));
        } else if (!isFinishDayValid(startDay,finalDay)) {
            eventFormState.setValue(new EventFormState(null, null, R.string.invalid_day,null,null,null,null));
        } else if (!isHourValid(startHour)) {
        eventFormState.setValue(new EventFormState(null, null, null,R.string.inavlid_hour,null,null,null));
    } else if (!isHourValid(finalHour)) {
        eventFormState.setValue(new EventFormState(null, null,null,null,R.string.inavlid_hour,null,null));
    } else if (!isNameValid(origin)) {
        eventFormState.setValue(new EventFormState(null, null,null,null,null,R.string.invalid_name,null));
    } else if (!isNameValid(place)) {
        eventFormState.setValue(new EventFormState(null, null,null,null,null,null,R.string.invalid_name));
        }else {
        eventFormState.setValue(new EventFormState(true));
        }
    }

    private boolean isNameValid(String name) {
        if (name == null)
            return false;

        return true;
    }

    private boolean isStartDayValid(Date day) {

        Calendar cal =  Calendar.getInstance();

       if(day.before( cal.getTime()))
            return false;

            return true ;
    }

    private boolean isFinishDayValid(Date startDay, Date finalDay) {

        if(finalDay.before( startDay))
            return false;

        return true ;
    }

    private boolean isHourValid(String hour) {

        if(hour == null)
            return false;

        return true ;
    }



}