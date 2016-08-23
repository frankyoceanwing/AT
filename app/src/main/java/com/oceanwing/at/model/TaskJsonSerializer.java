package com.oceanwing.at.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TaskJsonSerializer implements JsonSerializer<Task> {
    @Override
    public JsonElement serialize(Task task, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type", context.serialize(task.getType()));
        object.add("name", context.serialize(task.getName()));
        object.add("routing_config", context.serialize(task.getRoutingConfig()));
        object.add("runner_config", context.serialize(task.getRunnerConfig()));
        return object;
    }
}
