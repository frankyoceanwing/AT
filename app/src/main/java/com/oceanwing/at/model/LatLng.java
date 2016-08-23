package com.oceanwing.at.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oceanwing.at.util.LatLngUtil;

import java.util.Objects;

public class LatLng implements Parcelable {

    @SerializedName("lat")
    @Expose
    private double mLat;
    @SerializedName("lng")
    @Expose
    private double mLng;

    public LatLng(double lat, double lng) {
        this.mLat = lat;
        this.mLng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeDouble(mLat);
        out.writeDouble(mLng);
    }

    protected LatLng(Parcel in) {
        mLat = in.readDouble();
        mLng = in.readDouble();
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
        return mLat;
    }

    public void setLat(double lat) {
        this.mLat = lat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double lng) {
        this.mLng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatLng latLng = (LatLng) o;
        return Double.compare(latLng.mLat, mLat) == 0 &&
                Double.compare(latLng.mLng, mLng) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mLat, mLng);
    }

    public double distanceTo(LatLng to) {
        return LatLngUtil.distance(this, to);
    }

    public float bearingTo(LatLng to) {
        return LatLngUtil.bearing(this, to);
    }

    public LatLng offset(double distance, float bearing) {
        return LatLngUtil.offset(this, distance, bearing);
    }

}
