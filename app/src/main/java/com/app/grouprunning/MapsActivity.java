package com.app.grouprunning;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import info.hoang8f.widget.FButton;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,LocationListener {


    private GoogleMap mMap;
    LocationManager userLocation;
    Location location;
    private static double latitude =0.0;
    private static double longitude = 0.0;
    FButton button;
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = (FButton)findViewById(R.id.startButton);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

      toggleButton();

    }

    public void toggleButton(){

        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                String text = button.getText().toString();
                if (text.equals("Start")) {
                    textToSpeech.speak("Run Forest, run",TextToSpeech.QUEUE_FLUSH,null,null);
                    button.setButtonColor(Color.RED);
                    button.setText("Stop");

                } else {
                    textToSpeech.speak("Activity stopped", TextToSpeech.QUEUE_FLUSH, null, null);
                    button.setButtonColor(Color.GREEN);
                    button.setText("Start");


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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            userLocation = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            location = userLocation.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location!=null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng currentLatLng = new LatLng(latitude, longitude);
                //setting initial zoom
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));

                //SOURCE: http://stackoverflow.com/questions/18425141/android-google-maps-api-v2-zoom-to-current-location
                //animate camera to zoom to user location
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentLatLng)      // Sets the center of the map to location user
                        .zoom(16)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } else {
            // Show rationale and request permission.
        }
        // Add a marker in Sydney and move the camera

    }
    @Override
    public void onLocationChanged(Location location){
        this.location = location;
//        latitude =  this.location.getLatitude();
//        longitude = this.location.getLongitude();
//        System.out.println("Latitude: "+latitude);
//        System.out.println("Longitude: "+longitude);
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
}