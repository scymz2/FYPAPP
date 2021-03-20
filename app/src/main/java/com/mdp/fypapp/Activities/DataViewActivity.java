package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mdp.fypapp.Charts.TempLineChart;
import com.mdp.fypapp.Data.EnvData;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.List;


public class DataViewActivity extends AppCompatActivity {

    private static String TAG = "dataviewactivity";
    private ProgressBar progressBar;
    WebView webView1;
    DatabaseReference reference;
    List<EnvData> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);
        webView1 = findViewById(R.id.webView1);

        // progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        //获取指定格式的数据
        reference = FirebaseDatabase.getInstance().getReference("IoTDevices").child("station1");
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e(TAG, "Error getting data", task.getException());
                }else{
                    Log.d(TAG, String.valueOf(task.getResult().getValue()));
                    for(DataSnapshot child: task.getResult().getChildren()){
                        Log.d(TAG, String.valueOf(child.getValue()));
                        EnvData data = child.getValue(EnvData.class);
                        dataList.add(data);
                    }
                }
                Log.d(TAG, "onComplete: dataloaded!");
                webView1.reload();
            }
        });
        loadChart(dataList);

    }

    private void loadChart(List dataList) {

        //进行webview设置
        WebSettings webSettings1 = webView1.getSettings();

        webSettings1.setJavaScriptEnabled(true);
        webSettings1.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings1.setSupportZoom(true);
        webSettings1.setDisplayZoomControls(true);

        //给javascript传递生成的mylineChart的option
        webView1.addJavascriptInterface(new TempLineChart(this,dataList),"myLine");
        webView1.loadUrl("file:///android_asset/lineChart.html");
        webView1.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                super.onPageCommitVisible(view, url);
            }
        });
        Log.d(TAG, "loadChart: finish!");
    }
}