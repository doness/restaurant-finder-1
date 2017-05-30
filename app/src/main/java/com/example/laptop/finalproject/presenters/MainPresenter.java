package com.example.laptop.finalproject.presenters;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.models.Location;
import com.example.laptop.finalproject.models.MarkerData;
import com.example.laptop.finalproject.models.MarkerDataParcel;
import com.example.laptop.finalproject.models.Restaurant;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.Results;
import com.example.laptop.finalproject.models.UserRating;
import com.example.laptop.finalproject.utilities.NetworkCheck;
import com.example.laptop.finalproject.utilities.RxUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MainPresenter implements MainContract.IMainPresenter, ConnectionCallbacks, OnConnectionFailedListener {

    private MainContract.IMainView mainView;
    private MainInteracter interacter;
    private boolean maps_location;
    public double lat;
    public double lon;
    public String cuisine_id;
    public String category_id;
    public Integer price_max;
    public double rating_min;
    private boolean inputValidity;
    public int start_offset;
    private GoogleApiClient googleApiClient;
    private List<Restaurant> restaurantList;
    private List<Restaurant_> filteredRestaurants;
    private List<MarkerData> markerDataList;
    private CompositeSubscription compositeSubscription;
    private Context context;
    private boolean data_ready_check;
    public String underTest;


    @Inject
    public MainPresenter(MainInteracter interacter) {

        this.interacter = interacter;
    }

    @Override
    public void bind(MainContract.IMainView view) {

        this.mainView = view;
        filteredRestaurants = new ArrayList<>();
        markerDataList = new ArrayList<>();
        start_offset = 0;
        price_max = 5;
        rating_min = 0.5;
        compositeSubscription = new CompositeSubscription();
        data_ready_check = false;
    }

    @Override
    public void unbind() {
        this.mainView = null;
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    public void getUserInputs(Context context, String location, String cuisine, String category,
                              String price, String reviews) {

        boolean isConnected;

        if (underTest != null) {
            isConnected = true;
        }
        else {
            isConnected =  NetworkCheck.checkConnection(context);
        }

        if (!isConnected) {

            mainView.getError("Error: No Internet Connection");
            return;
        }

        this.context = context;

        //determine the location
        if (location.equals(Constants.USE_MY_LOCATION)) {

            maps_location = true;
            inputValidity = true;
            //Log.i("Debugging", "Inside presenter: Use my location true");

            if (underTest != null){

                maps_location = false;
            }

            else {

                googleApiClient = new GoogleApiClient.Builder(context)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();

                googleApiClient.connect();
            }


        } else if (location.equals("")) {

            maps_location = false;
            inputValidity = false;
            //Log.i("Debugging", "Inside presenter: Use my location false, empty postcode");

        } else {

            inputValidity = false;
            maps_location = false;

            location = location.toUpperCase();


            String regex = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$";

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(location);

            Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
            List<Address> address = null;

            //Log.i("Debugging", "Matcher is: " + String.valueOf(matcher.matches()));

            if (matcher.matches()) {
                boolean b = true;
                try {
                    address = geoCoder.getFromLocationName(location, 10);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    inputValidity = false;
                    b = false;
                    //Log.i("Debugging", "Inside presenter: Error getting location from postcode");
                }
                if (b) {
                    try {
                        Address first = address.get(0);
                        lat = first.getLatitude();
                        lon = first.getLongitude();
                        inputValidity = true;
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        inputValidity = false;
                        //Log.i("Debugging", "Inside presenter: Error parsing postcode");
                    }
                    //Log.i("Debugging", "Inside presenter: Successfully parsed postcode");
                    //Log.i("Debugging", "postcode is: " + location);
                    //Log.i("Debugging", "location is: " + String.valueOf(lat) + ", " + String.valueOf(lon));
                }
            }
        }
        //determine the cuisine type

        if (cuisine.equals(Constants.EN_AD_CUISINE_LIST[0].toString()) ||
                cuisine.equals(Constants.BG_AD_CUISINE_LIST[0].toString())) {
            cuisine_id = Constants.CUISINE_ID_LIST[0];
        }

        int i;
        for (i = 1; i < Constants.CUISINE_ID_LIST.length; i++) {
            if (cuisine.equals(Constants.EN_AD_CUISINE_LIST[i].toString()) ||
                    cuisine.equals(Constants.BG_AD_CUISINE_LIST[i].toString())) {
                cuisine_id = Constants.CUISINE_ID_LIST[i];
                //Log.i("Debugging", "Selected cuisine is: " + Constants.EN_CUISINE_LIST[i] +
                //        ", id is: " + Constants.CUISINE_ID_LIST[i]);

                break;
            }
        }

        //determine the category type

        if (category.equals(Constants.EN_AD_CATEGORY_LIST[0].toString()) ||
                category.equals(Constants.BG_AD_CATEGORY_LIST[0].toString())) {
            category_id = Constants.CATEGORY_ID_LIST[0];
        }

        for (i = 1; i < Constants.CATEGORY_ID_LIST.length; i++) {
            if (category.equals(Constants.EN_AD_CATEGORY_LIST[i].toString()) ||
                    category.equals(Constants.BG_AD_CATEGORY_LIST[i].toString())) {
                category_id = Constants.CATEGORY_ID_LIST[i];
                //Log.i("Debugging", "Selected category is: " + Constants.EN_CATEGORY_LIST[i] +
                //        ", id is: " + Constants.CATEGORY_ID_LIST[i]);
                break;
            }
        }

        //determine the price range
        for (i = 0; i < Constants.EN_PRICE_LIST.length; i++) {
            if (price.equals(Constants.EN_AD_PRICE_LIST[i].toString()) ||
                    price.equals(Constants.BG_AD_PRICE_LIST[i].toString())) {
                price_max = i;
                //Log.i("Debugging", "Selected price is: " + Constants.EN_PRICE_LIST[i]);
                break;
            }
        }

        //determine the rating limit
        for (i = 0; i < Constants.EN_RATING_LIST.length; i++) {
            if (reviews.equals(Constants.EN_AD_RATING_LIST[i].toString()) ||
                    reviews.equals(Constants.BG_AD_RATING_LIST[i].toString())) {
                rating_min = i;
                //Log.i("Debugging", "Selected rating is: " + Constants.EN_RATING_LIST[i]);
                break;
            }
        }

        //let the view know if the input is valid

        if (!maps_location) {


            mainView.confirmData(inputValidity);
        }

        else {
            data_ready_check = true;
        }
    }

    @Override
    public void fetchMarkerData() {

        compositeSubscription.add(interacter.getResultsUseCase(start_offset, lat, lon, cuisine_id, category_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Results>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();

                        if (e.getClass() == UnknownHostException.class) {
                            mainView.getError("No Internet Connection");
                        } else {
                            mainView.getError(e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(Results results) {

                        getResults(results);

                    }
                }));

    }

    @Override
    public void getResults(Results results) {

        restaurantList = results.getRestaurants();

        if (results.getResultsFound() == 0){
            mainView.getError("Error: No valid results");
            return;
        }

        if (price_max == 0) {
            price_max = 5;
        }

        for (Restaurant restaurant : restaurantList) {
            Restaurant_ temp_restaurant = restaurant.getRestaurant();
            UserRating temp_rating = temp_restaurant.getUserRating();
            if (temp_restaurant.getPriceRange() > price_max) {
                continue;
            } else if (temp_rating.getAggregateRating() < (rating_min - 0.5)) {
                continue;
            }

            filteredRestaurants.add(temp_restaurant);

        }

        if (filteredRestaurants != null) {
            prepareMarkerData(filteredRestaurants);
        } else {
            mainView.getError("Error: No valid results");
        }
    }

    @Override
    public void prepareMarkerData(List<Restaurant_> restaurants) {

        //Log.i("Debugging", "Got the restaurants");
        for (Restaurant_ restaurant : restaurants) {
            String temp_id = restaurant.getId();
            Location temp_location = restaurant.getLocation();
            double temp_lat = Double.parseDouble(temp_location.getLatitude());
            double temp_lon = Double.parseDouble(temp_location.getLongitude());
            String temp_name = restaurant.getName();
            Integer temp_price = restaurant.getPriceRange();
            UserRating temp_userRating = restaurant.getUserRating();
            double temp_rating = temp_userRating.getAggregateRating();
            String temp_cuisines = restaurant.getCuisines();
            Integer temp_location_check = 0;
            android.location.Location tempStart = new android.location.Location("tempStart");
            tempStart.setLatitude(lat);
            tempStart.setLongitude(lon);
            android.location.Location tempEnd = new android.location.Location("tempEnd");
            tempEnd.setLatitude(Double.parseDouble(temp_location.getLatitude()));
            tempEnd.setLongitude(Double.parseDouble(temp_location.getLongitude()));
            double temp_distance = tempStart.distanceTo(tempEnd);
            if (maps_location){
                temp_location_check = 1;
            }

            MarkerData temp_markerData = new MarkerData(temp_id, temp_lat, temp_lon, temp_name,
                    temp_price, temp_rating, temp_cuisines, temp_location_check, temp_distance);

            markerDataList.add(temp_markerData);

        }
        MarkerDataParcel markerDataParcel = new MarkerDataParcel(markerDataList);

        mainView.startMapActivity(markerDataParcel);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            mainView.getError(Constants.LOCATION_ERROR);
            return;
        }
        android.location.Location lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);

        if (lastLocation != null){
            lat = lastLocation.getLatitude();
            lon = lastLocation.getLongitude();
            boolean gotData = true;
            boolean dataIsReady;
            dataIsReady = data_ready_check;

            if (!dataIsReady){

                int i = 0;

                while (!dataIsReady) {

                    i++;
                    try {
                        wait(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dataIsReady = data_ready_check;

                    if (i>25){
                        gotData = false;
                        break;
                    }
                }
            }

            if (gotData){
                mainView.confirmData(inputValidity);
            }
            else {
                mainView.getError("Error: Invalid Input");
            }
        }
        else {
            mainView.getError(Constants.LOCATION_SERVICE_ERROR);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mainView.getError(connectionResult.getErrorMessage());
    }
}