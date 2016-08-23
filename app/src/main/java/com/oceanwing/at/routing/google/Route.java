package com.oceanwing.at.routing.google;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oceanwing.at.model.Position;

import java.util.ArrayList;
import java.util.List;

public class Route {

    @SerializedName("bounds")
    @Expose
    private Bounds bounds;
    @SerializedName("copyrights")
    @Expose
    private String copyrights;
    @SerializedName("legs")
    @Expose
    private List<Leg> legs = new ArrayList<Leg>();
    @SerializedName("overview_polyline")
    @Expose
    private OverviewPolyline overviewPolyline;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("warnings")
    @Expose
    private List<Object> warnings = new ArrayList<Object>();
    @SerializedName("waypoint_order")
    @Expose
    private List<Integer> waypointOrder = new ArrayList<Integer>();

    /**
     * @return The bounds
     */
    public Bounds getBounds() {
        return bounds;
    }

    /**
     * @param bounds The bounds
     */
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    /**
     * @return The copyrights
     */
    public String getCopyrights() {
        return copyrights;
    }

    /**
     * @param copyrights The copyrights
     */
    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    /**
     * @return The legs
     */
    public List<Leg> getLegs() {
        return legs;
    }

    /**
     * @param legs The legs
     */
    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    /**
     * @return The overviewPolyline
     */
    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    /**
     * @param overviewPolyline The overview_polyline
     */
    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    /**
     * @return The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return The warnings
     */
    public List<Object> getWarnings() {
        return warnings;
    }

    /**
     * @param warnings The warnings
     */
    public void setWarnings(List<Object> warnings) {
        this.warnings = warnings;
    }

    /**
     * @return The waypointOrder
     */
    public List<Integer> getWaypointOrder() {
        return waypointOrder;
    }

    /**
     * @param waypointOrder The waypoint_order
     */
    public void setWaypointOrder(List<Integer> waypointOrder) {
        this.waypointOrder = waypointOrder;
    }

    public List<com.oceanwing.at.model.Position> parsePosition() {
        List<com.oceanwing.at.model.Position> positions = new ArrayList<>();
        for (int i = 0; i < legs.size(); i++) {
            List<Step> steps = legs.get(i).getSteps();


            for (int j = 0; j < steps.size(); j++) {
                List<Position> ps = steps.get(j).getPolyline().decode();
                if (i == legs.size() - 1 && j == steps.size() - 1) {// 最后一段
                    positions.addAll(ps);
                } else {
                    positions.addAll(ps.subList(0, ps.size() - 2));// 不要最后1个点, 因为下一段的起点就是当前段的终点
                }
            }
        }
        return positions;
    }

}
