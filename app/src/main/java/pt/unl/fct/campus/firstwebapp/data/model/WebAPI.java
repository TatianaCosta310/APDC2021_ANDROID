package pt.unl.fct.campus.firstwebapp.data.model;

import android.app.Application;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import pt.unl.fct.campus.firstwebapp.data.LoginDataSource;

public class WebAPI implements API {

    public  static final String BASE_URL = "http://localhost:8080/rest/";

    private RequestQueue mRequestQueue;
    private final Application mApplication;
    private int getResponseSuccess;

    public WebAPI(Application application){

        mApplication = application;
        mRequestQueue = Volley.newRequestQueue(application);
    }

    public void login(String username, String password){

        String url = BASE_URL + "log/v1";

        JSONObject jsonObject = new JSONObject();



        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);



        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(mApplication," Successful response",Toast.LENGTH_LONG).show();
                getResponseSuccess = 1;
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication," Error response",Toast.LENGTH_LONG).show();
                getResponseSuccess = -1;

            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject ,successListener,errorListener);

        mRequestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            Toast.makeText(mApplication," Exception",Toast.LENGTH_LONG).show();
            getResponseSuccess = -1;
        }

    }

    public int getResponse(){
        return getResponseSuccess;
    }

    public void logout(){
        String url = BASE_URL + "/login/op7";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(mApplication,"Successfull response",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mApplication,"Error response",Toast.LENGTH_LONG).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        mRequestQueue.add(jsonObjectRequest);

    }

}
