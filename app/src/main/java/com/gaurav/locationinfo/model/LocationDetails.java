package com.gaurav.locationinfo.model;

/**
 * Created by Gaurav Sharma on 28-06-2021 on 01:43 .
 */
public class LocationDetails {

    private String latitude,longitude,accuracy,bearing,altitude,speed,time;


    public LocationDetails(String latitude, String longitude, String accuracy, String bearing, String altitude, String speed, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.bearing = bearing;
        this.altitude = altitude;
        this.speed = speed;
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
