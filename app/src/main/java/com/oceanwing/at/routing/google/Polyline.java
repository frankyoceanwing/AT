package com.oceanwing.at.routing.google;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Polyline {

    @SerializedName("points")
    @Expose
    private String points;

    /**
     * @return The points
     */
    public String getPoints() {
        return points;
    }

    /**
     * @param points The points
     */
    public void setPoints(String points) {
        this.points = points;
    }

    // copy from https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java
    public List<com.oceanwing.at.model.Position> decode() {
        int len = points.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<com.oceanwing.at.model.Position> positions = new ArrayList<>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = points.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = points.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            positions.add(new com.oceanwing.at.model.Position(lat * 1e-5, lng * 1e-5));
        }

        return positions;
    }

}
