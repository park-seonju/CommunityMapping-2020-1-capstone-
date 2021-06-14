package com.capstone.maps;

public class LocationHelper {
    private double Longitude;
    private double Latitude;
    private String Content;

    public LocationHelper(double longitude, double latitude, String content) {
        Longitude = longitude;
        Latitude = latitude;
        Content = content;
    }
    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

}




