package com.oceanwing.at;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.oceanwing.at.model.RoutingConfig;
import com.oceanwing.at.model.RunnerConfig;
import com.oceanwing.at.model.Task;
import com.oceanwing.at.model.Waypoint;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ATCompatActivity extends Activity {

    private static final String TAG = ATCompatActivity.class.getSimpleName();

    private ATCompatActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        mContext = this;
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            switch (intent.getAction()) {
                case MockGPSService.ACTION_START:
                    Uri data = getIntent().getData();
                    if (data != null) {
                        start(data);
                    }
                    break;
                case MockGPSService.ACTION_STOP:
                    stop();
                    break;
                default:
                    break;
            }
        }
        finish();
    }

    private void start(Uri data) {
        Log.i(TAG, "start Uri: " + data.toString());
        // mockgps://com.oceanwing.at/path?origin=37.7256676,-122.4496459&destination=36.1633689,-115.1444786
        String scheme = data.getScheme();
        if (!getString(R.string.scheme).equalsIgnoreCase(scheme)) {
            return;
        }
        String host = data.getHost();
        if (!getString(R.string.host).equalsIgnoreCase(host)) {
            return;
        }

        Waypoint origin = parseWaypoint(data, "origin");
        if (origin == null) {
            return;
        }

        Waypoint dest = parseWaypoint(data, "destination");
        if (dest == null) {
            return;
        }

        RoutingConfig.API api = RoutingConfig.API.HERE;
        String apiQuery = data.getQueryParameter("api");
        if (StringUtils.isNotBlank(apiQuery)) {
            try {
                api = RoutingConfig.API.valueOf(apiQuery.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return;
            }
        }

        String transport = (api == RoutingConfig.API.HERE) ? RoutingConfig.HERE_TRANSPORT_CAR
                : RoutingConfig.GOOGLE_TRANSPORT_DRIVING;
        String transportQuery = data.getQueryParameter("transport");
        if (StringUtils.isNotBlank(transportQuery)) {
            transport = transportQuery.trim();
        }

        String mode = (api == RoutingConfig.API.HERE) ? RoutingConfig.HERE_MODE_FAST
                : RoutingConfig.GOOGLE_MODE_BEST_GUESS;
        String modeQuery = data.getQueryParameter("mode");
        if (StringUtils.isNotBlank(modeQuery)) {
            mode = modeQuery.trim();
        }

        boolean avoidTolls = false;
        String avoidTollsQuery = data.getQueryParameter("avoid_tolls");
        if (StringUtils.isNotBlank(avoidTollsQuery)) {
            try {
                avoidTolls = Boolean.valueOf(avoidTollsQuery.trim());
            } catch (NumberFormatException e) {
                return;
            }
        }

        boolean avoidFerries = false;
        String avoidFerriesQuery = data.getQueryParameter("avoid_ferries");
        if (StringUtils.isNotBlank(avoidFerriesQuery)) {
            try {
                avoidFerries = Boolean.valueOf(avoidFerriesQuery.trim());
            } catch (NumberFormatException e) {
                return;
            }
        }

        boolean avoidHighways = false;
        String avoidHighwaysQuery = data.getQueryParameter("avoid_highways");
        if (StringUtils.isNotBlank(avoidHighwaysQuery)) {
            try {
                avoidHighways = Boolean.valueOf(avoidHighwaysQuery.trim());
            } catch (NumberFormatException e) {
                return;
            }
        }

        boolean traffic = false;
        String trafficQuery = data.getQueryParameter("traffic");
        if (StringUtils.isNotBlank(trafficQuery)) {
            try {
                traffic = Boolean.valueOf(trafficQuery.trim());
            } catch (NumberFormatException e) {
                return;
            }
        }

        float speed = RunnerConfig.DEFAULT_SPEED;
        String speedStr = data.getQueryParameter("speed");
        if (StringUtils.isNotBlank(speedStr)) {
            try {
                speed = Float.parseFloat(speedStr.trim());
            } catch (NumberFormatException e) {
                return;
            }
            if (speed < 10 || speed > 200) {
                return;
            }
        }

        boolean steady = false;
        String steadyQuery = data.getQueryParameter("steady");
        if (StringUtils.isNotBlank(steadyQuery)) {
            try {
                steady = Boolean.valueOf(steadyQuery.trim());
            } catch (NumberFormatException e) {
                return;
            }
        }

        long updateInterval = RunnerConfig.DEFAULT_UPDATE_INTERVAL;
        String updateIntervalQuery = data.getQueryParameter("update_interval");
        if (StringUtils.isNotBlank(updateIntervalQuery)) {
            try {
                updateInterval = Long.parseLong(updateIntervalQuery.trim());
            } catch (NumberFormatException e) {
                return;
            }
            if (updateInterval < 1) {
                return;
            }
        }

        String run = RunnerConfig.DEFAULT_RUN;
        String runQuery = data.getQueryParameter("run");
        if (StringUtils.isNotBlank(runQuery)) {
            run = runQuery.trim();
            if (!StringUtils.equalsIgnoreCase(run, RunnerConfig.RUN_ONCE)
                    && !StringUtils.equalsIgnoreCase(run, RunnerConfig.RUN_FOREVER)) {
                try {
                    Integer.parseInt(run);
                } catch (NumberFormatException e) {
                    return;
                }
            }
        }


        Task task = new Task();

        List<Waypoint> waypoints = new ArrayList<>();
        waypoints.add(origin);
        waypoints.add(dest);
        RoutingConfig routingConfig = new RoutingConfig(waypoints);
        routingConfig.setAPI(api);
        routingConfig.setTransport(transport);
        routingConfig.setMode(mode);
        routingConfig.setAvoidTolls(avoidTolls);
        routingConfig.setAvoidFerries(avoidFerries);
        routingConfig.setAvoidHighways(avoidHighways);
        routingConfig.setTraffic(traffic);
        task.setRoutingConfig(routingConfig);

        RunnerConfig runnerConfig = new RunnerConfig();
        runnerConfig.setOnline(true);
        runnerConfig.setSpeed(speed);
        runnerConfig.setSteady(steady);
        runnerConfig.setUpdateInterval(updateInterval);
        runnerConfig.setRun(run);

        MockGPSService.start(mContext, task);
    }

    private Waypoint parseWaypoint(Uri data, String key) {
        String query = data.getQueryParameter(key);
        String[] latLngParking = StringUtils.split(query, ",");
        if (latLngParking == null
                || (latLngParking.length != 2 && latLngParking.length != 3)) {
            return null;
        }
        double lat, lng;
        long parking = 0L;
        try {
            lat = Double.parseDouble(latLngParking[0].trim());
            lng = Double.parseDouble(latLngParking[1].trim());
            if (latLngParking.length == 3) {
                parking = Long.parseLong(latLngParking[2].trim());
            }
        } catch (NumberFormatException e) {
            return null;
        }
        if (lat <= -90 || lat >= 90 || lng <= -180 && lng >= 180) {
            return null;
        }
        if (parking < 0) {
            return null;
        }
        return new Waypoint(lat, lng, parking);
    }

    private void stop() {
        Log.i(TAG, "stop");
        MockGPSService.stop(mContext);
    }
}
