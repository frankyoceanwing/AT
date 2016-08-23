package com.oceanwing.at.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Waypoint implements Parcelable {

    @SerializedName("point")
    @Expose
    private LatLng mLatLng;
    @SerializedName("parking")
    @Expose
    private long mParking;

    public Waypoint(LatLng latLng) {
        mLatLng = latLng;
    }

    public Waypoint(LatLng latLng, long parking) {
        mLatLng = latLng;
        mParking = parking;
    }

    public Waypoint(double lat, double lng, long parking) {
        mLatLng = new LatLng(lat, lng);
        mParking = parking;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(mLatLng, flags);
        out.writeLong(mParking);

    }

    protected Waypoint(Parcel in) {
        mLatLng = in.readParcelable(LatLng.class.getClassLoader());
        mParking = in.readLong();
    }

    public static final Creator<Waypoint> CREATOR = new Creator<Waypoint>() {
        @Override
        public Waypoint createFromParcel(Parcel in) {
            return new Waypoint(in);
        }

        @Override
        public Waypoint[] newArray(int size) {
            return new Waypoint[size];
        }
    };

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public long getParking() {
        return mParking;
    }

    public void setParking(long parking) {
        mParking = parking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Waypoint waypoint = (Waypoint) o;
        return mParking == waypoint.mParking &&
                Objects.equals(mLatLng, waypoint.mLatLng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mLatLng, mParking);
    }
}
