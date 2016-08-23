package com.oceanwing.at;

import com.oceanwing.at.routing.here.HereMapAPI;
import com.oceanwing.at.routing.here.Position;
import com.oceanwing.at.routing.here.RoutingMode;
import com.oceanwing.at.routing.here.RoutingRequest;
import com.oceanwing.at.routing.here.RoutingResponse;
import com.oceanwing.at.routing.here.Waypoints;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class HereMapAPITest {
    private static final String APP_ID = "c7UDnNWvQl2dypUlW2YX";
    private static final String APP_CODE = "7aWDOy8CdYqbD-AXqa9Jbw";
    private static String API_VERSION = "7.2";

    @Test
    public void routingIsCorrect() throws Exception {
        Waypoints waypoints = new Waypoints(
                new Position(42.275135568115324, -71.53734062223702), new Position(42.259973168423336, -71.57715801506309));
        RoutingMode routingMode = new RoutingMode(RoutingMode.Type.FASTEST, RoutingMode.TransportMode.CAR, RoutingMode.TrafficMode.ENABLED);

        RoutingRequest request = new RoutingRequest(APP_ID, APP_CODE, waypoints, routingMode);
        Call<RoutingResponse> routingCall = HereMapAPI.getInstance().routing().calculateRoute(API_VERSION, request.toMap());
        Response<RoutingResponse> resp = routingCall.execute();
        assertTrue(resp.isSuccessful());
        assertNotNull(resp.body().getResponse());
    }
}