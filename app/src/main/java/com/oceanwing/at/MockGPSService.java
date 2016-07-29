package com.oceanwing.at;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MockGPSService extends IntentService {

    private static final String TAG = "MockGPSService";

    // Defines and instantiates an object for handling status updates.
    private BroadcastNotifier mBroadcaster = new BroadcastNotifier(this);

    private static final int ONGOING_NOTIFICATION_ID = 1;

    public static final String ACTION_START = "com.oceanwing.at.action.START";
    public static final String ACTION_STOP = "com.oceanwing.at.action.STOP";

    public static final String API = "com.oceanwing.at.extra.api";
    public static final String RUN = "com.oceanwing.at.extra.run";
    public static final String SPEED = "com.oceanwing.at.extra.speed";
    public static final String ORIGIN_LAT = "com.oceanwing.at.extra.origin.lat";
    public static final String ORIGIN_LNG = "com.oceanwing.at.extra.origin.lng";
    public static final String DEST_LAT = "com.oceanwing.at.extra.dest.lat";
    public static final String DEST_LNG = "com.oceanwing.at.extra.dest.lng";

    private static final String HERE_MAP_APP_ID = "HERE_MAP_APP_ID";
    private static final String HERE_MAP_APP_CODE = "HERE_MAP_APP_CODE";
    private static final String GOOGLE_MAP_API_KEY = "GOOGLE_MAP_API_KEY";

    private static String mMockProviderName = LocationManager.GPS_PROVIDER; // 模拟GPS
    private static LocationManager mLocationManager;

    private double mOriginLat, mOriginLng, mDestLat, mDestLng;
    private float mSpeed;

    private Location mCurrentLocation = null;
    private Location mLastLocation = null;

    public MockGPSService() {
        super("MockGPSService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        foreground();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enableTestProvider();
    }

    private void foreground() {
        Log.e(TAG, "foreground");
        Intent notificationIntent = new Intent(this, MockGPSActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setTicker(getString(R.string.app_name))
                .setContentText("Mocking")
                .setSmallIcon(R.drawable.ic_launcher_24)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_48))
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    private void enableTestProvider() {
        mLocationManager.addTestProvider(mMockProviderName,
                "requiresNetwork" == "", "requiresSatellite" == "requiresSatellite",
                "requiresCell" == "", "hasMonetaryCost" == "",
                "supportsAltitude" == "", "supportsSpeed" == "supportsSpeed",
                "supportsBearing" == "supportsBearing",
                Criteria.POWER_LOW,
                Criteria.ACCURACY_FINE);
        mLocationManager.setTestProviderEnabled(mMockProviderName, true);
    }

    private static void disableTestProvider() {
        mLocationManager.removeTestProvider(mMockProviderName);
    }

    public static void start(Context context, String api, String run, double originLat, double originLng, double destLat, double destLng, float speed) {
        Intent intent = new Intent(context, MockGPSService.class);
        intent.setAction(ACTION_START);
        intent.putExtra(API, api);
        intent.putExtra(RUN, run);
        intent.putExtra(ORIGIN_LAT, originLat);
        intent.putExtra(ORIGIN_LNG, originLng);
        intent.putExtra(DEST_LAT, destLat);
        intent.putExtra(DEST_LNG, destLng);
        intent.putExtra(SPEED, speed);
        context.startService(intent);
    }

    public static void stop(Context context) {
        disableTestProvider();
        Intent intent = new Intent(context, MockGPSService.class);
        intent.setAction(ACTION_STOP);
        context.stopService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                final String api = intent.getStringExtra(API);
                final String run = intent.getStringExtra(RUN);
                final float speed = kmh2mps(intent.getFloatExtra(SPEED, 0F));
                final double originLat = intent.getDoubleExtra(ORIGIN_LAT, 0D);
                final double originLng = intent.getDoubleExtra(ORIGIN_LNG, 0D);
                final double destLat = intent.getDoubleExtra(DEST_LAT, 0D);
                final double destLng = intent.getDoubleExtra(DEST_LNG, 0D);
                handleActionStart(api, run, originLat, originLng, destLat, destLng, speed);
            }
        }
    }

    private void handleActionStart(String api, String run, double originLat, double originLng, double destLat, double destLng, float speed) {
        mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_STARTED, "started");
        Log.i(TAG, String.format("API[%s] run %s (%.6f, %.6f) -> (%.6f, %.6f) at %.2f MPS", api, run, originLat, originLng, destLat, destLng, speed));

        mOriginLat = originLat;
        mOriginLng = originLng;
        mDestLat = destLat;
        mDestLng = destLng;
        mSpeed = speed;

        List<LatLng> points = calcRoute(api, originLat, originLng, destLat, destLng);
        points = optimizePoints(points, speed);
        LatLng current, next = null;
        String log;
        float bearing;
        double distance;
        try {
            do {
                for (int i = 0; i < points.size() - 1; i++) {
                    current = points.get(i);
                    next = points.get(i + 1);
                    bearing = current.bearingTo(next);
                    distance = current.distanceTo(next);

                    log = String.format("[%d/%d] (%.6f, %.6f) -> (%.6f, %.6f), bearing=%.0f, distance=%.3fkm",
                            (i + 1), points.size(),
                            current.getLat(), current.getLng(),
                            next.getLat(), next.getLng(),
                            bearing, distance, speed);
                    Log.i(TAG, log);

                    setLocation(current.getLat(), current.getLng(), bearing, speed);

                    mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_MOCKING, log);

                    //long t = (long) (distance / speed * 1000 * 1000); // m -> km, ms ->s
                    //Log.i(TAG, String.format("wait=%dms", t));
                    Thread.sleep(1000);
                }
                if (next != null) {
                    log = String.format("destination(%.6f, %.6f)", next.getLat(), next.getLng());
                    Log.i(TAG, log);
                    setLocation(next.getLat(), next.getLng(), 0, 0);
                    mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_MOCKING, log);
                }
            } while (getString(R.string.run_forever).equals(run));
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "failed to set location", e);
        } catch (InterruptedException e) {
            Log.e(TAG, "failed to sleep", e);
        } finally {
            mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_COMPLETE, "complete");
        }
    }

    private float kmh2mps(float speed) {
        return speed * 1000 / 3600; // km/h -> m/s
    }

    private List<LatLng> calcRoute(String api, double originLat, double originLng, double destLat, double destLng) {
        if (getString(R.string.api_here).equals(api)) {
            return calcRouteByHere(originLat, originLng, destLat, destLng);
        }
        return calcRouteByGoogle(originLat, originLng, destLat, destLng);
    }


    private List<LatLng> calcRouteByHere(double originLat, double originLng, double destLat, double destLng) {
        List<LatLng> points = new ArrayList<>();
        try {
            String url = Uri.parse("https://route.cit.api.here.com/routing/7.2/calculateroute.json")
                    .buildUpon()
                    .appendQueryParameter("app_id", readMetaDataFromApplication(HERE_MAP_APP_ID))
                    .appendQueryParameter("app_code", readMetaDataFromApplication(HERE_MAP_APP_CODE))
                    .appendQueryParameter("waypoint0", String.format("geo!%.6f,%.6f", originLat, originLng))
                    .appendQueryParameter("waypoint1", String.format("geo!%.6f,%.6f", destLat, destLng))
                    .appendQueryParameter("routeattributes", "sh")
                    .appendQueryParameter("mode", "fastest;car;traffic:disabled")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            points = parseHereResponse(jsonBody);
        } catch (PackageManager.NameNotFoundException e) {
            mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_ERROR, "API key not found: " + e.getMessage());
            Log.e(TAG, "failed to calculate route", e);
        } catch (JSONException je) {
            mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_ERROR, "failed to parse JSON");
            Log.e(TAG, "failed to parse JSON", je);
        } catch (IOException ioe) {
            mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_ERROR, "failed to calculate route");
            Log.e(TAG, "failed to calculate route", ioe);
        }
        return points;
    }

    private List<LatLng> calcRouteByGoogle(double originLat, double originLng, double destLat, double destLng) {
        List<LatLng> points = new ArrayList<>();
        try {
            String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                    .buildUpon()
                    .appendQueryParameter("key", readMetaDataFromApplication(GOOGLE_MAP_API_KEY))
                    .appendQueryParameter("origin", String.format("%.6f,%.6f", originLat, originLng))
                    .appendQueryParameter("destination", String.format("%.6f,%.6f", destLat, destLng))
                    .appendQueryParameter("units", "metric")
                    .appendQueryParameter("mode", "driving")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            points = parseGoogleResponse(jsonBody);
        } catch (PackageManager.NameNotFoundException e) {
            mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_ERROR, "API key not found: " + e.getMessage());
            Log.e(TAG, "failed to calculate route", e);
        } catch (JSONException je) {
            mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_ERROR, "failed to parse JSON");
            Log.e(TAG, "failed to parse JSON", je);
        } catch (IOException ioe) {
            mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_ERROR, "failed to calculate route");
            Log.e(TAG, "failed to calculate route", ioe);
        }
        return points;
    }

    private List<LatLng> parseHereResponse(JSONObject jsonBody) throws IOException, JSONException {
        mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_PARSING, "parsing");

        JSONObject respJsonObject = jsonBody.getJSONObject("response");
        JSONArray routesJsonArray = respJsonObject.getJSONArray("route");
        JSONObject routeJsonObject = routesJsonArray.getJSONObject(0);
        JSONArray shapeJsonArray = routeJsonObject.getJSONArray("shape");
        List<LatLng> points = new ArrayList<>();
        for (int i = 0; i < shapeJsonArray.length(); i++) {
            points.add(parseShape(shapeJsonArray.getString(i)));
        }
        return points;
    }

    private LatLng parseShape(String shape) {
        String[] s = TextUtils.split(shape, ",");
        return new LatLng(Double.valueOf(s[0]).doubleValue(), Double.valueOf(s[1]).doubleValue());
    }

    private List<LatLng> parseGoogleResponse(JSONObject jsonBody) throws IOException, JSONException {
        mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_PARSING, "parsing");

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

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_CONNECTING, "connecting");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private void setLocation(double latitude, double longitude, float bearing, float speed) {
        Location loc = new Location(mMockProviderName);
        loc.setAccuracy(Criteria.ACCURACY_FINE);
        loc.setTime(System.currentTimeMillis());
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);
        loc.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        loc.setBearing(bearing);

        mCurrentLocation = loc;
        if (mLastLocation != null) {
            LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            LatLng last = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            speed = (float) ((current.distanceTo(last) * 1000) / ((mCurrentLocation.getTime() - mLastLocation.getTime()) / 1000));
        }
        loc.setSpeed(speed);

        mLocationManager.setTestProviderLocation(mMockProviderName, loc);

        mLastLocation = loc;

        Log.i(TAG, String.format("lat=%.6f, lng=%.6f, bearing=%.0f, speed=%.2fm/s", latitude, longitude, loc.getBearing(), speed));
    }

    private List<LatLng> optimizePoints(List<LatLng> points, float speed) {
        double step = (1 * speed) / 1000; // 单位距离(1s 走的距离 km)
        List<LatLng> newPoints = new ArrayList<>();
        LatLng current, next = null;
        double distance;
        float bearing;
        for (int i = 0; i < points.size() - 1; i++) {
            current = points.get(i);
            newPoints.add(current);

            next = points.get(i + 1);
            distance = current.distanceTo(next);

            // 如果当前点和下一个点的距离 大于 单位距离(1s 走的距离)
            // 则沿着前进方向, 分解成 n 个单位距离以内的点
            if (distance > step) {
                bearing = current.bearingTo(next);
                int n = (int) (distance / step);
                LatLng start = current;
                for (int j = 0; j < n; j++) {
                    LatLng tmp = start.destinationPoint(step, bearing);
                    Log.i(TAG, String.format("destinationPoint(%d-%d): lat=%.6f, lng=%.6f", i, j, tmp.getLat(), tmp.getLng()));
                    newPoints.add(tmp);
                    start = tmp;
                }
            }
        }
        if (next != null) {
            newPoints.add(next);
        }
        return newPoints;
    }

    private String readMetaDataFromApplication(String name) throws PackageManager.NameNotFoundException {
        ApplicationInfo appInfo = this.getPackageManager()
                .getApplicationInfo(getPackageName(),
                        PackageManager.GET_META_DATA);
        return appInfo.metaData.getString(name);
    }
}
