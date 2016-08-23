package com.oceanwing.at.routing.here;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Route {

    @SerializedName("waypoint")
    @Expose
    private List<Waypoint> waypoint = new ArrayList<Waypoint>();
    @SerializedName("mode")
    @Expose
    private Mode mode;
    @SerializedName("shape")
    @Expose
    private List<String> shape = new ArrayList<String>();
    @SerializedName("leg")
    @Expose
    private List<Leg> leg = new ArrayList<Leg>();
    @SerializedName("summary")
    @Expose
    private Summary summary;

    /**
     * @return The waypoint
     */
    public List<Waypoint> getWaypoint() {
        return waypoint;
    }

    /**
     * @param waypoint The waypoint
     */
    public void setWaypoint(List<Waypoint> waypoint) {
        this.waypoint = waypoint;
    }

    /**
     * @return The mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * @param mode The mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * @return The shape
     */
    public List<String> getShape() {
        return shape;
    }

    /**
     * @param shape The shape
     */
    public void setShape(List<String> shape) {
        this.shape = shape;
    }

    /**
     * @return The leg
     */
    public List<Leg> getLeg() {
        return leg;
    }

    /**
     * @param leg The leg
     */
    public void setLeg(List<Leg> leg) {
        this.leg = leg;
    }

    /**
     * @return The summary
     */
    public Summary getSummary() {
        return summary;
    }

    /**
     * @param summary The summary
     */
    public void setSummary(Summary summary) {
        this.summary = summary;
    }


    public List<com.oceanwing.at.model.Position> parsePosition() {
        List<com.oceanwing.at.model.Position> positions = new ArrayList<>();
        for (String s : shape) {
            String[] array = StringUtils.split(s, ",");
            positions.add(new com.oceanwing.at.model.Position(
                    Double.valueOf(array[0]).doubleValue(), Double.valueOf(array[1]).doubleValue()));
        }
        return positions;
    }

}
