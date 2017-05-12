package com.example.laptop.finalproject.injection;


import com.example.laptop.finalproject.MainActivity;
import com.example.laptop.finalproject.MapsActivity;
import com.example.laptop.finalproject.fragments.Tab2;
import com.example.laptop.finalproject.fragments.Tab3;

import dagger.Component;

@Component(dependencies = RestaurantsModule.class)
public interface Restaurants_Component {

    void inject(MainActivity mainActivity);
    void inject(MapsActivity mapsActivity);
    void inject(Tab2 tab2);
    void inject(Tab3 tab3);
}
