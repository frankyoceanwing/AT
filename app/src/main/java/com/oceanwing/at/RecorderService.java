package com.oceanwing.at;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Iterator;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RecorderService extends IntentService {

    private static final String TAG = RecorderService.class.getSimpleName();

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.oceanwing.at.action.FOO";

    private static final String ACTION_BAZ = "com.oceanwing.at.action.BAZ";
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.oceanwing.at.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.oceanwing.at.extra.PARAM2";

    public RecorderService() {
        super("RecorderService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RecorderService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RecorderService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {


    }

    private void monitorLocation(Context context, LocationManager locationManager) {

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "lastKnownLocation permission [ACCESS_COARSE_LOCATION] not granted");
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "lastKnownLocation permission [ACCESS_FINE_LOCATION] not granted");
        }

        for (String s : locationManager.getAllProviders()) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(s);
            if (lastKnownLocation != null) {
                Log.i(TAG, String.format("last known lastKnownLocation [provider=%s, gps=%f,%f, acc=%f, alt=%f, speed=%f, bearing=%f, isMock=%b]",
                        lastKnownLocation.getProvider(),
                        lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                        lastKnownLocation.getAccuracy(),
                        lastKnownLocation.getAltitude(),
                        lastKnownLocation.getSpeed(),
                        lastKnownLocation.getBearing(),
                        lastKnownLocation.isFromMockProvider()));
            }
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, mLocationListener);
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.i(TAG, String.format("lastKnownLocation changed [provider=%s, gps=%f,%f, acc=%f, alt=%f, speed=%f, bearing=%f, isMock=%b]",
                        location.getProvider(),
                        location.getLatitude(), location.getLongitude(),
                        location.getAccuracy(),
                        location.getAltitude(),
                        location.getSpeed(),
                        location.getBearing(),
                        location.isFromMockProvider()));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, String.format("location provider[%s] status[%d] changed", provider, status));
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, String.format("location provider[%s] enabled", provider));
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, String.format("location provider[%s] disabled", provider));
        }
    };

    //状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public LocationManager locationManger;

        public void onGpsStatusChanged(int event) {
            switch (event) {
                //第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "第一次定位");
                    break;
                //卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.i(TAG, "卫星状态改变");
                    //获取当前状态
                    GpsStatus gpsStatus=locationManger.getGpsStatus(null);
                    //获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    //创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    System.out.println("搜索到："+count+"颗卫星");
                    break;
                //定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "定位启动");
                    break;
                //定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "定位结束");
                    break;
            }
        };
    };


    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
