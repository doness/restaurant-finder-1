package com.example.laptop.finalproject.services;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.Results;
import com.example.laptop.finalproject.models.ReviewsResult;

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

    @Headers({
            Constants.HEADER_ACCEPT,
            Constants.API_KEY
    })
    @GET("restaurant")
    Observable<Restaurant_> getRestaurant(@Query("res_id") Integer res_id);

    @Headers({
            Constants.HEADER_ACCEPT,
            Constants.API_KEY
    })
    @GET("reviews")
    Observable<ReviewsResult> getUserReviews(@Query("res_id") Integer res_id);
}
