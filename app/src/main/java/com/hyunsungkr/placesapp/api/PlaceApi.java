package com.hyunsungkr.placesapp.api;

import com.hyunsungkr.placesapp.model.PlaceList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceApi {

    @GET("/maps/api/place/nearbysearch/json")
    Call<PlaceList> getPlaceList(@Query("keyword") String keyword,
                                 @Query("location") String location,
                                 @Query("radius") int radius,
                                 @Query("language") String language,
                                 @Query("pagetoken") String pagetoken,
                                 @Query("key") String key);
}
