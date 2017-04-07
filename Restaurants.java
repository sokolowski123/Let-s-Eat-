package cs307spring17team26.lets_eat_;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ryan on 4/4/2017.
 */

public class Restaurants {

    private YelpAPI yelpApi;
    private Map<String, String> params;

    public Restaurants() {

        final String consumerKey = "jINYfs_pNzwGGFJJdVUF-g";
        final String consumerSecret = "d0Z7lmAkmiSfP8uXFKPF8SUOuCk";
        final String token = "PZy9JPPRb4RvqXSuFyJerK8xaYBYf6KH";
        final String tokenSecret = "ONRsjq3_gxHWF7v5xergJO5r94M";
        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
        yelpApi = apiFactory.createAPI();

        params.put("term", "restaurants");

    }

    public ArrayList<Business> getRestaurants(int maxRange, double longitude, double latitude) {

        params.put("radius", Double.toString(maxRange * 1609.34));

        CoordinateOptions coordinates = CoordinateOptions.builder().longitude(longitude).latitude(latitude).build();

        Call<SearchResponse> call = yelpApi.search(coordinates, params);

        Response<SearchResponse> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            return null;
        }

        ArrayList<Business> businesses = response.body().businesses();

        return businesses;

    }

}
