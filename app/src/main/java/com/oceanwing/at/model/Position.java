package com.oceanwing.at.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Position implements Parcelable {

    @SerializedName("point")
    @Expose
    private LatLng mLatLng;
    @SerializedName("gps_status")
    @Expose
    private int mGPSStatus;
    @SerializedName("network_status")
    @Expose
    private int mNetWorkStatus;

    public Position(double lat, double lng) {
        mLatLng = new LatLng(lat, lng);
    }

    public Position(LatLng latLng) {
        mLatLng = latLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(mLatLng, flags);
        out.writeInt(mGPSStatus);
        out.writeInt(mNetWorkStatus);

    }

    protected Position(Parcel in) {
        mLatLng = in.readParcelable(LatLng.class.getClassLoader());
        mGPSStatus = in.readInt();
        mNetWorkStatus = in.readInt();
    }

    public static final Creator<Position> CREATOR = new Creator<Position>() {
        @Override
        public Position createFromParcel(Parcel in) {
            return new Position(in);
        }

        @Override
        public Position[] newArray(int size) {
            return new Position[size];
        }
    };

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public int getGPSStatus() {
        return mGPSStatus;
    }

    public void setGPSStatus(int GPSStatus) {
        mGPSStatus = GPSStatus;
    }

    public int getNetWorkStatus() {
        return mNetWorkStatus;
    }

    public void setNetWorkStatus(int netWorkStatus) {
        mNetWorkStatus = netWorkStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return mGPSStatus == position.mGPSStatus &&
                mNetWorkStatus == position.mNetWorkStatus &&
                Objects.equals(mLatLng, position.mLatLng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mLatLng, mGPSStatus, mNetWorkStatus);
    }
}
