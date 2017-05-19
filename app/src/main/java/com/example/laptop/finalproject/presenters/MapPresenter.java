package com.example.laptop.finalproject.presenters;


import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.utilities.RxUtils;

import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MapPresenter implements MainContract.IMapPresenter {

    MainInteracter interacter;
    MainContract.IMapView mapView;
    CompositeSubscription compositeSubscription;

    @Inject
    public MapPresenter (MainInteracter interacter) {

        this.interacter = interacter;
    }

    @Override
    public void bind(MainContract.IMapView view) {

        this.mapView = view;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void fetchRestaurant(Integer res_id) {

        compositeSubscription.add(interacter.getRestaurantUseCase(res_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Restaurant_>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();

                        if(e.getClass() == UnknownHostException.class) {
                            mapView.getError("Error: No Internet Connection");
                        }

                        else {
                            mapView.getError(e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(Restaurant_ restaurant) {

                        mapView.getRestaurantData(restaurant);

                    }
                }));

    }

    @Override
    public void unbind() {

        mapView = null;
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }
}
