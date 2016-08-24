package com.oceanwing.at.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oceanwing.at.routing.google.DestinationsRequest;
import com.oceanwing.at.routing.here.RouteFeature;
import com.oceanwing.at.routing.here.RoutingMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RoutingConfig implements Parcelable {

    public static final String HERE_TRANSPORT_CAR = "Car";
    public static final String HERE_TRANSPORT_PEDESTRIAN = "Pedestrian";

    public static final String HERE_MODE_FAST = "Fast";
    public static final String HERE_MODE_SHOR = "Shortest";

    public static final String GOOGLE_TRANSPORT_DRIVING = "Driving";
    public static final String GOOGLE_TRANSPORT_WALKING = "Walking";

    public static final String GOOGLE_MODE_BEST_GUESS = "Best_Guess";
    public static final String GOOGLE_MODE_PESSIMISTIC = "Pessimistic";
    public static final String GOOGLE_MODE_OPTIMISTIC = "Optimistic";

    public static final API DEFAULT_API = API.HERE;
    public static final String DEFAULT_TRANSPORT = HERE_TRANSPORT_CAR;
    public static final String DEFAULT_MODE = HERE_MODE_FAST;

    @SerializedName("waypoints")
    @Expose
    private List<Waypoint> mWaypoints = new ArrayList<>();

    @SerializedName("api")
    @Expose
    private API mAPI = DEFAULT_API;

    @SerializedName("transport")
    @Expose
    private String mTransport = DEFAULT_TRANSPORT;

    @SerializedName("mode")
    @Expose
    private String mMode = DEFAULT_MODE;

    @SerializedName("avoid_tolls")
    @Expose
    private boolean mAvoidTolls = false;

    @SerializedName("avoid_ferries")
    @Expose
    private boolean mAvoidFerries = false;

    @SerializedName("avoid_highways")
    @Expose
    private boolean mAvoidHighways = false;

    @SerializedName("traffic")
    @Expose
    private boolean mTraffic = true;

    protected RoutingConfig(Parcel in) {
        mWaypoints = in.createTypedArrayList(Waypoint.CREATOR);
        mTransport = in.readString();
        mMode = in.readString();
        mAvoidTolls = in.readByte() != 0;
        mAvoidFerries = in.readByte() != 0;
        mAvoidHighways = in.readByte() != 0;
        mTraffic = in.readByte() != 0;
    }

    public static final Creator<RoutingConfig> CREATOR = new Creator<RoutingConfig>() {
        @Override
        public RoutingConfig createFromParcel(Parcel in) {
            return new RoutingConfig(in);
        }

        @Override
        public RoutingConfig[] newArray(int size) {
            return new RoutingConfig[size];
        }
    };

    public RoutingConfig() {
    }

    public RoutingConfig(List<Waypoint> waypoints) {
        mWaypoints = waypoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(mWaypoints);
        parcel.writeString(mTransport);
        parcel.writeString(mMode);
        parcel.writeByte((byte) (mAvoidTolls ? 1 : 0));
        parcel.writeByte((byte) (mAvoidFerries ? 1 : 0));
        parcel.writeByte((byte) (mAvoidHighways ? 1 : 0));
        parcel.writeByte((byte) (mTraffic ? 1 : 0));
    }

    public List<Waypoint> getWaypoints() {
        return mWaypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        mWaypoints = waypoints;
    }

    public API getAPI() {
        return mAPI;
    }

    public void setAPI(API API) {
        mAPI = API;
    }

    public String getTransport() {
        return mTransport;
    }

    public void setTransport(String transport) {
        mTransport = transport;
    }

    public String getMode() {
        return mMode;
    }

    public void setMode(String mode) {
        mMode = mode;
    }

    public boolean isAvoidTolls() {
        return mAvoidTolls;
    }

    public void setAvoidTolls(boolean avoidTolls) {
        mAvoidTolls = avoidTolls;
    }

    public boolean isAvoidFerries() {
        return mAvoidFerries;
    }

    public void setAvoidFerries(boolean avoidFerries) {
        mAvoidFerries = avoidFerries;
    }

    public boolean isAvoidHighways() {
        return mAvoidHighways;
    }

    public void setAvoidHighways(boolean avoidHighways) {
        mAvoidHighways = avoidHighways;
    }

    public boolean isTraffic() {
        return mTraffic;
    }

    public void setTraffic(boolean traffic) {
        mTraffic = traffic;
    }

    public enum API {
        GOOGLE("Google"),
        HERE("HERE");

        private String value;

        API(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutingConfig that = (RoutingConfig) o;
        return mAvoidTolls == that.mAvoidTolls &&
                mAvoidFerries == that.mAvoidFerries &&
                mAvoidHighways == that.mAvoidHighways &&
                mTraffic == that.mTraffic &&
                Objects.equals(mWaypoints, that.mWaypoints) &&
                mAPI == that.mAPI &&
                Objects.equals(mTransport, that.mTransport) &&
                Objects.equals(mMode, that.mMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mWaypoints, mAPI, mTransport, mMode, mAvoidTolls, mAvoidFerries, mAvoidHighways, mTraffic);
    }

    public RoutingMode parseHereRoutingMode() {
        RoutingMode.TransportMode mode = RoutingMode.TransportMode.CAR;
        switch (mTransport) {
            case HERE_TRANSPORT_PEDESTRIAN:
                mode = RoutingMode.TransportMode.PEDESTRIAN;
                break;
        }
        RoutingMode.Type type = RoutingMode.Type.FASTEST;
        switch (mMode) {
            case HERE_MODE_SHOR:
                type = RoutingMode.Type.SHORTEST;
                break;
        }
        RoutingMode.TrafficMode trafficMode = mTraffic ? RoutingMode.TrafficMode.ENABLED : RoutingMode.TrafficMode.DISABLED;
        RoutingMode routingMode = new RoutingMode(type, mode, trafficMode);
        List<RouteFeature> routeFeatures = new ArrayList<>();
        if (mAvoidTolls) {
            routeFeatures.add(new RouteFeature(RouteFeature.Type.TOLL_ROAD, RouteFeature.Weight.STRICT_EXCLUDE));
        }
        if (mAvoidFerries) {
            routeFeatures.add(new RouteFeature(RouteFeature.Type.RAIL_FERRY, RouteFeature.Weight.STRICT_EXCLUDE));
            routeFeatures.add(new RouteFeature(RouteFeature.Type.BOAT_FERRY, RouteFeature.Weight.STRICT_EXCLUDE));
        }
        if (mAvoidHighways) {
            routeFeatures.add(new RouteFeature(RouteFeature.Type.MOTORWAY, RouteFeature.Weight.STRICT_EXCLUDE));
        }
        routingMode.setFeatures(routeFeatures);
        return routingMode;
    }

    public Set<DestinationsRequest.Avoid> parseGoogleAvoids() {
        Set<DestinationsRequest.Avoid> avoids = new HashSet<>();
        if (mAvoidTolls) {
            avoids.add(DestinationsRequest.Avoid.TOLLS);
        }
        if (mAvoidFerries) {
            avoids.add(DestinationsRequest.Avoid.FERRIES);
        }
        if (mAvoidHighways) {
            avoids.add(DestinationsRequest.Avoid.HIGHWAYS);
        }
        return avoids;
    }

    public DestinationsRequest.Mode parseGoogleMode() {
        DestinationsRequest.Mode mode = DestinationsRequest.Mode.DRIVING;
        switch (mTransport) {
            case GOOGLE_TRANSPORT_WALKING:
                mode = DestinationsRequest.Mode.WALKING;
                break;
        }
        return mode;
    }

    public DestinationsRequest.TrafficModel parseGoogleTrafficModel() {
        DestinationsRequest.TrafficModel mode = DestinationsRequest.TrafficModel.BEST_GUESS;
        switch (mMode) {
            case GOOGLE_MODE_OPTIMISTIC:
                mode = DestinationsRequest.TrafficModel.OPTIMISTIC;
                break;
            case GOOGLE_MODE_PESSIMISTIC:
                mode = DestinationsRequest.TrafficModel.PESSIMISTIC;
                break;
        }
        return mode;
    }
}
