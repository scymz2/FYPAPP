package com.mdp.fypapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mdp.fypapp.Charts.EchartsDataBean;
import com.mdp.fypapp.R;

public class ChartActivity extends AppCompatActivity {

    WebView echart_show;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        initView();
        initData();
        initListener();

    }

    private void initView() {
        echart_show = (WebView) findViewById(R.id.show_echart);
    }

    // 进度条
    private void initData() {
        TAG = this.getClass().getName();
    }
    private void initListener() {

        // 进行WebView设置
        WebSettings webSettings = echart_show.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        echart_show.loadUrl("file:///android_asset/myechart.html");
        echart_show.setWebViewClient(new WebViewClient() {
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
                echart_show.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getEchartsLineJson() + ");");
                //echart_show.loadUrl("javascript:createChart2('bar'," + EchartsDataBean.getInstance().getEchartsBarJson() + ");");
                //echart_show.loadUrl("javascript:createChart3('pie'," + EchartsDataBean.getInstance().getEchartsPieJson() + ");");
            }
        });
    }





}