package com.oceanwing.at.routing.here;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class End {

    @SerializedName("linkId")
    @Expose
    private String linkId;
    @SerializedName("mappedPosition")
    @Expose
    private Position mappedPosition;
    @SerializedName("originalPosition")
    @Expose
    private Position originalPosition;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("spot")
    @Expose
    private Double spot;
    @SerializedName("sideOfStreet")
    @Expose
    private String sideOfStreet;
    @SerializedName("mappedRoadName")
    @Expose
    private String mappedRoadName;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("shapeIndex")
    @Expose
    private Integer shapeIndex;

    /**
     * @return The linkId
     */
    public String getLinkId() {
        return linkId;
    }

    /**
     * @param linkId The linkId
     */
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    /**
     * @return The mappedPosition
     */
    public Position getMappedPosition() {
        return mappedPosition;
    }

    /**
     * @param mappedPosition The mappedPosition
     */
    public void setMappedPosition(Position mappedPosition) {
        this.mappedPosition = mappedPosition;
    }

    /**
     * @return The originalPosition
     */
    public Position getOriginalPosition() {
        return originalPosition;
    }

    /**
     * @param originalPosition The originalPosition
     */
    public void setOriginalPosition(Position originalPosition) {
        this.originalPosition = originalPosition;
    }

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
     * @return The spot
     */
    public Double getSpot() {
        return spot;
    }

    /**
     * @param spot The spot
     */
    public void setSpot(Double spot) {
        this.spot = spot;
    }

    /**
     * @return The sideOfStreet
     */
    public String getSideOfStreet() {
        return sideOfStreet;
    }

    /**
     * @param sideOfStreet The sideOfStreet
     */
    public void setSideOfStreet(String sideOfStreet) {
        this.sideOfStreet = sideOfStreet;
    }

    /**
     * @return The mappedRoadName
     */
    public String getMappedRoadName() {
        return mappedRoadName;
    }

    /**
     * @param mappedRoadName The mappedRoadName
     */
    public void setMappedRoadName(String mappedRoadName) {
        this.mappedRoadName = mappedRoadName;
    }

    /**
     * @return The label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label The label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return The shapeIndex
     */
    public Integer getShapeIndex() {
        return shapeIndex;
    }

    /**
     * @param shapeIndex The shapeIndex
     */
    public void setShapeIndex(Integer shapeIndex) {
        this.shapeIndex = shapeIndex;
    }

}
