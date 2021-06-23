package pt.unl.fct.campus.firstwebapp.data.Events;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

public class EventCreatedView {

    private List<JsonObject> list;
    //... other data fields that may be accessible to the Events

    EventCreatedView(List<JsonObject> list) {
        this.list = list;
    }

    List<JsonObject>  getEventsList() {
        return list;
    }
}

