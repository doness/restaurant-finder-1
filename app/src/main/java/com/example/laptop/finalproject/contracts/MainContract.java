package com.example.laptop.finalproject.contracts;


import android.content.Context;

import com.example.laptop.finalproject.models.MarkerDataParcel;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.Results;

import java.util.List;

public interface MainContract {

    interface IMainPresenter{

        void bind(IMainView view);
        void getUserInputs(Context context, String location, String cuisine, String category, String price, String reviews);
        void fetchMarkerData();
        void getResults(Results results);
        void prepareMarkerData(List<Restaurant_> restaurants);
        void unbind();
    }

    interface IMainView{

        void confirmData(boolean dataState);
        void startMapActivity(MarkerDataParcel markerDataParcel);
        void getError(String error_message);
    }

    interface IMapPresenter{
        void bind(IMapView view);
        void fetchRestaurant(Integer res_id);
        void unbind();
    }

    interface IMapView{
        void getRestaurantData(Restaurant_ restaurant);
        void getError(String error_message);
    }
}

