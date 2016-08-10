package com.oceanwing.at.routing;

import android.net.Uri;
import android.util.Log;

import com.oceanwing.at.model.LatLng;
import com.oceanwing.at.util.HTTPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

public class GoogleRouting {

    private static final String TAG = GoogleRouting.class.getSimpleName();

    private static final String GOOGLE_MAP_API_KEY = "AIzaSyBc6iEK9pI1kwJAyPvCdS0tpeQZyJU8igQ";
    private static final int MAX_WAY_POINTS = 100;

    public List<LatLng> calcRoute(double originLat, double originLng, double destLat, double destLng) {
        List<LatLng> points = new ArrayList<>();
        try {
            String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                    .buildUpon()
                    .appendQueryParameter("key", GOOGLE_MAP_API_KEY)
                    .appendQueryParameter("origin", String.format("%.6f,%.6f", originLat, originLng))
                    .appendQueryParameter("destination", String.format("%.6f,%.6f", destLat, destLng))
                    .appendQueryParameter("units", "metric")
                    .appendQueryParameter("mode", "driving")
                    .build().toString();
            String jsonString = HTTPUtils.getString(url);
            Log.i(TAG, "received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            //points = parseAllSteps(jsonBody);
            points = parseAllPoints(jsonBody);
            points = optimizePoints(points);
        } catch (JSONException je) {
            Log.e(TAG, "failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "failed to calculate route", ioe);
        }
        return points;
    }

    private List<LatLng> optimizePoints(List<LatLng> points) {
        if (points.size() <= MAX_WAY_POINTS) {
            return points;
        }
        // TODO 超过 100 个点的时候, 需要限制点的个数
        return points;
    }

    private List<LatLng> parseAllSteps(JSONObject jsonBody) throws IOException, JSONException {
        JSONArray routesJsonArray = jsonBody.getJSONArray("routes");
        if (routesJsonArray.length() == 0) {
            throw new InvalidObjectException("ZERO_RESULTS");
        }
        JSONObject routeJsonObject = routesJsonArray.getJSONObject(0);
        JSONArray legsJsonArray = routeJsonObject.getJSONArray("legs");
        JSONObject legJsonObject = legsJsonArray.getJSONObject(0);
        JSONArray stepJsonArray = legJsonObject.getJSONArray("steps");
        List<LatLng> points = new ArrayList<>();
        for (int i = 0; i < stepJsonArray.length(); i++) {
            points.add(parseStepStartLocation(stepJsonArray.getJSONObject(i)));
            if (i == stepJsonArray.length() - 1) { // 最后一段
                points.add(parseStepEndLocation(stepJsonArray.getJSONObject(i)));
            }
        }
        return points;
    }

    private LatLng parseStepStartLocation(JSONObject step) throws JSONException {
        JSONObject locationObject = step.getJSONObject("start_location");
        return new LatLng(locationObject.getDouble("lat"), locationObject.getDouble("lng"));
    }

    private LatLng parseStepEndLocation(JSONObject step) throws JSONException {
        JSONObject locationObject = step.getJSONObject("end_location");
        return new LatLng(locationObject.getDouble("lat"), locationObject.getDouble("lng"));
    }

    private List<LatLng> parseAllPoints(JSONObject jsonBody) throws IOException, JSONException {
        JSONArray routesJsonArray = jsonBody.getJSONArray("routes");
        if (routesJsonArray.length() == 0) {
            throw new InvalidObjectException("ZERO_RESULTS");
        }
        JSONObject routeJsonObject = routesJsonArray.getJSONObject(0);
        JSONArray legsJsonArray = routeJsonObject.getJSONArray("legs");
        JSONObject legJsonObject = legsJsonArray.getJSONObject(0);
        JSONArray stepJsonArray = legJsonObject.getJSONArray("steps");
        List<LatLng> points = new ArrayList<>();
        for (int i = 0; i < stepJsonArray.length(); i++) {
            List<LatLng> stepPoints = parseStep(stepJsonArray.getJSONObject(i));
            if (i == stepJsonArray.length() - 1) { // 最后一段
                points.addAll(stepPoints);
            } else {
                for (int j = 0; j < stepPoints.size() - 1; j++) { // 不要最后1个点, 因为下一段的起点就是当前段的终点
                    points.add(stepPoints.get(j));
                }
            }
        }
        return points;
    }

    private List<LatLng> parseStep(JSONObject step) throws JSONException {
        JSONObject polylineJsonObject = step.getJSONObject("polyline");
        String encodedPolyline = polylineJsonObject.getString("points");
        return decodePolyline(encodedPolyline);
    }

    // copy from https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java
    private List<LatLng> decodePolyline(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<LatLng> path = new ArrayList<>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }
}
