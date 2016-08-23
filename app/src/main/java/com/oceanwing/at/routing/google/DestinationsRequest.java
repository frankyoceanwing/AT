package com.oceanwing.at.routing.google;


import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DestinationsRequest {
    private String mKey;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Mode mMode;
    private final String mDepartureTime = "now";
    private TrafficModel mTrafficModel;
    private Set<Avoid> mAvoids;
    private List<LatLng> mWaypoints;

    private DestinationsRequest() {
    }

    public DestinationsRequest(@NonNull String key, @NonNull LatLng origin, @NonNull LatLng destination) {
        mKey = key;
        mOrigin = origin;
        mDestination = destination;
        mMode = Mode.DRIVING;
        mTrafficModel = TrafficModel.BEST_GUESS;
        mAvoids = new HashSet<>();
        mWaypoints = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();
        query.append(String.format("?key=%s", mKey));
        query.append(String.format("&origin=%s", mOrigin.toString()));
        query.append(String.format("&offset=%s", mDestination.toString()));
        query.append(String.format("&mode=%s", mMode.toString()));
        query.append(String.format("&departure_time=%s", mDepartureTime));
        query.append(String.format("&traffic_model=%s", mTrafficModel.toString()));
        if (!mAvoids.isEmpty()) {
            query.append(String.format("&avoid=%s", StringUtils.join(mAvoids, "|")));
        }
        if (!mWaypoints.isEmpty()) {
            query.append(String.format("&waypoints=%s", StringUtils.join(mWaypoints, "|")));
        }
        return query.toString();
    }

    public Map<String, String> toMap() {
        Map<String, String> query = new HashMap<>();
        query.put("key", mKey);
        query.put("origin", mOrigin.toString());
        query.put("offset", mDestination.toString());
        query.put("mode", mMode.toString());
        query.put("departure_time", mDepartureTime);
        query.put("traffic_model", mTrafficModel.toString());
        if (!mAvoids.isEmpty()) {
            query.put("avoid", StringUtils.join(mAvoids, "|"));
        }
        if (!mWaypoints.isEmpty()) {
            query.put("waypoints", StringUtils.join(mWaypoints, "|"));
        }
        return query;
    }

    public String getKey() {
        return mKey;
    }

    public LatLng getOrigin() {
        return mOrigin;
    }

    public LatLng getDestination() {
        return mDestination;
    }

    public Mode getMode() {
        return mMode;
    }

    public TrafficModel getTrafficModel() {
        return mTrafficModel;
    }

    public Set<Avoid> getAvoids() {
        return mAvoids;
    }

    public void setMode(@NonNull Mode mode) {
        mMode = mode;
    }

    public void setTrafficModel(@NonNull TrafficModel trafficModel) {
        mTrafficModel = trafficModel;
    }

    public void setAvoids(@NonNull Set<Avoid> avoids) {
        mAvoids = avoids;
    }

    public List<LatLng> getWaypoints() {
        return mWaypoints;
    }

    public void setWaypoints(@NonNull List<LatLng> waypoints) {
        mWaypoints = waypoints;
    }

    public enum Mode {
        DRIVING("driving"),
        WALKING("walking");

        private String value;

        Mode(String value) {
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

    public enum TrafficModel {
        BEST_GUESS("best_guess"),
        PESSIMISTIC("pessimistic"),
        OPTIMISTIC("optimistic");

        private String value;

        TrafficModel(String value) {
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


    public enum Avoid {
        TOLLS("tolls"),
        FERRIES("ferries"),
        HIGHWAYS("highways");

        private String value;

        Avoid(String value) {
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

    public enum Units {
        metric("metric"),
        imperial("imperial");

        private String value;

        Units(String value) {
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

}
