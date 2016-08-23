package com.oceanwing.at.routing.here;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Feature {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("weight")
    @Expose
    private List<String> weight = new ArrayList<String>();

    /**
     * @return The value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return The weight
     */
    public List<String> getWeight() {
        return weight;
    }

    /**
     * @param weight The weight
     */
    public void setWeight(List<String> weight) {
        this.weight = weight;
    }

}
