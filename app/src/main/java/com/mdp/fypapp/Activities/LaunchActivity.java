package com.mdp.fypapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;


import com.mdp.fypapp.R;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Handler x = new Handler();

        x.postDelayed(new splashhandler(), 2000);
    }

    class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(),MainActivity.class));
            LaunchActivity.this.finish();// 把当前的LaunchActivity结束掉
        }
    }
}
