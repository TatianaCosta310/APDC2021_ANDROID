package pt.unl.fct.campus.firstwebapp.data.model;

import kotlin.ParameterName;
import okhttp3.internal.http.HttpHeaders;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

   @POST("rest/login/op2")
    Call<LoginData> authenticateUser(@Body LoginData data);

   @GET("rest/login/op7")
    Call<Void> logoutUser();

   @POST("rest/login/op1")
    Call<Void> register(@Body RegisterData data);

   // Nao esta completa pelo servidor
   @POST("rest/login/op3")
    Call<Void> updateInfos(@Body AdditionalAttributes atributs);



   @FormUrlEncoded
   @HTTP(method = "DELETE", path="rest/login/op8", hasBody = true)
    Call<Void> removeAccount(@Field("p") String password);

    //Nao completa pelo servidor
    @POST("rest/login/op11")
    Call<Void> changePassword();

    @POST("rest/events/create")
    Call<String[] > createEvent(@Body EventData event);




}
