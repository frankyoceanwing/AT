package com.oceanwing.at.routing.here;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class RoutingMode {

    private Type mType;
    private TransportMode mTransportMode;
    private TrafficMode mTrafficMode;
    private List<RouteFeature> mFeatures;

    public RoutingMode() {
        mType = Type.FASTEST;
        mTransportMode = TransportMode.CAR;
        mTrafficMode = TrafficMode.DEFAULT;
        mFeatures = new ArrayList<>();
    }

    public RoutingMode(@NonNull Type type, @NonNull TransportMode transportMode, @NonNull TrafficMode trafficMode) {
        mType = type;
        mTransportMode = transportMode;
        mTrafficMode = trafficMode;
        mFeatures = new ArrayList<>();
    }

    public Type getType() {
        return mType;
    }

    public void setType(@NonNull Type type) {
        mType = type;
    }

    public TransportMode getTransportMode() {
        return mTransportMode;
    }

    public void setTransportMode(@NonNull TransportMode transportMode) {
        mTransportMode = transportMode;
    }

    public TrafficMode getTrafficMode() {
        return mTrafficMode;
    }

    public void setTrafficMode(@NonNull TrafficMode trafficMode) {
        mTrafficMode = trafficMode;
    }

    public List<RouteFeature> getFeatures() {
        return mFeatures;
    }

    public void setFeatures(@NonNull List<RouteFeature> features) {
        mFeatures = features;
    }

    @Override
    public String toString() {
        return String.format("&mode=%s", values());
    }

    public String values() {
        return String.format("%s;%s;traffic:%s;%s", mType, mTransportMode, mTrafficMode,
                StringUtils.join(mFeatures.toArray(), ","));
    }

    public enum Type {
        FASTEST,
        SHORTEST;

        @Override
        public String toString() {
            return super.name().toLowerCase();
        }
    }

    public enum TransportMode {
        CAR,
        PEDESTRIAN;

        @Override
        public String toString() {
            return super.name().toLowerCase();
        }
    }

    public enum TrafficMode {
        ENABLED,
        DISABLED,
        DEFAULT;

        @Override
        public String toString() {
            return super.name().toLowerCase();
        }
    }


}

