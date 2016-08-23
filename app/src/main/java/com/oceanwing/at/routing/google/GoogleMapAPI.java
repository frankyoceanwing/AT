package com.oceanwing.at.routing.google;


import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public class GoogleMapAPI {

    private volatile static GoogleMapAPI mInstance;
    private static Retrofit mRetrofit;

    private GoogleMapAPI() {
    }

    public synchronized static GoogleMapAPI getInstance() {
        if (mInstance == null) {
            mInstance = new GoogleMapAPI();
        }
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mInstance;
    }

    public interface DestinationsService {
        @GET("directions/json")
        Call<DestinationsResponse> getDestinations(@QueryMap Map<String, String> options);

    }

    public DestinationsService destinations() {
        return mRetrofit.create(DestinationsService.class);
    }
}