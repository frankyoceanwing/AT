package com.oceanwing.at.routing.here;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Position {

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public Position(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public String geoString() {
        return String.format("geo!%.6f,%.6f", latitude, longitude);
    }

    public Position valueOf(String shape) {
        String[] latLng = shape.split(",");
        if (latLng.length != 2) {
            throw new IllegalArgumentException("illegal shape");
        }
        return new Position(Double.valueOf(latLng[0].trim()), Double.valueOf(latLng[1].trim()));
    }

}
