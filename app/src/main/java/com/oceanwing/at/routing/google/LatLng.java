package com.oceanwing.at.routing.google;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatLng {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    private LatLng() {}

    public LatLng(@NonNull Double lat, @NonNull Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * @return The lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     * @param lat The lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * @return The lng
     */
    public Double getLng() {
        return lng;
    }

    /**
     * @param lng The lng
     */
    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return String.format("%.6f,%.6f", lat, lng);
    }


}
