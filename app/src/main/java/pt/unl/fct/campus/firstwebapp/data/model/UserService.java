package pt.unl.fct.campus.firstwebapp.data.model;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

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
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {


    //SUPERUSER ENDPOINTS

    @GET("rest/super/role")
    Call<String> handleRole(@Header("Cookie") String value);

    //UESER ENDPOINTS


    @POST("rest/login/op2")
    Call<LoginData> authenticateUser(@Body LoginData data);

   @GET("rest/login/op7")
   Call<Void> logoutUser();

   @POST("rest/login/op1")
   Call<Void> register(@Body RegisterData data,@Header("vrfcck") String serverVcode);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("rest/login/vcd/{email}")
    Call<Void> verification_code(@Path("email") String email, @Query("n") String newUser);

    @POST("rest/login/chgmailvcd")
    Call<Void> verification_code_email(@Header("Cookie") String value, @Body ChangeEmailArgs args);


    @POST("rest/login/op3")
   Call<Void> updateInfos(@Header("Cookie") String value,@Body AdditionalAttributes atributs);

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("rest/login/savep")
    Call<String> updateProfilePicture(@Header("Cookie") String value,@PartMap Map<String,RequestBody> map);

   @GET("rest/login/infos/{userid}")
   Call<ProfileResponse> getInfos(@Header("Cookie") String value,@Path(value = "userid") String userid);


   @FormUrlEncoded
   @HTTP(method = "DELETE",path = "rest/login/op8", hasBody = true)
    Call<Void> removeAccount(@Header("Cookie") String value, @Field("p") String password);

    @POST("rest/login/chgpwd")
    Call<Void> changePassword(@Header("vrfcck") String serverVcode, @Body ChangePasswordArgs args);

    @PUT("rest/login/updatename/{name}")
    Call<Void> changeName(@Header("Cookie") String token,@Path("name") String newName);


    @PUT("rest/login/chgmail")
    Call<Void> changeEmail(@Header("Cookie") String value,@Header("vrfcck") String loginToken, @Body ChangeEmailArgs args);

    //EVENTS ENDPOINTS
    @Multipart
    @POST("rest/events/create")
    Call<Long> createEvent(@Header("Cookie") String value, @PartMap Map<String,RequestBody> map);

    // da 401 whyy???  supostamente nao autorizado?? hmmm
    @DELETE("rest/events/delete/{eventId}")
    Call<Void>  doRemoveEvent(@Path(value = "eventId") String eventId, @Header("Cookie") String token);


    //@Headers({"Content-Type:application/json",  "Accept-Charset: utf-8"})
    @POST("rest/events/view")
    Call<List<JsonObject>> seeEvents(@Header("crsck") String value, @Header("Cookie") String token,
                                          @Body UpcomingEventsArgs args,
                                          @Header("Accept-Charset") String enc, @Header("Content-Type") String charset);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("rest/events/view/finished")
    Call< List<JsonObject> >seeFinishedEvents(@Header("fnesck") String value, @Header("Cookie") String token);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("rest/events/view/myevents")
    Call< String[]> seeMyEvents(@Header("Cookie") String value, @Query("userid") String userid, @Query("cursor") String cursor);

    @FormUrlEncoded
    @HTTP(method = "POST",path = "rest/events/participate", hasBody = true)
    Call<Void> participate(@Header("Cookie") String token, @Field("eid") long eventid);

   @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @DELETE("rest/events/rparticipation/{eventid}")
    Call<Void> removeParticipation(@Header("Cookie") String token, @Path("eventid") long eventid);

   @Headers({"Content-Type: application/json","Accept: application/json"})
   @GET("rest/events/view/interested")
   Call< String[]> seeParticipatingEvents(@Query("userid") String user,
                                                  @Query("cursor") String cursor,
                                                  @Header("Cookie") String token);


    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("rest/events/event/{eventId}")
    Call<JsonObject> getEvent(@Path("eventId") long eventid, @Header("Cookie") String token);


    //Events Comments Endpoints

    @POST("rest/comments/create")
    Call<JsonObject> postComment(@Header("Cookie") String value, @Body CommentObject comment);

    @DELETE("rest/comments/remove/{commentId}")
    Call<Void> deleteComment(@Header("Cookie") String value, @Path("commentId") long commentId);

    @GET("rest/comments/load/{eventid}")
    Call<JsonObject> loadComments(@Header("Cookie") String value, @Path("eventid") long eventId,@Query("c") String cursor);

}
