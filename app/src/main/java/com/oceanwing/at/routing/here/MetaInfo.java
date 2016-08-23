package com.oceanwing.at.routing.here;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaInfo {

    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("mapVersion")
    @Expose
    private String mapVersion;
    @SerializedName("moduleVersion")
    @Expose
    private String moduleVersion;
    @SerializedName("interfaceVersion")
    @Expose
    private String interfaceVersion;

    /**
     * @return The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return The mapVersion
     */
    public String getMapVersion() {
        return mapVersion;
    }

    /**
     * @param mapVersion The mapVersion
     */
    public void setMapVersion(String mapVersion) {
        this.mapVersion = mapVersion;
    }

    /**
     * @return The moduleVersion
     */
    public String getModuleVersion() {
        return moduleVersion;
    }

    /**
     * @param moduleVersion The moduleVersion
     */
    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }

    /**
     * @return The interfaceVersion
     */
    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    /**
     * @param interfaceVersion The interfaceVersion
     */
    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

}
