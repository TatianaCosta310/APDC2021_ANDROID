package pt.unl.fct.campus.firstwebapp.data.Events;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;

import android.text.method.Touch;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;

import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.GoogleMaps.MapsActivity;
import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.model.EventData;
import pt.unl.fct.campus.firstwebapp.data.model.StoragePics;
import pt.unl.fct.campus.firstwebapp.data.model.UploadImageFromPhone;
import pt.unl.fct.campus.firstwebapp.ui.login.Main_Page;



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
    private AlertDialog.Builder alerta;


    private String token,location;
    
    private int timeHour,timeMinute;

    private  Calendar cal;

    MapsActivity map;

    UploadImageFromPhone uploadImageFromPhone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);



        eventViewModel = new ViewModelProvider(this, new EventViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(EventViewModel.class);

        alerta = new AlertDialog.Builder(CreateEventPage.this);
        alerta.setTitle("Atention!");

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

         bitmap = null;

         uploadImageFromPhone = new UploadImageFromPhone();

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

                if (!isDataValidText()) {

                 alerta.setMessage("Event name, goals and description must be filled !");
                 alerta.create().show();

               }else if(!isDataValidDate()){
                    alerta.setMessage("Event date, is not valid!");
                    alerta.create().show();

                }else if(!isDataValidHour()) {
                    alerta.setMessage("Event hour, is not valid!");
                    alerta.create().show();

                }else if(!isNumVolunteersValid(numVolunteers.getText().toString())){
                    alerta.setMessage("Number of volunteers, is not valid!");
                    alerta.create().show();

                }else if(bitmap == null){
                    alerta.setMessage("Must have an image!");
                    alerta.create().show();

                }else{

                    Gson gson = new Gson();

                    EventData e = new EventData();

                    // Create the Event object
                    Long vol = Long.valueOf(numVolunteers.getText().toString());


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

                    String o = gson.toJson(e);

                    RequestBody re = RequestBody.create(MediaType.parse("multipart/form-data"), o);

                     Map<String, RequestBody> map = new HashMap<>();

                      map.put("evd",re);

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

                    RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), f);
                    map.put("img_cover",fbody);

                    eventViewModel.createEvent(token,map);

                }
            }
        });

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

                }

            }
        });

    }

    public boolean isDataValidText() {
        if (isNameValid(eventName.getText().toString()) == false || isNameValid(goal.getText().toString()) == false
                || isNameValid(description.getText().toString()) == false) {
            return false;
        }

        return true;
    }


    public boolean isDataValidDate() {
        Date startDate = null, endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(eventDateCreation.getText().toString());

            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(eventDue.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (isStartDayValid(startDate) == false || isFinishDayValid(startDate, endDate) == false) {
            return false;
        }

        return  true;
    }


    public boolean isDataValidHour() {

        if(isHourValid(eventStartHour.getText().toString()) == false || isHourValid(eventFinalHour.getText().toString()) == false){
            return false;
        }
        return true;
    }


    private boolean isNumVolunteersValid(String num){

        if(num.isEmpty() || num == null) {
            return false;

        }else if( num.charAt(0) == '0' ||Long.valueOf(num) == 0) {
            return false;
        }


        return  true;
    }

    private boolean isNameValid(String name) {
        if (name == null || name.isEmpty())
            return false;

        return true;
    }

    private boolean isStartDayValid(Date day) {

        cal =  Calendar.getInstance();

        if(day == null)
            return false;


        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(day);
        c2.setTime(cal.getTime());



        int yearDiff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        int monthDiff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        int dayDiff = c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);


        if(yearDiff < 0 || monthDiff < 0 || dayDiff < 0) {
            return false;
        }

        return true ;
    }

    private boolean isFinishDayValid(Date startDay, Date finalDay) {

        if(finalDay == null )
            return false;

        if(finalDay.before( startDay))
            return false;

        return true ;
    }

    private boolean isHourValid(String hour) {

        String h = "Hour";

        if(hour == null || hour.isEmpty() || hour.contains(h) )
            return false;

        return true ;
    }


    public void doCalendarSelection(DatePickerDialog.OnDateSetListener mDateSetListener){

        cal =  Calendar.getInstance();

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
                       // int indexImage = cursor.getColumnIndex(imageProj[0]);
                         //bitmap = null;
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


        alerta.setMessage("Event Created whith Success!");
        alerta.create().show();

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

}
