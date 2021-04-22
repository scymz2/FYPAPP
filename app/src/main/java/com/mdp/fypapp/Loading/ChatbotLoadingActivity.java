package com.mdp.fypapp.Loading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.mdp.fypapp.Activities.ChatbotActivity;
import com.mdp.fypapp.Activities.MapActivity;
import com.mdp.fypapp.R;

public class ChatbotLoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_loading);

        Handler x = new Handler();
        x.postDelayed(new ChatbotLoadingActivity.splashhandler(), 700);
    }

    class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), ChatbotActivity.class));
            ChatbotLoadingActivity.this.finish();
        }
    }
}