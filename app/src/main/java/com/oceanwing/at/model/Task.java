package com.oceanwing.at.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oceanwing.at.util.DateTimeUtil;

import java.util.List;
import java.util.Objects;

public class Task implements Parcelable {

    public static final String EXTRA_TASK = "com.oceanwing.at.extra.task";
    public static final String EXTRA_TASK_NAME = "com.oceanwing.at.extra.task.name";
    public static final String EXTRA_TASK_TYPE = "com.oceanwing.at.extra.task.type";
    public static final String ACTION_NEW = "com.oceanwing.at.action.task.NEW";
    public static final String ACTION_OPEN = "com.oceanwing.at.action.task.OPEN";

    @SerializedName("name")
    @Expose
    private String mName = DateTimeUtil.getCurrentTimeInString(DateTimeUtil.yyyyMMddHHmmss);

    @SerializedName("type")
    @Expose
    private Type mType = Type.MOCK;

    @SerializedName("routing_config")
    @Expose
    private RoutingConfig mRoutingConfig = new RoutingConfig();

    @SerializedName("runner_config")
    @Expose
    private RunnerConfig mRunnerConfig = new RunnerConfig();

    public Task() {
    }

    public Task(Type type, List<Waypoint> waypoints) {
        mType = type;
        mRoutingConfig = new RoutingConfig(waypoints);
    }


    protected Task(Parcel in) {
        mName = in.readString();
        mRoutingConfig = in.readParcelable(RoutingConfig.class.getClassLoader());
        mRunnerConfig = in.readParcelable(RunnerConfig.class.getClassLoader());
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }

    public RoutingConfig getRoutingConfig() {
        return mRoutingConfig;
    }

    public void setRoutingConfig(RoutingConfig routingConfig) {
        mRoutingConfig = routingConfig;
    }

    public RunnerConfig getRunnerConfig() {
        return mRunnerConfig;
    }

    public void setRunnerConfig(RunnerConfig runnerConfig) {
        mRunnerConfig = runnerConfig;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeParcelable(mRoutingConfig, i);
        parcel.writeParcelable(mRunnerConfig, i);
    }


    public enum Type {
        MOCK(0),
        RECORD(1);

        private int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(mName, task.mName) &&
                mType == task.mType &&
                Objects.equals(mRoutingConfig, task.mRoutingConfig) &&
                Objects.equals(mRunnerConfig, task.mRunnerConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mName, mType, mRoutingConfig, mRunnerConfig);
    }
}
