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

/**
 * Created by franky on 16/8/3.
 */
public class HereRouting {

    private static final String TAG = HereRouting.class.getSimpleName();

    private static final String HERE_MAP_APP_ID = "DemoAppId01082013GAL";
    private static final String HERE_MAP_APP_CODE = "AJKnXv84fjrb0KIHawS0Tg";

    public List<LatLng> matchRoute(List<LatLng> poits) {
        List<LatLng> matchedPoints = new ArrayList<>();
        try {
            String url = Uri.parse("http://rme.cit.api.here.com/2/matchroute.json")
                    .buildUpon()
                    .appendQueryParameter("app_id", HERE_MAP_APP_ID)
                    .appendQueryParameter("app_code", HERE_MAP_APP_CODE)
                    .appendQueryParameter("routemode", "car")
                    .build().toString();

            String jsonString = HTTPUtils.postString(url, "*", toGPX(poits));
            Log.i(TAG, "received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            matchedPoints = parseTracePoints(jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "failed to calculate route", ioe);
        }
        return matchedPoints;
    }

    private List<LatLng> parseTracePoints(JSONObject jsonBody) throws IOException, JSONException {
        JSONArray pointsJsonArray = jsonBody.getJSONArray("TracePoints");
        if (pointsJsonArray.length() == 0) {
            throw new InvalidObjectException("NONE");
        }
        List<LatLng> points = new ArrayList<>();
        for (int i = 0; i < pointsJsonArray.length(); i++) {
            points.add(parseMatchedPoint(pointsJsonArray.getJSONObject(i)));
        }
        return points;
    }

    private LatLng parseMatchedPoint(JSONObject obj) throws JSONException {
        return new LatLng(obj.getDouble("latMatched"), obj.getDouble("lonMatched"));
    }

    private byte[] toGPX(List<LatLng> points) {
        StringBuilder sb = new StringBuilder();
        sb.append("<gpx version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.topografix.com/GPX/1/0\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd\">");
        sb.append("<trk>");
        sb.append("<trkseg>");
        for (LatLng p : points) {
            sb.append("<trkpt lat=\"" + p.getLat() + "\" lon=\"" + p.getLng() + "\"/>");
        }
        sb.append("</trkseg>");
        sb.append("</trk>");
        sb.append("</gpx>");
        return sb.toString().getBytes();
    }
}
