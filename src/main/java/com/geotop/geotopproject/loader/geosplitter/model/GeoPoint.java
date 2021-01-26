package com.geotop.geotopproject.loader.geosplitter.model;


public class GeoPoint {
    private double longitude;
    private double latitude;

    public GeoPoint(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return String.valueOf(latitude) + ", " + String.valueOf(longitude);
    }
}
