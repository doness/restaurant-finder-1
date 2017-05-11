package com.example.laptop.finalproject.services;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.models.Results;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface IRestaurantsAPI {

    @Headers({
            Constants.HEADER_ACCEPT,
            Constants.API_KEY
    })
    @GET("search")
    Observable<Results> getSearchResults(@Query("start") int start,
                                         @Query("lat") double lat, @Query("lon") double lon,
                                         @Query("radius") double radius,
                                         @Query("cuisines") String cuisines,
                                         @Query("category") String category);
}
