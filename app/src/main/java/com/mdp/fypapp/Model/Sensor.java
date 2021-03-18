package com.mdp.fypapp.Model;

public class Sensor {
    private double lat;
    private double lng;
    private double temp;
    private double humdi;
    private double noise;
    private double light;

    public Sensor() {
    }

    public Sensor(double lat, double lng, double temp, double humdi, double noise, double light) {
        this.lat = lat;
        this.lng = lng;
        this.temp = temp;
        this.humdi = humdi;
        this.noise = noise;
        this.light = light;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumdi() {
        return humdi;
    }

    public void setHumdi(double humdi) {
        this.humdi = humdi;
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
