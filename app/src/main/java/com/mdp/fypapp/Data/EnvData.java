package com.mdp.fypapp.Data;

import java.util.ArrayList;
import java.util.List;

public class EnvData {

    private String time;
    private double data;

    public EnvData(String time, double data) {
        this.time = time;
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public double getData() {
        return data;
    }


    public static List<EnvData> getWeekData(){
        List<EnvData> list = new ArrayList<EnvData>(7);
        list.add(new EnvData("5", 4));
        list.add(new EnvData("7", 7));
        list.add(new EnvData("9", 14));
        list.add(new EnvData("11", 24));
        list.add(new EnvData("13", 20));
        list.add(new EnvData("15", 10));
        list.add(new EnvData("17", -2));
        return list;
    }


}
