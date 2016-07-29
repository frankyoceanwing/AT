package com.oceanwing.at;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franky on 16/7/25.
 */
public class LatLng implements Parcelable {

    private double lat;
    private double lng;

    // the Earth's radius is about 6,371km
    private double earthRadius = 6371.0;


    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeDouble(lat);
        out.writeDouble(lng);
        out.writeDouble(earthRadius);
    }

    protected LatLng(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
        earthRadius = in.readDouble();
    }

    public static final Creator<LatLng> CREATOR = new Creator<LatLng>() {
        @Override
        public LatLng createFromParcel(Parcel in) {
            return new LatLng(in);
        }

        @Override
        public LatLng[] newArray(int size) {
            return new LatLng[size];
        }
    };

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getEarthRadius() {
        return earthRadius;
    }

    public void setEarthRadius(double earthRadius) {
        this.earthRadius = earthRadius;
    }

    private double toRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    private double toDegrees(double radians) {
        return radians * 180 / Math.PI;
    }


    public double distanceTo(LatLng p2) {
        double p1Lat = toRadians(this.lat);
        double p1Lng = toRadians(this.lng);
        double p2Lat = toRadians(p2.lat);
        double p2Lng = toRadians(p2.lng);
        double l = p2Lat - p1Lat;
        double g = p2Lng - p1Lng;

        double a = Math.sin(l / 2) * Math.sin(l / 2)
                + Math.cos(p1Lat) * Math.cos(p2Lat)
                * Math.sin(g / 2) * Math.sin(g / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return this.earthRadius * c;
    }


    public float bearingTo(LatLng p2) {
        double p1Lat = toRadians(this.lat);
        double p2Lat = toRadians(p2.lat);
        double r = toRadians(p2.lng - this.lng);

        // see http://mathforum.org/library/drmath/view/55417.html
        double y = Math.sin(r) * Math.cos(p2Lat);
        double x = Math.cos(p1Lat) * Math.sin(p2Lat) -
                Math.sin(p1Lat) * Math.cos(p2Lat) * Math.cos(r);
        double o = Math.atan2(y, x);

        return (float) ((toDegrees(o) + 360) % 360);
    }

    public LatLng destinationPoint(double distance, float bearing) {
        double r = distance / this.earthRadius; // angular distance in radians
        double b = toRadians(bearing);

        double p1Lat = toRadians(this.lat);
        double p1Lng = toRadians(this.lng);

        double p2Lat = Math.asin(Math.sin(p1Lat) * Math.cos(r) + Math.cos(p1Lat) * Math.sin(r) * Math.cos(b));
        double x = Math.cos(r) - Math.sin(p1Lat) * Math.sin(p2Lat);
        double y = Math.sin(b) * Math.sin(r) * Math.cos(p1Lat);
        double p2Lng = p1Lng + Math.atan2(y, x);

        return new LatLng(toDegrees(p2Lat), (toDegrees(p2Lng) + 540) % 360 - 180); // normalise to −180..+180°
    }


}
