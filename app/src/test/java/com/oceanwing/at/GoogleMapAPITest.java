package com.oceanwing.at;

import com.oceanwing.at.routing.google.DestinationsRequest;
import com.oceanwing.at.routing.google.DestinationsResponse;
import com.oceanwing.at.routing.google.GoogleMapAPI;
import com.oceanwing.at.routing.google.LatLng;
import com.oceanwing.at.routing.google.Status;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class GoogleMapAPITest {
    private static final String KEY = "AIzaSyBc6iEK9pI1kwJAyPvCdS0tpeQZyJU8igQ";

    @Test
    public void directionsIsCorrect() throws Exception {
        DestinationsRequest request = new DestinationsRequest(KEY, new LatLng(42.275135568115324, -71.53734062223702), new LatLng(42.259973168423336, -71.57715801506309));
        Call<DestinationsResponse> call = GoogleMapAPI.getInstance().destinations().getDestinations(request.toMap());
        Response<DestinationsResponse> resp = call.execute();
        assertTrue(resp.isSuccessful());
        assertEquals(Status.OK, resp.body().getStatus());
    }
}