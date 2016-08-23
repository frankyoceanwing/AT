package com.oceanwing.at.routing.here;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class RoutingRequest {

    private String mAppID;
    private String mAppCode;
    private Waypoints mWaypoints;
    private RoutingMode mMode;
    private RouteAttributes mRouteAttributes;
    private Representation mRepresentation;

    private RoutingRequest() {
    }

    public RoutingRequest(@NonNull String appID, @NonNull String appCode,
                          @NonNull Waypoints waypoints, @NonNull RoutingMode mode) {
        mAppID = appID;
        mAppCode = appCode;
        mWaypoints = waypoints;
        mMode = mode;
    }

    public String getAppID() {
        return mAppID;
    }

    public String getAppCode() {
        return mAppCode;
    }

    public Waypoints getWaypoints() {
        return mWaypoints;
    }

    public RoutingMode getMode() {
        return mMode;
    }

    public RouteAttributes getRouteAttributes() {
        return mRouteAttributes;
    }

    public Representation getRepresentation() {
        return mRepresentation;
    }

    public void setRouteAttributes(RouteAttributes routeAttributes) {
        mRouteAttributes = routeAttributes;
    }

    public void setRepresentation(Representation representation) {
        mRepresentation = representation;
    }

    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();
        query.append(String.format("?app_id=%s", mAppID));
        query.append(String.format("&app_code=%s", mAppCode));
        query.append(mWaypoints.toString());
        query.append(mMode.toString());
        if (mRouteAttributes != null) {
            query.append(mRouteAttributes.toString());
        }
        if (mRepresentation != null) {
            query.append(String.format("&representation=%s", mRepresentation.toString()));
        }
        return query.toString();
    }

    public Map<String, String> toMap() {
        Map<String, String> query = new HashMap<>();
        query.put("app_id", mAppID);
        query.put("app_code", mAppCode);
        query.putAll(mWaypoints.toMap());
        query.put("mode", mMode.values());
        if (mRouteAttributes != null) {
            query.put("routeAttributes", mRouteAttributes.values());
        }
        if (mRepresentation != null) {
            query.put("representation", mRepresentation.value());
        }
        return query;
    }

    public enum RouteAttribute {
        SHAPE("sh");

        private String value;

        RouteAttribute(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Representation {
        OVERVIEW("overview");

        private String value;

        Representation(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
