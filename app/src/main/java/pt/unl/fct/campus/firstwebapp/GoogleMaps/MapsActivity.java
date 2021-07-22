package pt.unl.fct.campus.firstwebapp.GoogleMaps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.CreateEventPage;
import pt.unl.fct.campus.firstwebapp.data.Events.SeeEventsPage;
import pt.unl.fct.campus.firstwebapp.data.model.Location;
import pt.unl.fct.campus.firstwebapp.data.model.LooClass;
import pt.unl.fct.campus.firstwebapp.data.model.UpcomingEventsArgs;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private AutoCompleteTextView meetingPlace;
    private Button next;
    Bundle params;
    Address address;
    String next_null;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_CODE = 1234;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        meetingPlace = findViewById(R.id.eventPlace2);
        next = findViewById(R.id.ButtonFinishMap);
        getPermission();

        Intent oldIntent = getIntent();
        params = oldIntent.getExtras();


            if(address == null){
                next.setVisibility(View.INVISIBLE);
            }

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(address!= null) {

                    if(params.getString("Page").equals("SeeEvents"))
                        openNextPage(SeeEventsPage.class);

                    if(params.getString("Page").equals("CreateEvents"))
                    openNextPage( CreateEventPage.class);
                }else{
                    Toast.makeText(MapsActivity.this,"Must have a location!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera initially
        LatLng mark = new LatLng(-34, 151);

        Double lat = params.getDouble("latitude");
        Double longitute = params.getDouble("longitude");

        next_null = params.getString("Next_Null");


        if (lat != 0.0) {

            mark = new LatLng(lat, longitute);

            if (next_null != null)
                next.setVisibility(View.INVISIBLE);

        } else if (next_null == null){
            next.setVisibility(View.VISIBLE);
    }

        mMap.addMarker(new MarkerOptions().position(mark).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));

        init();
    }


    private void getPermission(){
        String [] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
            }
        }else{
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_CODE);

        }
    }

    public void initMap(){

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
       mapFragment.getMapAsync(MapsActivity.this);

    }


    public void init(){


        meetingPlace.setAdapter(new PlaceAutoSuggestAdapter(MapsActivity.this,android.R.layout.simple_list_item_1));

        meetingPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LatLng latLng = getLatLngFromAddress(meetingPlace.getText().toString());

                if(latLng!=null) {

                     address= getAddressFromLatLng(latLng);

                    if(address!=null) {

                        mMap.addMarker(new MarkerOptions().position(latLng).title(address.getLocality()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));



                        hideKeyboard();

                       // geolocate();
                    }
                    else {
                        Log.d("Adddress","Address Not Found");
                    }
                }
                else {
                    Log.d("Lat Lng","Lat Lng Not Found");
                }

            }
        });



    }


    private LatLng getLatLngFromAddress(String address){

        Geocoder geocoder=new Geocoder(MapsActivity.this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if(addressList!=null){
                Address singleaddress=addressList.get(0);
                LatLng latLng=new LatLng(singleaddress.getLatitude(),singleaddress.getLongitude());
                return latLng;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private Address getAddressFromLatLng(LatLng latLng){
        Geocoder geocoder=new Geocoder(MapsActivity.this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if(addresses!=null){
                Address address=addresses.get(0);
                return address;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    /*private void geolocate(){

        String searchOriginString = meetingPlace.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);

        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchOriginString, 1);
        }catch (IOException e){
           Log.e("MapsActivity", "geoLocate: IOException: " + e.getMessage() );
           e.printStackTrace();
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d("MapsActivity", "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            LatLng geoPlace = new LatLng(address.getLatitude(), address.getLongitude());

            mMap.addMarker(new MarkerOptions().position(geoPlace).title("Marker in GeoPlace"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geoPlace,15f));
            hideKeyboard();

        }

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_CODE:{


                if(grantResults.length > 0){

                    for(int i = 0; i < grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    public void openNextPage(Class page){

        Intent intent = new Intent(this ,page);

        Intent oldIntent = getIntent();

       /* Location loc = new Location("34",address.getLocality(),new LooClass(address.getLatitude(),address.getLongitude()));

        Gson g = new Gson();

        String c = g.toJson(loc);
*/

        if(oldIntent != null) {
            params = oldIntent.getExtras();
           // params.putString("location", c);
            params.putDouble("Latitude",address.getLatitude());
            params.putDouble("Longitude", address.getLongitude());
           // params.putString("POSTAL_CODE", address.getPostalCode() );
            //params.putString("COUNTRY_NAME",address.getCountryName());
            params.putString("LOCALITY",address.getLocality());

            if(params != null)
                intent.putExtras(params);
        } else {
            params = new Bundle();
        }

        startActivity(intent);

    }

    private void hideKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}