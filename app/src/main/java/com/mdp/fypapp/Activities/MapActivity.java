package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.mdp.fypapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 18f;

    private Boolean mLocationPermissionGranted = false;
    private LinearLayout map, heat, noise;
    private RelativeLayout locate;
    private GoogleMap gMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private TileOverlay overlay;
    private Polygon polygon1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map = findViewById(R.id.Map);
        heat = findViewById(R.id.heatMap);
        noise = findViewById(R.id. noiseMap);
        locate = findViewById(R.id.locate);

        getLocationPermission();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(overlay != null){
                    overlay.remove();
                    gMap.clear();
                    addMarker();
                    addPolygon();
                }
            }
        });

        heat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHeatMap();
                addPolygon();
            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapLocate();
            }
        });



    }

    private LatLng translate(double latitude, double longitude){
        return new LatLng(latitude - 0.002588150652347, longitude + 0.00417007670166);
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

        // 移动地图到指定经度的位置
        mapLocate();

        //添加标记到指定经纬度
        addMarker();
        addPolygon();


        //动态展示infowindow的数据
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getTitle().equals("Marker")){
                    if(gMap != null){
                        gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                View row = getLayoutInflater().inflate(R.layout.custom_info_window,null);
                                return row;
                            }
                        });
                    }
                    marker.showInfoWindow();
                }
                return true;
            }
        });


        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d(TAG, "onInfoWindowClick: " + marker.getTitle().equals("Marker"));
                if(marker.getTitle().equals("Marker")) {
                    Intent i = new Intent(MapActivity.this, StationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else{
                    Toast.makeText(MapActivity.this, "Connection Lost", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        //add circles
//        CircleOptions circleOptions = new CircleOptions()
//                .center(appointLoc)
//                .strokeWidth(0)
//                .fillColor(0x62FF0000)
//                .radius(100);
//
//        CircleOptions circleOptions1 = new CircleOptions()
//                .center(new LatLng(29.800060, 121.564924))
//                .strokeWidth(0)
//                .fillColor(0x62FFFF00)
//                .radius(300);
//
//        CircleOptions circleOptions2 = new CircleOptions()
//                .center(new LatLng(29.798608, 121.561029))
//                .strokeWidth(0)
//                .fillColor(0x6200FF00)
//                .radius(200);
//
//        gMap.addCircle(circleOptions);
//        gMap.addCircle(circleOptions1);
//        gMap.addCircle(circleOptions2);
    }

    private void addPolygon(){
        polygon1 = gMap.addPolygon(new PolygonOptions()
                .add(translate(29.80364656061833, 121.5593776047536),
                        translate(29.803885331271914, 121.55977907661506),
                        translate(29.804108444318363, 121.55996627978641),
                        translate(29.80397927366794, 121.56015573841763),
                        translate(29.803859888524485, 121.56013769473849),
                        translate(29.803775731698405, 121.56003845450309),
                        translate(29.803748331786267, 121.56007454186143),
                        translate(29.803832488635347, 121.56023693498146),
                        translate(29.803734631827346, 121.5603294088372),
                        translate(29.803409746538215, 121.5603925617143),
                        translate(29.802932202390622, 121.56005424272992),
                        translate(29.80297917404723, 121.55986703955858),
                        translate(29.803227732029537, 121.5599031269169),
                        translate(29.803149446117487, 121.55979035392211))
                .strokeColor(Color.TRANSPARENT)
                .fillColor(Color.argb(128, 0, 0, 255)));
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

    private void addHeatMap() {
        gMap.clear();

        List<WeightedLatLng> weightedLatLngs = new ArrayList<>();
        weightedLatLngs.add(new WeightedLatLng(new LatLng(29.801307706799356, 121.56411954566717), 100));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(29.801307706799356, 121.56411954566717), 100));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(29.801407706799356, 121.56401954566717), 60));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(29.801457706799356, 121.56421954566717), 80));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(29.800646695225527, 121.5639156977986), 80));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(29.800646695225527, 121.5639156977986), 80));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(29.80092599642338, 121.56433412237094), 80));
        weightedLatLngs.add(new WeightedLatLng(new LatLng(29.80092599642338, 121.56433412237094), 80));
        // Get the data: latitude/longitude positions of police; stations.
        

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .weightedData(weightedLatLngs).radius(50)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        overlay = gMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }

    private List<LatLng> readItems(@RawRes int resource) throws JSONException {
        List<LatLng> result = new ArrayList<>();
        InputStream inputStream = MapActivity.this.getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            result.add(new LatLng(lat, lng));
        }
        return result;
    }

    private void addMarker(){
        //添加标记到指定经纬度
        gMap.addMarker(new MarkerOptions().position(new LatLng(29.801307706799356, 121.56411954566717)).title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2)));


        gMap.addMarker(new MarkerOptions().position(new LatLng(29.800646695225527, 121.5639156977986)).title("Marker1")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker3)));


        gMap.addMarker(new MarkerOptions().position(new LatLng(29.80092599642338, 121.56433412237094)).title("Marker2")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker3)));

    }

    private void mapLocate(){
        double lat = 29.800981856569372;
        double lng = 121.56409808799681;
        LatLng appointLoc = new LatLng(lat, lng);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(appointLoc, DEFAULT_ZOOM));
    }

}