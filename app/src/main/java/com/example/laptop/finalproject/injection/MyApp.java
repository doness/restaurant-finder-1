package com.example.laptop.finalproject.injection;

import android.app.Application;


public class MyApp extends Application {

    Restaurants_Component restaurants_component;

    public Restaurants_Component getRestaurants_component() {

        return restaurants_component;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        restaurants_component = DaggerRestaurants_Component.create();
    }
}
