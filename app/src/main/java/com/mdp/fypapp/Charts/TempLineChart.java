package com.mdp.fypapp.Charts;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.AxisLine;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.SplitLine;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.code.Y;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.LineStyle;
import com.mdp.fypapp.Model.EnvData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TempLineChart {

    Context mContext;
    List<EnvData> lineDatas;


    public TempLineChart(Context context, List<EnvData> datas){
        this.mContext = context;
        this.lineDatas = datas;
    }

    //将该方法暴露给js脚本调用
    @JavascriptInterface
    public String getLineChartOptions(){
        GsonOption option = (GsonOption) createLineChartOptions();
        return option.toString();
    }

    //此函数主要是绘line图
    @JavascriptInterface
    public Option createLineChartOptions(){
        //创建option对象
        GsonOption option = new GsonOption();
        //设置图标标题，并且居中显示
        option.title().text("Temperature").x(X.center);
        //设置图例，居中显示
        option.legend().data("station1").x(X.right).y(Y.top).borderWidth(0);
        //设置y轴为值轴，并且不显示y轴，最大值设置400，最小值-100

        option.yAxis(new ValueAxis().name("℃").axisLine(new AxisLine()
        .show(true).lineStyle(new LineStyle().width(1))).max(40).min(-20));

        //backgroundcolor
        //option.backgroundColor("#100c2a");
        option.itemStyle(new ItemStyle().color("#4992ff"));

        //创建类目轴，并且不显示竖着的分割线
        CategoryAxis categoryAxisX = new CategoryAxis()
                .splitLine(new SplitLine().show(false))
                .axisLine(new AxisLine().onZero(true))
                .boundaryGap(true);



        //不显示表格边框
        option.grid().borderWidth(1);

        //创建line数据
        //此处开始使用用户数据
        Line line = new Line("station1").smooth(true).lineStyle(new LineStyle().color("#4992ff"));
        //根据获取的数据赋值
        for(EnvData lineData: lineDatas){
            //增加类目，值为日期
            Date date = new Date(lineData.getTime());
            SimpleDateFormat sfd = new SimpleDateFormat("HH:mm");
            sfd.format(date);
            categoryAxisX.data(date);
            //日期对应的数据
            line.data(lineData.getTemperature());
        }
        //设置x轴为类目轴
        option.xAxis(categoryAxisX);
        //设置数据

        option.series(line);
        return option;

    }


}
