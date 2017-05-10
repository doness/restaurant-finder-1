package com.example.laptop.finalproject.injection;


import com.example.laptop.finalproject.MainActivity;

import dagger.Component;

@Component(dependencies = RestaurantsModule.class)
public interface Restaurants_Component {

    void inject(MainActivity mainActivity);
}
