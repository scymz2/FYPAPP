package com.mdp.fypapp.Model;

import java.util.ArrayList;
import java.util.List;

public class EnvData {

    private int light;
    private int noise;
    private int temperature;
    private long time;

    public int getLight() {
        return light;
    }

    public int getNoise() {
        return noise;
    }

    public int getTemperature() {
        return temperature;
    }

    public EnvData(){}

    public EnvData(long time, int light, int noise, int temperature) {
        this.time = time;
        this.light = light;
        this.noise = noise;
        this.temperature = temperature;
    }

    public long getTime() {
        return time;
    }



//    public static List<EnvData> getWeekData(){
//        List<EnvData> list = new ArrayList<EnvData>(7);
//        list.add(new EnvData("5", 4));
//        list.add(new EnvData("7", 7));
//        list.add(new EnvData("9", 14));
//        list.add(new EnvData("11", 24));
//        list.add(new EnvData("13", 20));
//        list.add(new EnvData("15", 10));
//        list.add(new EnvData("17", -2));
//        return list;
//    }


}
