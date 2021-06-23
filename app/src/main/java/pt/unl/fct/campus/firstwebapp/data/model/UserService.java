package pt.unl.fct.campus.firstwebapp.data.model;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import kotlin.ParameterName;
import okhttp3.Cookie;
import okhttp3.MultipartBody;
import okhttp3.internal.http.HttpHeaders;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

   @POST("rest/login/op2")
    Call<LoginData> authenticateUser(@Body LoginData data);

   @GET("rest/login/op7")
    Call<Void> logoutUser();

   @POST("rest/login/op1")
    Call<Void> register(@Body RegisterData data);

   // Esta feito pelo server como?? ver isso e aplicar!
   @POST("rest/login/op3")
    Call<Void> updateInfos(@Body AdditionalAttributes atributs);

   @FormUrlEncoded
   @HTTP(method = "DELETE",path = "rest/login/op8", hasBody = true)
    Call<Void> removeAccount(@Header("Cookie") String value, @Field("p") String password);

    //Nao completa pelo servidor
    @POST("rest/login/op11")
    Call<Void> changePassword();

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("rest/events/view")
    Call<List<JsonObject>> seeEvents(@Header("Cookie") String value);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("rest/events/view/finished")
    Call< List<JsonObject>> seeFinishedEvents(@Header("Cookie") String value);

    @Multipart
    @POST("rest/events/create")
    Call<Void> createEvent(@Header("Cookie") String value,
                           @Part("name") String name,
                           @Part("description") String description,
                           @Part("name") String goals,
                           @Part("location") String location,
                           @Part("meetingPlace") String meetingPlace,
                           @Part("startDate") String startDate,
                           @Part("endDate") String endDate,
                           @Part("startTime") String startTime,
                           @Part("endTime") String endTime,
                           @Part("volunteers") long volunteers);

    // @Part MultipartBody.Part image);




}
