package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mdp.fypapp.Adapter.StationAdapter;
import com.mdp.fypapp.Charts.EchartsDataBean;
import com.mdp.fypapp.Charts.TempLineChart;
import com.mdp.fypapp.Model.EnvData;
import com.mdp.fypapp.Model.Sensor;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.List;

public class StationActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private ProgressBar progressBar1, progressBar2, progressBar3;
    private FloatingActionButton chatbot;
    WebView webView1, webView2, webView3;
    RelativeLayout topBar;
    ImageView back;
    DatabaseReference reference;
    List<EnvData> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        chatbot = findViewById(R.id.btnBot);
        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StationActivity.this, ChatbotActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        chatbot.bringToFront();

        topBar = findViewById(R.id.top);
        topBar.bringToFront();

        back = findViewById(R.id.backToMain);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        webView1 = findViewById(R.id.webView1);
        webView2 = findViewById(R.id.webView2);
        webView3 = findViewById(R.id.webView3);

        // progress bar
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2  = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar1.setMax(100);
        progressBar2.setMax(100);
        progressBar3.setMax(100);

//        //获取指定格式的数据
//        reference = FirebaseDatabase.getInstance().getReference("IoTDevices").child("station1");
//        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(!task.isSuccessful()){
//                    Log.e(TAG, "Error getting data", task.getException());
//                }else{
//                    Log.d(TAG, String.valueOf(task.getResult().getValue()));
//                    for(DataSnapshot child: task.getResult().getChildren()){
//                        Log.d(TAG, String.valueOf(child.getValue()));
//                        EnvData data = child.getValue(EnvData.class);
//                        dataList.add(data);
//                    }
//                }
//                Log.d(TAG, "onComplete: dataloaded!");
//                webView1.reload();
//            }
//        });
        loadChart(webView1, progressBar1, 1);
        loadChart(webView2, progressBar2, 2);
        loadChart(webView3, progressBar3, 3);

    }

    private void loadChart(WebView webView, ProgressBar Bar, int i) {

        // 进行WebView设置
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        switch(i){
            case 1:
                webView.loadUrl("file:///android_asset/tempChart.html");
                break;
            case 2:
                webView.loadUrl("file:///android_asset/humiChart.html");
                break;
            case 3:
                webView.loadUrl("file:///android_asset/lightChart.html");
                break;
        }
        Bar.setVisibility(View.GONE);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //最好在这里调用js代码 以免网页未加载完成
                view.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getEchartsLineJson() + ");");
            }
        });

//        //进行webview设置
//        WebSettings webSettings1 = webView1.getSettings();
//
//        webSettings1.setJavaScriptEnabled(true);
//        webSettings1.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings1.setSupportZoom(true);
//        webSettings1.setDisplayZoomControls(true);
//
//        //给javascript传递生成的mylineChart的option
//        webView1.addJavascriptInterface(new TempLineChart(this,dataList),"myLine");
//        webView1.loadUrl("file:///android_asset/tempChart.html");
//        webView1.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageCommitVisible(WebView view, String url) {
//                progressBar.setVisibility(View.INVISIBLE);
//                super.onPageCommitVisible(view, url);
//            }
//        });
//        Log.d(TAG, "loadChart: finish!");
    }
}