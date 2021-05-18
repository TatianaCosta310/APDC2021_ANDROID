package pt.unl.fct.campus.firstwebapp.data.model;

import android.net.wifi.hotspot2.pps.Credential;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("rest/log/v1")
    Call<UserAuthenticated> authenticateUser(@Body UserCredentials user);

}
