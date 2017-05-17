package com.example.laptop.finalproject.injection;

import android.app.Application;
import android.content.Context;


public class MyApp extends Application {

    Restaurants_Component restaurants_component;
    private static Context context;

    public Restaurants_Component getRestaurants_component() {

        return restaurants_component;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        restaurants_component = DaggerRestaurants_Component.create();
        context = getApplicationContext();
    }

    public static Context getContext() {

        return context;
    }
}
