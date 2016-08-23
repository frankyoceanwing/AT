package com.oceanwing.at.routing.here;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Leg {

    @SerializedName("start")
    @Expose
    private Start start;
    @SerializedName("end")
    @Expose
    private End end;
    @SerializedName("length")
    @Expose
    private Integer length;
    @SerializedName("travelTime")
    @Expose
    private Integer travelTime;
    @SerializedName("maneuver")
    @Expose
    private List<Maneuver> maneuver = new ArrayList<Maneuver>();

    /**
     * @return The start
     */
    public Start getStart() {
        return start;
    }

    /**
     * @param start The start
     */
    public void setStart(Start start) {
        this.start = start;
    }

    /**
     * @return The end
     */
    public End getEnd() {
        return end;
    }

    /**
     * @param end The end
     */
    public void setEnd(End end) {
        this.end = end;
    }

    /**
     * @return The length
     */
    public Integer getLength() {
        return length;
    }

    /**
     * @param length The length
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * @return The travelTime
     */
    public Integer getTravelTime() {
        return travelTime;
    }

    /**
     * @param travelTime The travelTime
     */
    public void setTravelTime(Integer travelTime) {
        this.travelTime = travelTime;
    }

    /**
     * @return The maneuver
     */
    public List<Maneuver> getManeuver() {
        return maneuver;
    }

    /**
     * @param maneuver The maneuver
     */
    public void setManeuver(List<Maneuver> maneuver) {
        this.maneuver = maneuver;
    }

}
