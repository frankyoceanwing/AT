package com.oceanwing.at.routing.google;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GeocodedWaypoint {

    @SerializedName("geocoder_status")
    @Expose
    private GeocoderStatus geocoderStatus;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("types")
    @Expose
    private List<String> types = new ArrayList<String>();

    /**
     * @return The geocoderStatus
     */
    public GeocoderStatus getGeocoderStatus() {
        return geocoderStatus;
    }

    /**
     * @param geocoderStatus The geocoder_status
     */
    public void setGeocoderStatus(GeocoderStatus geocoderStatus) {
        this.geocoderStatus = geocoderStatus;
    }

    /**
     * @return The placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * @param placeId The place_id
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * @return The types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * @param types The types
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

}
