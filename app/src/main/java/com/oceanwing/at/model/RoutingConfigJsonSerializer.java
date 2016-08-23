package com.oceanwing.at.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class RoutingConfigJsonSerializer implements JsonSerializer<RoutingConfig> {
    @Override
    public JsonElement serialize(RoutingConfig routingConfig, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("waypoints", context.serialize(routingConfig.getWaypoints()));
        object.add("api", context.serialize(routingConfig.getAPI()));
        object.add("transport", context.serialize(routingConfig.getTransport()));
        object.add("mode", context.serialize(routingConfig.getMode()));
        object.add("avoid_tolls", context.serialize(routingConfig.isAvoidTolls()));
        object.add("avoid_ferries", context.serialize(routingConfig.isAvoidFerries()));
        object.add("avoid_highways", context.serialize(routingConfig.isAvoidHighways()));
        object.add("traffic", context.serialize(routingConfig.isTraffic()));
        return object;
    }
}
