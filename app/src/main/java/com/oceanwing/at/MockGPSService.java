package com.oceanwing.at;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.oceanwing.at.model.LatLng;
import com.oceanwing.at.model.Position;
import com.oceanwing.at.model.RoutingConfig;
import com.oceanwing.at.model.RunnerConfig;
import com.oceanwing.at.model.Task;
import com.oceanwing.at.model.Waypoint;
import com.oceanwing.at.routing.google.DestinationsRequest;
import com.oceanwing.at.routing.google.DestinationsResponse;
import com.oceanwing.at.routing.google.GoogleMapAPI;
import com.oceanwing.at.routing.here.HereMapAPI;
import com.oceanwing.at.routing.here.Route;
import com.oceanwing.at.routing.here.RouteAttributes;
import com.oceanwing.at.routing.here.RoutingRequest;
import com.oceanwing.at.routing.here.RoutingResponse;
import com.oceanwing.at.routing.here.Waypoints;
import com.oceanwing.at.util.MetaDataUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MockGPSService extends IntentService {

    private static final String TAG = MockGPSService.class.getSimpleName();

    private static MockGPSService sContext;

    // Defines and instantiates an object for handling status updates.
    private BroadcastNotifier mBroadcaster;

    private static final int ONGOING_NOTIFICATION_ID = 1;

    public static final String ACTION_START = "com.oceanwing.at.action.START";
    public static final String ACTION_STOP = "com.oceanwing.at.action.STOP";

    private static final String HERE_MAP_ROUTING_API_VERSION = "HERE_MAP_ROUTING_API_VERSION";
    private static final String HERE_MAP_APP_ID = "HERE_MAP_APP_ID";
    private static final String HERE_MAP_APP_CODE = "HERE_MAP_APP_CODE";
    private static final String GOOGLE_MAP_API_KEY = "GOOGLE_MAP_API_KEY";

    private static final String mMockProviderName = LocationManager.GPS_PROVIDER; // 模拟GPS
    private static LocationManager mLocationManager;

    private static boolean sIsPaused;
    private static boolean sIsStopped;

    private Location mCurrentLocation = null;
    private Location mLastLocation = null;

    private static Task sTask;

    public MockGPSService() {
        super("MockGPSService");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
        sContext = this;

        foreground();

        mBroadcaster = new BroadcastNotifier(this);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        addTestProvider(mMockProviderName);
        setTestProviderEnabled(mMockProviderName, true);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        if (!sIsStopped) {
            stop(sContext);
        }
        super.onDestroy();
        sContext = null;
    }

    public static boolean isCreated() {
        return sContext != null;
    }

    private void foreground() {
        Intent notificationIntent = new Intent(this, RunnerActivity.class);
        if (sTask != null) {
            notificationIntent.putExtra(Task.EXTRA_TASK_NAME, sTask.getName());
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setTicker(getString(R.string.app_name))
                .setContentText("Mocking")
                .setSmallIcon(R.drawable.ic_gps_fixed_white_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_gps_fixed_white_48dp))
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    private void addTestProvider(String mockProviderName) {
        switch (mockProviderName) {
            case LocationManager.GPS_PROVIDER:
                if (mLocationManager != null) {
                    mLocationManager.addTestProvider(mMockProviderName,
                            "requiresNetwork" == "", "requiresSatellite" == "requiresSatellite",
                            "requiresCell" == "", "hasMonetaryCost" == "",
                            "supportsAltitude" == "", "supportsSpeed" == "supportsSpeed",
                            "supportsBearing" == "supportsBearing",
                            Criteria.POWER_LOW,
                            Criteria.ACCURACY_FINE);
                }
                break;
        }
    }

    private static void setTestProviderEnabled(String mockProviderName, boolean enabled) {
        if (mLocationManager != null) {
            try {
                mLocationManager.setTestProviderEnabled(mockProviderName, enabled);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private static void removeTestProvider(String mockProviderName) {
        if (mLocationManager != null) {
            try {
                mLocationManager.removeTestProvider(mockProviderName);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private static boolean isTestProviderEnabled(String mockProviderName) {
        if (mLocationManager != null) {
            try {
                return mLocationManager.isProviderEnabled(mockProviderName);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return false;
    }

    public static void start(Context context, Task task) {
        Log.i(TAG, "start");
        if (task != null) {
            sTask = task;
            Intent intent = new Intent(context, MockGPSService.class);
            intent.setAction(ACTION_START);
            intent.putExtra(Task.EXTRA_TASK, sTask);
            context.startService(intent);
        }
    }

    public static void start(Context context, String taskName) {
        Log.i(TAG, "start");
        try {
            Task task = TaskFile.getTask(sContext, taskName);
            start(context, task);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void stop(Context context) {
        sTask = null;
        Log.i(TAG, "stop");
        removeTestProvider(mMockProviderName);
        Intent intent = new Intent(context, MockGPSService.class);
        intent.setAction(ACTION_STOP);
        context.stopService(intent);
        sIsStopped = true;
    }

    public static boolean isPaused() {
        return sIsPaused;
    }

    public static void setPaused(boolean paused) {
        sIsPaused = paused;
    }

    public static boolean isGPSEnabled() {
        return isTestProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void setGPSEnabled(boolean enabled) {
        setTestProviderEnabled(LocationManager.GPS_PROVIDER, enabled);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_START:
                    if (intent.hasExtra(Task.EXTRA_TASK)) {
                        handleActionStart((Task) intent.getParcelableExtra(Task.EXTRA_TASK));
                    } else if (intent.hasExtra(Task.EXTRA_TASK_NAME) && StringUtils.isNotBlank(intent.getStringExtra(Task.EXTRA_TASK_NAME))) {
                        try {
                            Task task = TaskFile.getTask(sContext, intent.getStringExtra(Task.EXTRA_TASK_NAME));
                            if (task != null) {
                                handleActionStart(task);
                            }
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                    break;
            }
        }
    }

    private void handleActionStart(Task task) {
        try {
            sIsStopped = false;

            mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_STARTED, "== start ==");

            RunnerConfig runnerConfig = task.getRunnerConfig();
            List<Position> positions = runnerConfig.getPositions();

            Waypoint origin = null;
            Waypoint dest = null;

            RoutingConfig routingConfig = task.getRoutingConfig();
            if (routingConfig != null && (runnerConfig.isOnline() || positions.isEmpty())) {
                List<Waypoint> waypoints = routingConfig.getWaypoints();
                origin = waypoints.get(0);
                dest = waypoints.get(waypoints.size() - 1);
                Log.i(TAG, String.format("api[%s] (%.6f, %.6f) -> (%.6f, %.6f) run %s at %.2f KPH",
                        routingConfig.getAPI(),
                        origin.getLatLng().getLat(), origin.getLatLng().getLng(),
                        dest.getLatLng().getLat(), dest.getLatLng().getLng(),
                        runnerConfig.getRun(), runnerConfig.getSpeed()));
                positions = calcRoute(routingConfig);

                positions = optimizePositions(routingConfig, runnerConfig, positions);
                runnerConfig.setPositions(positions);
                task.setRunnerConfig(runnerConfig);
                TaskFile.saveTask(sContext, task);
            }

            Position current, next = null;
            String log;
            int state = BroadcastNotifier.STATE_ACTION_ERROR;
            float bearing, speed;
            double distance = 0;

            long run;
            switch (runnerConfig.getRun()) {
                case RunnerConfig.RUN_ONCE:
                    run = 1;
                    break;
                case RunnerConfig.RUN_FOREVER:
                    run = Long.MAX_VALUE;
                    break;
                default:
                    run = Long.valueOf(runnerConfig.getRun());
                    break;
            }

            for (int n = 0; n < run; n++) {
                for (int i = 0; i < positions.size() - 1; ) {
                    if (isTestProviderEnabled(mMockProviderName)) {
                        current = positions.get(i);
                        next = positions.get(i + 1);
                        bearing = current.getBearing() == 0 ? current.getLatLng().bearingTo(next.getLatLng()) : current.getBearing();
                        speed = runnerConfig.getSpeed() == 0 ? kph2mps(runnerConfig.getSpeed()) : current.getSpeed();
                        distance = current.getLatLng().distanceTo(next.getLatLng());

                        setLocation(mMockProviderName,
                                current.getLatLng().getLat(), current.getLatLng().getLng(),
                                bearing, speed);

                        log = String.format("== mocking == [%5d / %5d] (%.6f, %.6f) -> (%.6f, %.6f), bearing = %.0f, distance = %.3f KM",
                                (i + 1), positions.size(),
                                current.getLatLng().getLat(), current.getLatLng().getLng(),
                                next.getLatLng().getLat(), next.getLatLng().getLng(),
                                bearing, distance);
                        state = BroadcastNotifier.STATE_ACTION_MOCKING;
                    } else {
                        log = "== no GPS ==";
                        state = BroadcastNotifier.STATE_ACTION_NO_GPS;
                    }

                    if (sIsPaused) {
                        log = "== pausing ==";
                        state = BroadcastNotifier.STATE_ACTION_PAUSING;
                    } else {
                        i++; // 往下走
                    }

                    Log.i(TAG, log);
                    mBroadcaster.broadcast(state, (sIsPaused ? (i + 1) : i), log);

                    if (i == 1 && origin != null && origin.getParking() > 0) {
                        mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_MOCKING, (sIsPaused ? (i + 1) : i), "== parking ==");
                        Thread.sleep(origin.getParking() * 1000);
                    }

                    long wait = runnerConfig.getUpdateInterval() * 1000;
                    double stepDistance = runnerConfig.getUpdateInterval() * runnerConfig.getSpeed() / 3600D; // 单位距离(1s 走的距离 km)
                    if (runnerConfig.isSteady()) {
                        wait = (long) (distance / stepDistance * wait);
                        Log.i(TAG, String.format("wait=%dms", wait));
                    }
                    Thread.sleep(wait);
                }
                if (next != null) {
                    log = String.format("== mocking == [destination] (%.6f, %.6f)", next.getLatLng().getLat(), next.getLatLng().getLng());
                    Log.i(TAG, log);
                    setLocation(mMockProviderName, next.getLatLng().getLat(), next.getLatLng().getLng(), next.getBearing(), next.getSpeed());
                    mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_MOCKING, positions.size(), log);

                    if (dest != null && dest.getParking() > 0) {
                        mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_MOCKING, positions.size(), "== parking ==");
                        Thread.sleep(dest.getParking() * 1000);
                    }

                }
            }
        } catch (IOException e) {
            Log.e(TAG, "failed to save task", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "failed to set location", e);
        } catch (InterruptedException e) {
            Log.e(TAG, "failed to sleep", e);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "failed to read meta data", e);
        } finally {
            mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_COMPLETE, "== complete ==");
        }
    }


    private List<Position> calcRoute(RoutingConfig routingConfig) throws IOException, PackageManager.NameNotFoundException {
        List<Position> positions = new ArrayList<>();
        List<Waypoint> waypoints = routingConfig.getWaypoints();
        Waypoint origin = waypoints.get(0);
        Waypoint dest = waypoints.get(waypoints.size() - 1);

        switch (routingConfig.getAPI()) {
            case HERE:
                Waypoints wps = new Waypoints(
                        new com.oceanwing.at.routing.here.Position(origin.getLatLng().getLat(), origin.getLatLng().getLng()),
                        new com.oceanwing.at.routing.here.Position(dest.getLatLng().getLat(), dest.getLatLng().getLng()));
                final String APP_ID = MetaDataUtil.read(sContext, HERE_MAP_APP_ID);
                final String APP_CODE = MetaDataUtil.read(sContext, HERE_MAP_APP_CODE);
                final String API_VERSION = MetaDataUtil.read(sContext, HERE_MAP_ROUTING_API_VERSION);
                RoutingRequest hereRequest = new RoutingRequest(APP_ID, APP_CODE, wps, routingConfig.parseHereRoutingMode());
                RouteAttributes routeAttributes = new RouteAttributes(RoutingRequest.RouteAttribute.SHAPE);
                hereRequest.setRouteAttributes(routeAttributes);
                Call<RoutingResponse> routingCall = HereMapAPI.getInstance().routing().calculateRoute(API_VERSION, hereRequest.toMap());
                Response<RoutingResponse> hereResp = routingCall.execute();
                if (hereResp.isSuccessful()) {
                    Log.i(TAG, "here routing successfully");
                    com.oceanwing.at.routing.here.Response response = hereResp.body().getResponse();
                    List<Route> routes = response.getRoute();
                    if (routes != null && !routes.isEmpty()) {
                        Route route = routes.get(0);
                        positions = route.parsePosition();
                    }
                } else {
                    Log.e(TAG, "here routing failed: " + hereResp.errorBody().string());
                    mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_ERROR, ">> routing failed <<\n" + hereResp.errorBody().string());
                }
                break;
            case GOOGLE:
                final String API_KEY = MetaDataUtil.read(sContext, GOOGLE_MAP_API_KEY);
                DestinationsRequest googleRequest = new DestinationsRequest(API_KEY,
                        new com.oceanwing.at.routing.google.LatLng(origin.getLatLng().getLat(), origin.getLatLng().getLng()),
                        new com.oceanwing.at.routing.google.LatLng(dest.getLatLng().getLat(), dest.getLatLng().getLng()));
                googleRequest.setMode(routingConfig.parseGoogleMode());
                googleRequest.setTrafficModel(routingConfig.parseGoogleTrafficModel());
                googleRequest.setAvoids(routingConfig.parseGoogleAvoids());
                Call<DestinationsResponse> call = GoogleMapAPI.getInstance().destinations().getDestinations(googleRequest.toMap());
                Response<DestinationsResponse> googleResp = call.execute();
                if (googleResp.isSuccessful()) {
                    Log.i(TAG, "google routing successfully");
                    List<com.oceanwing.at.routing.google.Route> routes = googleResp.body().getRoutes();
                    if (routes != null && !routes.isEmpty()) {
                        com.oceanwing.at.routing.google.Route route = routes.get(0);
                        positions = route.parsePosition();
                    }
                } else {
                    Log.e(TAG, "google routing failed: " + googleResp.errorBody().string());
                    mBroadcaster.broadcast(BroadcastNotifier.STATE_ACTION_ERROR, ">> routing failed <<\n" + googleResp.errorBody().string());
                }
                break;
        }
        return positions;
    }

    private float kph2mps(float speed) {
        return speed * 1000 / 3600; // km/h -> m/s
    }


    private void setLocation(String mockProviderName, double latitude, double longitude, float bearing, float speed) {
        Location loc = new Location(mockProviderName);
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

        mLocationManager.setTestProviderLocation(mockProviderName, loc);

        mLastLocation = loc;

        Log.d(TAG, String.format("lat=%.6f, lng=%.6f, bearing=%.0f, speed=%.2fm/s", latitude, longitude, loc.getBearing(), speed));
    }

    private List<Position> optimizePositions(RoutingConfig routingConfig, RunnerConfig runnerConfig, List<Position> positions) {
        double step = runnerConfig.getUpdateInterval() * runnerConfig.getSpeed() / 3600D; // 单位距离(1s 走的距离 km)
        List<Position> newPositions = new ArrayList<>();
        Position current, next = null;
        double distance;
        float bearing;
        for (int i = 0; i < positions.size() - 1; i++) {
            current = positions.get(i);
            newPositions.add(current);

            next = positions.get(i + 1);
            distance = current.getLatLng().distanceTo(next.getLatLng());

            // 如果当前点和下一个点的距离 大于 单位距离(1s 走的距离)
            // 则沿着前进方向, 分解成 n 个单位距离以内的点
            if (distance > step) {
                bearing = current.getLatLng().bearingTo(next.getLatLng());
                int n = (int) (distance / step);
                Position start = current;
                for (int j = 0; j < n; j++) {
                    Position tmp = new Position(start.getLatLng().offset(step, bearing));
                    Log.d(TAG, String.format("offset(%d-%d): lat=%.6f, lng=%.6f", i, j,
                            tmp.getLatLng().getLat(), tmp.getLatLng().getLng()));
                    newPositions.add(tmp);
                    start = tmp;
                }
            }
        }
        if (next != null) {
            newPositions.add(next);
        }
        return newPositions;
    }
}
