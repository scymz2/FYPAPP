package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.type.Color;
import com.mdp.fypapp.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 16f;

    private Boolean mLocationPermissionGranted = false;
    private GoogleMap gMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLocationPermission();
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            Log.d(TAG, "onComplete: location is " + currentLocation.getLatitude() + "," + currentLocation.getLongitude());

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        } else {
                            Log.d(TAG, "onComplete: current location is null!");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to lat" + latLng.latitude + ", lng" + latLng.longitude);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void setMarker(LatLng latLng){
        gMap.addMarker(new MarkerOptions().position(latLng).title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;

        if(gMap != null){
            gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View row = getLayoutInflater().inflate(R.layout.custom_info_window,null);
                    TextView number = findViewById(R.id.number);
                    TextView status = findViewById(R.id.status);


                    return row;
                }
            });
        }

        //set ui modules for the Map

        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setTiltGesturesEnabled(false);
        gMap.getUiSettings().setRotateGesturesEnabled(false);
//        if (mLocationPermissionGranted) {
//            getDeviceLocation();
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            gMap.setMyLocationEnabled(true);
//        }

        //location of nottingham
        double lat = 29.800641711544987;
        double lng = 121.56274010792607;
        LatLng appointLoc = new LatLng(lat, lng);

        // 移动地图到指定经度的位置
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(appointLoc, DEFAULT_ZOOM));

        //添加标记到指定经纬度
        gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));


        gMap.addMarker(new MarkerOptions().position(new LatLng(29.800060, 121.564924)).title("Marker1")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));


        gMap.addMarker(new MarkerOptions().position(new LatLng(29.798608, 121.561029)).title("Marker2")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));






        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent i = new Intent(MapActivity.this, StationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //add circles
        CircleOptions circleOptions = new CircleOptions()
                .center(appointLoc)
                .strokeWidth(0)
                .fillColor(0x62FF0000)
                .radius(100);

        CircleOptions circleOptions1 = new CircleOptions()
                .center(new LatLng(29.800060, 121.564924))
                .strokeWidth(0)
                .fillColor(0x62FFFF00)
                .radius(300);

        CircleOptions circleOptions2 = new CircleOptions()
                .center(new LatLng(29.798608, 121.561029))
                .strokeWidth(0)
                .fillColor(0x6200FF00)
                .radius(200);

        gMap.addCircle(circleOptions);
        gMap.addCircle(circleOptions1);
        gMap.addCircle(circleOptions2);
    }

    private void getLocationPermission(){

        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i =0; i < grantResults.length; i++){
                        mLocationPermissionGranted = false;
                        return;
                    }
                    mLocationPermissionGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }



}