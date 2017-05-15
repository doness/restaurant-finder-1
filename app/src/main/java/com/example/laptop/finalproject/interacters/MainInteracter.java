package com.example.laptop.finalproject.interacters;

import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.Results;
import com.example.laptop.finalproject.models.ReviewsResult;

import rx.Observable;

public interface MainInteracter {

    Observable<Results> getResultsUseCase(int start, double lat, double lon, String cuisines,
                                          String category);

    Observable<Restaurant_> getRestaurantUseCase(Integer res_id);

    Observable<ReviewsResult> getUserReviewsUseCase(Integer res_id);
}
