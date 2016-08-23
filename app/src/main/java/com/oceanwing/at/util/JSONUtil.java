package com.oceanwing.at.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.oceanwing.at.model.RoutingConfig;
import com.oceanwing.at.model.RoutingConfigJsonSerializer;
import com.oceanwing.at.model.RunnerConfig;
import com.oceanwing.at.model.RunnerConfigJsonSerializer;
import com.oceanwing.at.model.Task;
import com.oceanwing.at.model.TaskJsonSerializer;

import java.io.Reader;

public class JSONUtil {

    private static GsonBuilder sGsonBuilder;
    private static Gson sGson;

    static {
        sGsonBuilder = new GsonBuilder();
        sGsonBuilder.registerTypeAdapter(RoutingConfig.class, new RoutingConfigJsonSerializer());
        sGsonBuilder.registerTypeAdapter(RunnerConfig.class, new RunnerConfigJsonSerializer());
        sGsonBuilder.registerTypeAdapter(Task.class, new TaskJsonSerializer());
        sGsonBuilder.setPrettyPrinting();
        sGson = sGsonBuilder.create();
    }

    private JSONUtil() {
    }

    public static String toJson(Object object) {
        return sGson.toJson(object);
    }

    public static <T> T fromJson(Reader reader, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        return sGson.fromJson(reader, classOfT);
    }
}
