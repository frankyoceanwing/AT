package com.oceanwing.at.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class RunnerConfigJsonSerializer implements JsonSerializer<RunnerConfig> {
    @Override
    public JsonElement serialize(RunnerConfig runnerConfig, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("online", context.serialize(runnerConfig.isOnline()));
        object.add("speed", context.serialize(runnerConfig.getSpeed()));
        object.add("steady", context.serialize(runnerConfig.isSteady()));
        object.add("update_interval", context.serialize(runnerConfig.getUpdateInterval()));
        object.add("run", context.serialize(runnerConfig.getRun()));
        object.add("positions", context.serialize(runnerConfig.getPositions()));
        return object;
    }
}
