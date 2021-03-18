package com.mdp.fypapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mdp.fypapp.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERR_DIALOG_REQUEST = 9001;

    private WebView webView;
    private CardView tool;
    private CardView sensor;
    private CardView chatbot;
    private CardView data;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //google service
        if(isServiceOK()){
            Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();
        }

        // progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        // webview
        webView = findViewById(R.id.weather);
        webView.setVerticalScrollBarEnabled(false);

        webView.loadUrl("https://weather.com/weather/today/l/29.80,121.56?par=google&temp=f");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                super.onPageCommitVisible(view, url);
            }
        });


        //chatbot
        chatbot = findViewById(R.id.chatbot);
        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChatbotActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //data
        data = findViewById(R.id.data);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DataViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //map
        sensor = findViewById(R.id.sensor);
        sensor.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

    public boolean isServiceOK(){
        Log.d(TAG, "isServiceOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServiceOK: Google Play Services is working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServiceOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERR_DIALOG_REQUEST);
        }else{
            Toast.makeText(this,"you can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}