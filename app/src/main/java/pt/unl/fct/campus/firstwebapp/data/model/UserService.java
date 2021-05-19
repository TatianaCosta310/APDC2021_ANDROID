package pt.unl.fct.campus.firstwebapp.data.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("rest/log/v1")
    Call<LoginData> authenticateUser(@Body LoginData data);

    @POST("rest/logout/logout1")
    Call<LoginData> logoutUser(@Body LoginData data);

    @POST("rest/register/r1")
    Call<RegisterData> register(@Body RegisterData data);

    //@POST
   // Call<T> createEvent();

}
