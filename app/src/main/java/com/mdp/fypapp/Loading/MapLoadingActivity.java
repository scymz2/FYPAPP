package com.mdp.fypapp.Loading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ImageView;

import com.mdp.fypapp.Activities.MapActivity;
import com.mdp.fypapp.R;

public class MapLoadingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_loading);

        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 700);
    }

    class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), MapActivity.class));
            MapLoadingActivity.this.finish();
        }
    }
}
