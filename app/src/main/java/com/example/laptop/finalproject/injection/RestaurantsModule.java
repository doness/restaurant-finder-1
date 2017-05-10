package com.example.laptop.finalproject.injection;


import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.services.RestaurantsService;

import dagger.Module;
import dagger.Provides;

@Module
public class RestaurantsModule {

    @Provides
    public MainInteracter getInteracterObject(){

        return new RestaurantsService();
    }
}
