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

    private double mAltitude;
    private float mAccuracy;
    private float mSpeed;
    private float mBearing;

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
        out.writeDouble(mAltitude);
        out.writeFloat(mAccuracy);
        out.writeFloat(mSpeed);
        out.writeFloat(mBearing);
    }

    protected Position(Parcel in) {
        mLatLng = in.readParcelable(LatLng.class.getClassLoader());
        mGPSStatus = in.readInt();
        mNetWorkStatus = in.readInt();
        mAltitude = in.readDouble();
        mAccuracy = in.readFloat();
        mSpeed = in.readFloat();
        mBearing = in.readFloat();
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

    public double getAltitude() {
        return mAltitude;
    }

    public void setAltitude(double altitude) {
        mAltitude = altitude;
    }

    public float getAccuracy() {
        return mAccuracy;
    }

    public void setAccuracy(float accuracy) {
        mAccuracy = accuracy;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    public float getBearing() {
        return mBearing;
    }

    public void setBearing(float bearing) {
        mBearing = bearing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return mGPSStatus == position.mGPSStatus &&
                mNetWorkStatus == position.mNetWorkStatus &&
                mAltitude == position.mAltitude &&
                mAccuracy == position.mAccuracy &&
                mSpeed == position.mSpeed &&
                mBearing == position.mBearing &&
                Objects.equals(mLatLng, position.mLatLng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mLatLng, mGPSStatus, mNetWorkStatus, mAltitude, mAccuracy, mSpeed, mBearing);
    }
}
