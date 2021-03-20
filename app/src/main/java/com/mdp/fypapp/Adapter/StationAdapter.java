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
        holder.temperature.setText(String.valueOf(sensors.get(position).getTemp()));
        holder.light.setText(String.valueOf(sensors.get(position).getLight()));
        holder.sound.setText(String.valueOf(sensors.get(position).getNoise()));

    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, light, sound, temperature;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.textView7);
            temperature = itemView.findViewById(R.id.temperature);
            light = itemView.findViewById(R.id.light);
            sound = itemView.findViewById(R.id.sound);
        }
    }
}
