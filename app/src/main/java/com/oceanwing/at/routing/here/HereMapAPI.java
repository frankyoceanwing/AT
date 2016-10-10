package com.oceanwing.at.routing.here;


import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public class HereMapAPI {

    private volatile static HereMapAPI mInstance;
    private static Retrofit mRetrofit;

    private HereMapAPI() {
    }

    public synchronized static HereMapAPI getInstance() {
        if (mInstance == null) {
            mInstance = new HereMapAPI();
        }
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("https://route.cit.api.here.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mInstance;
    }

    public interface RoutingService {
        @GET("routing/{version}/calculateroute.json")
        Call<RoutingResponse> calculateRoute(@Path("version") String version, @QueryMap Map<String, String> options);

    }

    public RoutingService routing() {
        return mRetrofit.create(RoutingService.class);
    }
}