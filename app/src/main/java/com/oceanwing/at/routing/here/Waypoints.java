package com.oceanwing.at.routing.here;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Waypoints {
    private List<Position> mPositions;

    private Waypoints() {
    }

    public Waypoints(@NonNull Position origin, @NonNull Position dest) {
        mPositions = new ArrayList<>();
        mPositions.add(origin);
        mPositions.add(dest);
    }

    public Waypoints(@NonNull List<Position> positions) {
        if (positions.size() < 2) {
            throw new IllegalArgumentException("require at least two waypoints");
        }
        mPositions = positions;
    }

    public List<Position> getPositions() {
        return mPositions;
    }

    @Override
    public String toString() {
        StringBuilder points = new StringBuilder();
        Position position;
        for (int i = 0; i < mPositions.size(); i++) {
            position = mPositions.get(i);
            points.append(String.format("&waypoint%d=geo!%.6f,%.6f", i, position.getLatitude(), position.getLongitude()));
        }
        return points.toString();
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < mPositions.size(); i++) {
            map.put("waypoint" + i, mPositions.get(i).geoString());
        }
        return map;
    }
}
