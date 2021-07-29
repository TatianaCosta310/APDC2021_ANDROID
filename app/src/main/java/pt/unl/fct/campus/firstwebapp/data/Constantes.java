package pt.unl.fct.campus.firstwebapp.data;

import com.google.gson.Gson;

import java.util.HashSet;

public interface Constantes {

    String BASE_URL = "https://apdc-project-310922.ew.r.appspot.com/";
    String BLOB_PIC_ID_PROJECT = "profile_pics46335560256500";
    String BLOB_ID_PROJECT = "daniel1624401699897";

    String BLOB_ID_EVENT_PHOTOS = "events_photoes_bucket";

    String HEADER_CONTENT_TYPE_JSON =  "application/json";
    String ACCEPT_CHARSET =  "utf-8";


    Gson gson = new Gson();
    HashSet<String> cookies   = new HashSet<>();

    int [] goalRange = new int [5];

}
