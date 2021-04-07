package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mdp.fypapp.Charts.EchartsDataBean;
import com.mdp.fypapp.Charts.TempLineChart;
import com.mdp.fypapp.Model.EnvData;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.List;


public class DataViewActivity extends AppCompatActivity {

    private static String TAG = "dataviewactivity";
    private ProgressBar progressBar;
    private FloatingActionButton chatbot;
    WebView webView1, webView2, webView3, webView4;
    DatabaseReference reference;
    List<EnvData> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);
        webView1 = findViewById(R.id.webView1);
        webView2 = findViewById(R.id.webView2);
        webView3 = findViewById(R.id.webView3);
        webView4 = findViewById(R.id.webView4);
        chatbot = findViewById(R.id.btnBot);

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DataViewActivity.this, ChatbotActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });



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
        loadChart(webView1, 1);
        loadChart(webView2, 2);
        loadChart(webView3, 3);
        loadChart(webView4, 4);

    }

    private void loadChart(WebView webView, int i) {

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        switch(i){
            case 1:
                webView.loadUrl("file:///android_asset/loginChart.html");
                break;
            case 2:
                webView.loadUrl("file:///android_asset/questionChart.html");
                break;
            case 3:
                webView.loadUrl("file:///android_asset/questionChart1.html");
                break;
            case 4:
                webView.loadUrl("file:///android_asset/mlChart.html");
        }
        //Bar.setVisibility(View.GONE);
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
    }
}