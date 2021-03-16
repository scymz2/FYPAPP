package com.mdp.fypapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mdp.fypapp.Charts.TempLineChart;
import com.mdp.fypapp.Data.EnvData;
import com.mdp.fypapp.R;

import java.util.List;

import static com.mdp.fypapp.Data.EnvData.getWeekData;

public class DataViewActivity extends AppCompatActivity {

    WebView webView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);
        webView1 = findViewById(R.id.webView1);

        //获取指定格式的数据
        List<EnvData> datas = getWeekData();

        //进行webview设置
        WebSettings webSettings1 = webView1.getSettings();

        webSettings1.setJavaScriptEnabled(true);
        webSettings1.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings1.setSupportZoom(true);
        webSettings1.setDisplayZoomControls(true);

        //给javascript传递生成的mylineChart的option
        webView1.addJavascriptInterface(new TempLineChart(this,datas),"myLine");
        webView1.loadUrl("file:///android_asset/lineChart.html");
        webView1.setWebViewClient(new WebViewClient());


    }
}