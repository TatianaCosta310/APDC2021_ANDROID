package pt.unl.fct.campus.firstwebapp.data.Events;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import pt.unl.fct.campus.firstwebapp.data.model.EventData;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;

public class EventCreatedView {

    private List<JsonObject> list;
    private  String response;
    //... other data fields that may be accessible to the Events

    EventCreatedView(List<JsonObject> list) {
        this.list = list;
    }



    EventCreatedView(String response) {
      this.response = response;
    }

    List<JsonObject>  getEventsList() {
        return list;
    }
    String getResponse(){return response;}
}

