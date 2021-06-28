package pt.unl.fct.campus.firstwebapp.data.model;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import kotlin.ParameterName;
import okhttp3.Cookie;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
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
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @POST("rest/login/op2")
    Call<LoginData> authenticateUser(@Body LoginData data);

   @GET("rest/login/op7")
   Call<Void> logoutUser();

   @POST("rest/login/op1")
   Call<Void> register(@Body RegisterData data);

   @POST("rest/login/op3")
   Call<Void> updateInfos(@Header("Cookie") String value,@Body AdditionalAttributes atributs);

   @GET("rest/login/infos")
   Call<AdditionalAttributes> getInfos(@Header("Cookie") String value);

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
    Call<EventData2> createEvent(@Header("Cookie") String value,@PartMap  Map<String, RequestBody> map);

    //DA ERRRO 403
    @FormUrlEncoded
    @POST("rest/events/participate")
   // @HTTP(method = "POST",path = "rest/events/participate", hasBody = true)
    Call<Void> participate(@Header("token") String token, @Field("eid") long eventid);


    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("rest/events/view/myevents")
    Call< List<JsonObject>> seeMyEvents(@Header("Cookie") String value);

}
