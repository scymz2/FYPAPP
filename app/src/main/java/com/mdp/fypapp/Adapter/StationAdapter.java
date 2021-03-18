package com.mdp.fypapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdp.fypapp.Model.Sensor;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder>{

    List<Sensor> sensors = new ArrayList<>();
    Context context;

    public StationAdapter(Context ct, List<Sensor> sensors){
        context = ct;
        this.sensors = sensors;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.station_segment, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.id.setText("Sensor "+position);
        holder.latitude.setText(String.valueOf(sensors.get(position).getLat()));
        holder.longitude.setText(String.valueOf(sensors.get(position).getLng()));
        holder.temperature.setText(String.valueOf(sensors.get(position).getTemp()));
        holder.humidity.setText(String.valueOf(sensors.get(position).getHumdi()));

    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, latitude, longitude, temperature, humidity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.textView7);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            temperature = itemView.findViewById(R.id.temperature);
            humidity = itemView.findViewById(R.id.humidity);
        }
    }
}
