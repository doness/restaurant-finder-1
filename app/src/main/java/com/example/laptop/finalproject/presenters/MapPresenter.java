package com.example.laptop.finalproject.presenters;


import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.models.Restaurant_;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapPresenter implements MainContract.IMapPresenter {

    MainInteracter interacter;
    MainContract.IMapView mapView;

    @Inject
    public MapPresenter (MainInteracter interacter) {

        this.interacter = interacter;
    }

    @Override
    public void bind(MainContract.IMapView view) {

        this.mapView = view;
    }

    @Override
    public void fetchRestaurant(Integer res_id) {

        interacter.getRestaurantUseCase(res_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Restaurant_>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(Restaurant_ restaurant) {

                        mapView.getRestaurantData(restaurant);

                    }
                });

    }

    @Override
    public void unbind() {

        mapView = null;
    }
}
