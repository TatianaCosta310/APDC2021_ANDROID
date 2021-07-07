package pt.unl.fct.campus.firstwebapp.data.Events;

import com.google.gson.JsonObject;

import java.util.List;

public class EventCreatedView {

    private List<JsonObject> list;
    private  String response;
    private JsonObject jsonObject;
    //... other data fields that may be accessible to the Events

    EventCreatedView(List<JsonObject> list) {
        this.list = list;
    }



    EventCreatedView(String response) {
      this.response = response;
    }

    EventCreatedView(JsonObject jsonObject){this.jsonObject =jsonObject; }

    List<JsonObject>  getEventsList() {
        return list;
    }

    JsonObject getJsonObject(){return  jsonObject;}

    String getResponse(){return response;}
}

