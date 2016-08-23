package com.oceanwing.at.routing.google;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bounds {

    @SerializedName("northeast")
    @Expose
    private LatLng northeast;
    @SerializedName("southwest")
    @Expose
    private LatLng southwest;

    /**
     * @return The northeast
     */
    public LatLng getNortheast() {
        return northeast;
    }

    /**
     * @param northeast The northeast
     */
    public void setNortheast(LatLng northeast) {
        this.northeast = northeast;
    }

    /**
     * @return The southwest
     */
    public LatLng getSouthwest() {
        return southwest;
    }

    /**
     * @param southwest The southwest
     */
    public void setSouthwest(LatLng southwest) {
        this.southwest = southwest;
    }

}
