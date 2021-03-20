package com.mdp.fypapp.Model;

public class Sensor {
    private double temp;
    private double noise;
    private double light;

    public Sensor() {
    }

    public Sensor(double temp, double noise, double light) {
        this.temp = temp;
        this.noise = noise;
        this.light = light;
    }


    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }
}
