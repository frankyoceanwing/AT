package com.oceanwing.at.routing.here;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Mode {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("transportModes")
    @Expose
    private List<String> transportModes = new ArrayList<String>();
    @SerializedName("trafficMode")
    @Expose
    private String trafficMode;
    @SerializedName("feature")
    @Expose
    private List<Feature> feature = new ArrayList<Feature>();

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The transportModes
     */
    public List<String> getTransportModes() {
        return transportModes;
    }

    /**
     * @param transportModes The transportModes
     */
    public void setTransportModes(List<String> transportModes) {
        this.transportModes = transportModes;
    }

    /**
     * @return The trafficMode
     */
    public String getTrafficMode() {
        return trafficMode;
    }

    /**
     * @param trafficMode The trafficMode
     */
    public void setTrafficMode(String trafficMode) {
        this.trafficMode = trafficMode;
    }

    /**
     * @return The feature
     */
    public List<Feature> getFeature() {
        return feature;
    }

    /**
     * @param feature The feature
     */
    public void setFeature(List<Feature> feature) {
        this.feature = feature;
    }

}
