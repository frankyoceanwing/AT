package com.oceanwing.at.util;

import com.oceanwing.at.model.LatLng;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public class LatLngUtil {

    private LatLngUtil() {
    }

    // the Earth's radius is about 6371.009 KM
    public static final double EARTH_RADIUS = 6371.009;

    public static double distance(LatLng from, LatLng to) {
        double fromLat = toRadians(from.getLat());
        double fromLng = toRadians(from.getLng());
        double toLat = toRadians(to.getLat());
        double toLng = toRadians(to.getLng());
        double l = toLat - fromLat;
        double g = toLng - fromLng;

        double a = sin(l / 2) * sin(l / 2)
                + cos(fromLat) * cos(toLat)
                * sin(g / 2) * sin(g / 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public static float bearing(LatLng from, LatLng to) {
        double fromLat = toRadians(from.getLat());
        double toLat = toRadians(to.getLat());
        double r = toRadians(to.getLng() - from.getLng());

        // see http://mathforum.org/library/drmath/view/55417.html
        double y = sin(r) * cos(toLat);
        double x = cos(fromLat) * sin(toLat) -
                sin(fromLat) * cos(toLat) * cos(r);
        double o = atan2(y, x);

        return (float) ((toDegrees(o) + 360) % 360);
    }

    public static LatLng offset(LatLng from, double distance, float bearing) {
        double r = distance / EARTH_RADIUS; // angular distance in radians
        double b = toRadians(bearing);

        double fromLat = toRadians(from.getLat());
        double fromLng = toRadians(from.getLng());

        double p2Lat = asin(sin(fromLat) * cos(r) + cos(fromLat) * sin(r) * cos(b));
        double x = cos(r) - sin(fromLat) * sin(p2Lat);
        double y = sin(b) * sin(r) * cos(fromLat);
        double p2Lng = fromLng + atan2(y, x);

        return new LatLng(toDegrees(p2Lat), (toDegrees(p2Lng) + 540) % 360 - 180); // normalise to −180..+180°
    }
}
