package com.example.laptop.finalproject.interacters;

import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.Results;

import rx.Observable;

/**
 * Created by laptop on 10/05/2017.
 */

public interface MainInteracter {

    Observable<Results> getResultsUseCase(int start, double lat, double lon, String cuisines,
                                          String category);

    Observable<Restaurant_> getRestaurantUseCase(Integer res_id);
}
