package com.mdp.fypapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.mdp.fypapp.Adapter.StationAdapter;
import com.mdp.fypapp.Model.Sensor;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.List;

public class StationActivity extends AppCompatActivity {

    private RecyclerView stationView;
    private Sensor sensor, sensor1, sensor2, sensor3, sensor4, sensor5, sensor6, sensor7;
    private List<Sensor> sensors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        stationView = findViewById(R.id.recyclerView);


        sensor = new Sensor(29.8006417, 121.5627401, 24, 0.5, 47, 200);
        sensor1 = new Sensor(29.8006417, 121.5627401, 24, 0.5, 47, 200);
        sensor2 = new Sensor(29.8006417, 121.5627401, 24, 0.5, 47, 200);
        sensor3 = new Sensor(29.8006417, 121.5627401, 24, 0.5, 47, 200);
        sensor4 = new Sensor(29.8006417, 121.5627401, 24, 0.5, 47, 200);
        sensor5 = new Sensor(29.8006417, 121.5627401, 24, 0.5, 47, 200);
        sensor6 = new Sensor(29.8006417, 121.5627401, 24, 0.5, 47, 200);
        sensor7 = new Sensor(29.8006417, 121.5627401, 24, 0.5, 47, 200);

        sensors.add(sensor);
        sensors.add(sensor1);
        sensors.add(sensor2);
        sensors.add(sensor3);
        sensors.add(sensor4);
        sensors.add(sensor5);
        sensors.add(sensor6);
        sensors.add(sensor7);




        StationAdapter stationAdapter = new StationAdapter(this, sensors);
        stationView.setAdapter(stationAdapter);
        stationView.setLayoutManager(new LinearLayoutManager(this));

    }
}