package com.mdp.fypapp.Charts;

import java.util.Arrays;

public class EchartsLineBean {

    public String type;
    public String title;
    public int maxValue;
    public int minValue;
    public String imageUrl;
    public String[] times;
    public int[] temp;


    @Override
    public String toString() {
        return "EchartsLineBean{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", maxValue=" + maxValue +
                ", minValue=" + minValue +
                ", imageUrl='" + imageUrl + '\'' +
                ", times=" + Arrays.toString(times) +
                ", steps=" + Arrays.toString(temp) +
                '}';
    }
}