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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.mdp.fypapp.Loading.ChatbotLoadingActivity;
import com.mdp.fypapp.Loading.DataViewLoadingActivity;
import com.mdp.fypapp.Loading.MapLoadingActivity;
import com.mdp.fypapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERR_DIALOG_REQUEST = 9001;

    private CardView tool;
    private CardView sensor;
    private CardView chatbot;
    private CardView data;
    private FloatingActionButton chatBtn;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private MaterialToolbar actionBar;
    private DrawerLayout mainDrawer;
    private TextView status_tv, time, temperature, humidity, description;
    private ImageView status;
    private ImageView avatar;
    private String d, t, h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.status);
        status_tv = findViewById(R.id.status_tv);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(System.currentTimeMillis());

        time = findViewById(R.id.currenttime);
        time.setText(simpleDateFormat.format(date));

        //google service
        if (isServiceOK()) {
            //Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();
            status.setImageResource(R.drawable.connected);
            status_tv.setText("connected");

        }

        actionBar = findViewById(R.id.appBar);
        mainDrawer = findViewById(R.id.mainDrawer);
        setUpDrawerLayout();

        //getWeather
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Ningbo,china&APPID=885aa9eb815b6b6b1b24ce7ade4b78d9";
        HttpUtil.sendRequestWithOkhttp(url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList data = parseJsonWithJsonObject(response);
                d = (String) data.get(0);
                t = (String) data.get(1);
                h = (String) data.get(2);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        description.setText(d);
                        temperature.setText(String.valueOf(Double.parseDouble(t)- 273.15));
                        humidity.setText(h + " %");
                    }
                });
            }
        });


        //profile
        avatar = findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        //chatbtn
        chatBtn = findViewById(R.id.btnBot);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChatbotActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


        //tool
        tool = findViewById(R.id.quiz);
        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


        //chatbot
        chatbot = findViewById(R.id.chatbot);
        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChatbotLoadingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //data
        data = findViewById(R.id.data);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DataViewLoadingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //map
        sensor = findViewById(R.id.sensor);
        sensor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapLoadingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //temp & humidity
        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        description = findViewById(R.id.description);

    }

    private void setUpDrawerLayout() {
        setSupportActionBar(actionBar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainDrawer, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.syncState();
    }

    public boolean isServiceOK() {
        Log.d(TAG, "isServiceOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServiceOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServiceOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERR_DIALOG_REQUEST);
        } else {
            Toast.makeText(this, "you can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    static class HttpUtil {

        public static void sendRequestWithOkhttp(String address, okhttp3.Callback callback) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            client.newCall(request).enqueue(callback);
        }
    }

    private ArrayList parseJsonWithJsonObject(Response response) throws IOException {
        String responseData = response.body().string();
        ArrayList arrayList = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray weather = jsonObject.getJSONArray("weather");
            JSONObject main = jsonObject.getJSONObject("main");

            JSONObject Weather = weather.getJSONObject(0);
            String description = Weather.getString("description");
            String temp = main.getString("temp");
            String humidity = main.getString("humidity");

            arrayList.add(description);
            arrayList.add(temp);
            arrayList.add(humidity);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}