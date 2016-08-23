package com.oceanwing.at.routing.here;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Maneuver {

    @SerializedName("position")
    @Expose
    private Position position;
    @SerializedName("instruction")
    @Expose
    private String instruction;
    @SerializedName("travelTime")
    @Expose
    private Integer travelTime;
    @SerializedName("length")
    @Expose
    private Integer length;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("_type")
    @Expose
    private String type;

    /**
     * @return The position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position The position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @return The instruction
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * @param instruction The instruction
     */
    public void setInstruction(String instruction) {
        this.instruction = instruction;
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
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The _type
     */
    public void setType(String type) {
        this.type = type;
    }

}
