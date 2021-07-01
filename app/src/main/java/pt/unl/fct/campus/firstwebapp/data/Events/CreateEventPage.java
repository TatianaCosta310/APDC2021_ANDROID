package pt.unl.fct.campus.firstwebapp.data.Events;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.GoogleMaps.MapsActivity;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.EventData;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.StoragePics;
import pt.unl.fct.campus.firstwebapp.ui.login.Main_Page;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.Part;


public class CreateEventPage extends AppCompatActivity implements StoragePics {

    Uri selectedImage;
    ImageView image ;
    Button createEventButton;
    Bitmap bitmap;
    Bundle params;


    TextView eventDateCreation, eventDue, eventStartHour,eventFinalHour;
    EditText eventName,numVolunteers,description, goal;

    private EventViewModel eventViewModel;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStart,mDateSetListenerEnd;


    private String token,location;
    
    private int timeHour,timeMinute;

    private  Calendar cal;

    MapsActivity map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        eventViewModel = new ViewModelProvider(this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(EventViewModel.class);

        map = new MapsActivity();

        image =  findViewById(R.id.imageUp);
        createEventButton = findViewById(R.id.ButtonFinish);

        eventName = findViewById(R.id.eventName);
        goal = findViewById(R.id.eventGoals);
        numVolunteers = findViewById(R.id.eventNumberVolunteers);
        description = findViewById(R.id.eventDescription);

        eventDateCreation = findViewById(R.id.eventDateCreation);
        eventDue = findViewById(R.id.eventDue);

        eventStartHour = findViewById(R.id.eventStartHourText);
        eventFinalHour = findViewById(R.id.eventFinalHourText);

        Intent oldIntent = getIntent();
         params = oldIntent.getExtras();

        token = params.getString("token");
        location = params.getString("location");

        //eventDateCreation.setPaintFlags(eventDateCreation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //eventDue.setPaintFlags(eventDue.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

       /* eventViewModel.getEventFormState().observe(this, new Observer<EventFormState>() {
            @Override
            public void onChanged(@Nullable EventFormState createFormState) {

                if (createFormState == null) {
                    Toast.makeText(CreateEventPage.this,"BRAAAHHHH ",Toast.LENGTH_LONG).show();

                    return ;
                }
                createEventButton.setEnabled(createFormState.isDataValid());
                if (createFormState.getName()!= null) {
                    eventName.setError(getString(createFormState.getName()));
                }

                if (createFormState.getStartDate() != null) {
                    eventDateCreation.setError(getString(createFormState.getStartDate()));
                }

                if (createFormState.getFinalDate()!= null) {
                    eventDue.setError(getString(createFormState.getFinalDate()));
                }

                if (createFormState.getStartHour()!= null) {
                    eventDue.setError(getString(createFormState.getStartHour()));
                }

                if (createFormState.getFinalHour()!= null) {
                    eventDue.setError(getString(createFormState.getFinalHour()));
                }

                if (createFormState.getGoals()!= null) {
                    eventDue.setError(getString(createFormState.getGoals()));
                }

                if (createFormState.getDescription()!= null) {
                    eventDue.setError(getString(createFormState.getDescription()));
                }
            }
        });
*/
        eventViewModel.getLoginResult().observe(this, new Observer<EventResult>() {

            @Override
            public void onChanged(@Nullable EventResult eventResult) {
                if (eventResult == null) {
                    return;
                }

                if (eventResult.getError() != null) {
                    showCreateFailed(eventResult.getError());
                }
                if (eventResult.getSuccess() != null) {

                   openIntent();

                    setResult(Activity.RESULT_OK);
                }


                finish();
            }
        });


     /*   TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

                Date dateStart = null,endDate= null;
                try {
                    dateStart = new SimpleDateFormat("yyyy-MM-dd").parse(eventDateCreation.getText().toString());
                    endDate = new SimpleDateFormat("yyyy-MM-dd").parse( eventDue.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                eventViewModel.CreateDataChanged(eventName.getText().toString(),
                        dateStart,endDate,eventStartHour.getText().toString(),
                        eventFinalHour.getText().toString(),numVolunteers.getText().toString(),goal.getText().toString(),description.getText().toString());
            }
        };



        eventName.addTextChangedListener(afterTextChangedListener);
        eventDateCreation.addTextChangedListener(afterTextChangedListener);
        eventDue.addTextChangedListener(afterTextChangedListener);
        eventStartHour.addTextChangedListener(afterTextChangedListener);
        eventFinalHour.addTextChangedListener(afterTextChangedListener);
        numVolunteers.addTextChangedListener(afterTextChangedListener);
        goal.addTextChangedListener(afterTextChangedListener);
        description.addTextChangedListener(afterTextChangedListener);
*/


        eventDateCreation.setOnClickListener(v -> {

           doCalendarSelection(mDateSetListenerStart);

            mDateSetListenerStart = (view, year, month, day) -> {
                month = month +1;


                String date = year + "-" + month + "-" + day;
                eventDateCreation.setText(date);
            };
        });

        eventDue.setOnClickListener(v -> {

            doCalendarSelection(mDateSetListenerEnd);

            mDateSetListenerEnd = (view, year, month, day) -> {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                eventDue.setText(date);
            };
        });



        eventStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventPage.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                timeHour = hourOfDay;
                                timeMinute = minute;

                                cal = Calendar.getInstance();
                                cal.set(0,0,0,timeHour,timeMinute);


                                eventStartHour.setText(DateFormat.format("hh:mm",cal));
                            }
                        },12,0,false
                        );
                timePickerDialog.updateTime(timeHour,timeMinute);
                timePickerDialog.show();
            }
        });

        eventFinalHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventPage.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                timeHour = hourOfDay;
                                timeMinute = minute;

                                cal = Calendar.getInstance();
                                cal.set(0,0,0,timeHour,timeMinute);


                                eventFinalHour.setText(DateFormat.format("hh:mm",cal));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(timeHour,timeMinute);
                timePickerDialog.show();
            }
        });



        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isDataValid()) {

                    Gson gson = new Gson();

                    EventData e = new EventData();

                    // Create the Event object
                    Long v1 = new Long(30);
                    Long vol = v1.valueOf(numVolunteers.getText().toString());


                    e.setName(eventName.getText().toString());
                    e.setDescription(description.getText().toString());
                    e.setGoals(goal.getText().toString());
                    e.setVolunteers(vol);
                    e.setStartTime(eventStartHour.getText().toString());
                    e.setEndTime(eventFinalHour.getText().toString());
                    e.setStartDate(eventDateCreation.getText().toString());
                    e.setEndDate(eventDue.getText().toString());
                    e.setLocation(location);


                    //Send to Server
                /*let formData = new FormData();
                formData.append("img_cover",fil);
                formData.append("evd",evd);
                */

                    String o = gson.toJson(e);

                    RequestBody re = RequestBody.create(MediaType.parse("multipart/form-data"), o);

                    //MultipartBody.Part re = MultipartBody.Part.createFormData("evd", "evd", RequestBody.create(MediaType.parse("multipart/form-data"), o));

                     Map<String, RequestBody> map = new HashMap<>();

                      map.put("evd",re);

                    //Image

                /*int size = bitmap.getRowBytes() * bitmap.getHeight();
                ByteBuffer b = ByteBuffer.allocate(size);
                bitmap.copyPixelsToBuffer(b);
                byte[] bytes = new byte[size]; */


                    File f = new File(CreateEventPage.this.getCacheDir(), "image.png");
                    try {
                        f.createNewFile();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    //write the bytes in file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f);
                        fos.write(bitmapdata);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }


                    //tryingggg

                    //encode image to base64 string
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.id.imageUp);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    //

                   // RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), imageString);

                    RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), f);
                    map.put("img_cover",fbody);

                   // MultipartBody.Part file = MultipartBody.Part.createFormData("img_cover", "img_cover", RequestBody.create(MediaType.parse("multipart/form-data"), f));

                    eventViewModel.createEvent(token,map);

                } else {
                    Toast.makeText(CreateEventPage.this, "Invalid must fill everything!", Toast.LENGTH_SHORT).show();
                }
            }
        });

}


    public boolean isDataValid(){
        if(eventName.getText() == null || description.getText() == null || goal.getText() == null){
            return false;
        }
        return true;
    }
    public void doCalendarSelection(DatePickerDialog.OnDateSetListener mDateSetListener){

        Calendar cal  = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                CreateEventPage.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();


    }

    public void Upload(View v) {

        verifyPermission(CreateEventPage.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Open Galery"),PICK_IMAGE_REQUEST);

    }


    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST){

            if(resultCode == RESULT_OK){
                 selectedImage = data.getData();

                 String [] imageProj = {MediaStore.Images.Media.DATA};

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    Cursor cursor = getContentResolver().query(selectedImage,imageProj,null,null,null);

                    if(cursor != null){
                        cursor.moveToFirst();
                        int indexImage = cursor.getColumnIndex(imageProj[0]);
                         bitmap = null;
                         //path = cursor.getString(indexImage);
                        cursor.close();

                        try{
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);


                        }catch(IOException e){
                            e.printStackTrace();
                        }

                        image.setImageBitmap(bitmap);

                    }
                }

            }
        }

    }

    public void verifyPermission(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }


    public void  openIntent(){

        Intent intent = new Intent(this , Main_Page.class);

        Intent oldIntent = getIntent();

        if(oldIntent != null) {
            params = oldIntent.getExtras();
            intent.putExtras(params);
        }

        startActivity(intent);

    }

    private void showCreateFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

   public static class FileUtil {
        /*
         * Gets the file path of the given Uri.
         */
        @SuppressLint("NewApi")
        public static String getPath(Uri uri, Context context) {
            final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
            String selection = null;
            String[] selectionArgs = null;
            // Uri is different in versions after KITKAT (Android 4.4), we need to
            // deal with different Uris.
            if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    uri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    switch (type) {
                        case "image":
                            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case "video":
                            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case "audio":
                            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                            break;
                    }
                    selection = "_id=?";
                    selectionArgs = new String[]{
                            split[1]
                    };
                }
            }
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = {
                        MediaStore.Images.Media.DATA
                };
                try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        return cursor.getString(columnIndex);
                    }
                } catch (Exception e) {
                    Log.e("on getPath", "Exception", e);
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */

        private static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        private static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        private static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }
    }
}
