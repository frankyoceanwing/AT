package com.oceanwing.at.routing.here;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RoutingErrorResponse {

    @SerializedName("_type")
    @Expose
    private String errorType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("subtype")
    @Expose
    private String subtype;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("additionalData")
    @Expose
    private List<AdditionalData> additionalData = new ArrayList<AdditionalData>();
    @SerializedName("metaInfo")
    @Expose
    private MetaInfo metaInfo;

    /**
     * @return The errorType
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * @param errorType The errorType
     */
    public void setErrorType(String errorType) {
        this.errorType = errorType;
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
     * @return The subtype
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * @param subtype The subtype
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     * @return The details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details The details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return The additionalData
     */
    public List<AdditionalData> getAdditionalData() {
        return additionalData;
    }

    /**
     * @param additionalData The additionalData
     */
    public void setAdditionalData(List<AdditionalData> additionalData) {
        this.additionalData = additionalData;
    }

    /**
     * @return The metaInfo
     */
    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    /**
     * @param metaInfo The metaInfo
     */
    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

}
