package com.oceanwing.at.routing.here;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Response {

    @SerializedName("metaInfo")
    @Expose
    private MetaInfo metaInfo;
    @SerializedName("route")
    @Expose
    private List<Route> route = new ArrayList<Route>();
    @SerializedName("language")
    @Expose
    private String language;

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

    /**
     * @return The route
     */
    public List<Route> getRoute() {
        return route;
    }

    /**
     * @param route The route
     */
    public void setRoute(List<Route> route) {
        this.route = route;
    }

    /**
     * @return The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language The language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

}
