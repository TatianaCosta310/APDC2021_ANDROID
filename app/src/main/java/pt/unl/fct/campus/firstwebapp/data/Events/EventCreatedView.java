package pt.unl.fct.campus.firstwebapp.data.Events;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;

public class EventCreatedView {

    private List<JsonObject> list;
    private String[] listP;
    private  String response;
    private JsonObject jsonObject;
    private String token;
    //... other data fields that may be accessible to the Events

    public  EventCreatedView(){}

   public  EventCreatedView(List<JsonObject> list) {
        this.list = list;
    }

    EventCreatedView(String response) {
      this.response = response;
    }

    public EventCreatedView(JsonObject jsonObject){this.jsonObject =jsonObject; }

    public void setToken(String token){
        this.token = token;
    }

    public void setListP(String[]listP){
        this.listP = listP;
    }

    public String[]getListP(){
        return listP;
    }

    public String getToken(){
        return  token;
    }

    List<JsonObject>  getEventsList() {
        return list;
    }

    public JsonObject getJsonObject(){return  jsonObject;}

    String getResponse(){return response;}
}

