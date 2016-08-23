package com.oceanwing.at.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RunnerConfig implements Parcelable {

    public static final String RUN_ONCE = "Once";
    public static final String RUN_FOREVER = "Forever";

    public static final String DEFAULT_RUN = RUN_ONCE;
    public static final float DEFAULT_SPEED = 60;
    public static final int DEFAULT_UPDATE_INTERVAL = 1;

    @SerializedName("online")
    @Expose
    private boolean mOnline = false;

    @SerializedName("speed")
    @Expose
    private float mSpeed = DEFAULT_SPEED;

    @SerializedName("steady")
    @Expose
    private boolean mSteady = false;

    @SerializedName("update_interval")
    @Expose
    private long mUpdateInterval = DEFAULT_UPDATE_INTERVAL;

    @SerializedName("run")
    @Expose
    private String mRun = DEFAULT_RUN;

    @SerializedName("positions")
    @Expose
    private List<Position> mPositions = new ArrayList<>();

    public RunnerConfig() {
    }

    protected RunnerConfig(Parcel in) {
        mOnline = in.readByte() != 0;
        mSpeed = in.readFloat();
        mSteady = in.readByte() != 0;
        mUpdateInterval = in.readLong();
        mRun = in.readString();
        mPositions = in.createTypedArrayList(Position.CREATOR);
    }

    public static final Creator<RunnerConfig> CREATOR = new Creator<RunnerConfig>() {
        @Override
        public RunnerConfig createFromParcel(Parcel in) {
            return new RunnerConfig(in);
        }

        @Override
        public RunnerConfig[] newArray(int size) {
            return new RunnerConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (mOnline ? 1 : 0));
        parcel.writeFloat(mSpeed);
        parcel.writeByte((byte) (mSteady ? 1 : 0));
        parcel.writeLong(mUpdateInterval);
        parcel.writeString(mRun);
        parcel.writeTypedList(mPositions);
    }

    public boolean isOnline() {
        return mOnline;
    }

    public void setOnline(boolean online) {
        mOnline = online;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    public long getUpdateInterval() {
        return mUpdateInterval;
    }

    public void setUpdateInterval(long updateInterval) {
        mUpdateInterval = updateInterval;
    }

    public boolean isSteady() {
        return mSteady;
    }

    public void setSteady(boolean steady) {
        mSteady = steady;
    }

    public String getRun() {
        return mRun;
    }

    public void setRun(String run) {
        mRun = run;
    }

    public List<Position> getPositions() {
        return mPositions;
    }

    public void setPositions(List<Position> positions) {
        mPositions = positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunnerConfig that = (RunnerConfig) o;
        return mOnline == that.mOnline &&
                Float.compare(that.mSpeed, mSpeed) == 0 &&
                mSteady == that.mSteady &&
                mUpdateInterval == that.mUpdateInterval &&
                Objects.equals(mRun, that.mRun) &&
                Objects.equals(mPositions, that.mPositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mOnline, mSpeed, mSteady, mUpdateInterval, mRun, mPositions);
    }
}
