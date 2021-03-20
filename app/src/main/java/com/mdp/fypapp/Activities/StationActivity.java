package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mdp.fypapp.Adapter.StationAdapter;
import com.mdp.fypapp.Model.Sensor;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.List;

public class StationActivity extends AppCompatActivity {

    private RecyclerView stationView;
    private Sensor sensor, sensor1, sensor2;
    private List<Sensor> sensors = new ArrayList<>();
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        stationView = findViewById(R.id.recyclerView);


        //获取数据
        reference = FirebaseDatabase.getInstance().getReference("IoTDevices");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sensor = new Sensor(24, 200, 47);
        sensor1 = new Sensor(24, 200, 47);
        sensor2 = new Sensor(24, 200, 47);

        sensors.add(sensor);
        sensors.add(sensor1);
        sensors.add(sensor2);





        StationAdapter stationAdapter = new StationAdapter(this, sensors);
        stationView.setAdapter(stationAdapter);
        stationView.setLayoutManager(new LinearLayoutManager(this));

    }
}