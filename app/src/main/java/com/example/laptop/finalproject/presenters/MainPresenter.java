package com.example.laptop.finalproject.presenters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainPresenter implements MainContract.IMainPresenter{

    private MainContract.IMainView mainView;
    private MainInteracter interacter;
    private boolean maps_location;
    private double lat;
    private double lon;
    private String cuisine_id;
    private String category_id;
    private Integer price_max;
    private double rating_min;
    private boolean inputValidity;
    private int start_offset;
    private List<Restaurant> restaurantList;
    private List<Restaurant_> filteredRestaurants;
    private List<MarkerData> markerDataList;


    @Inject
    public MainPresenter (MainInteracter interacter) {

        this.interacter = interacter;
    }

    @Override
    public void bind(MainContract.IMainView view) {

        this.mainView = view;
        filteredRestaurants = new ArrayList<>();
        markerDataList = new ArrayList<>();
        start_offset = 0;
    }

    @Override
    public void unbind() {
        this.mainView = null;
    }

    @Override
    public void getUserInputs(Context context, String location, String cuisine, String category,
                              String price, String reviews) {

        //determine the location
        if (location.equals(Constants.USE_MY_LOCATION)) {

            maps_location = true;
            inputValidity = true;
            //Log.i("Debugging", "Inside presenter: Use my location true");

        }

        else if (location.equals("")) {

            maps_location = false;
            inputValidity = false;
            //Log.i("Debugging", "Inside presenter: Use my location false, empty postcode");

        }

        else {

            inputValidity = false;

            String regex = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$";

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(location);

            maps_location = false;
            Geocoder geoCoder = new Geocoder(context ,Locale.getDefault());
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
                    }

                    catch (IndexOutOfBoundsException e){
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

        if (cuisine.equals(Constants.EN_CUISINE_LIST[0]) || cuisine.equals(Constants.BG_CUISINE_LIST[0])){
            cuisine_id = Constants.CUISINE_ID_LIST[0];
        }

        int i;
        for (i=1; i<Constants.CUISINE_ID_LIST.length; i++){
            if (cuisine.equals(Constants.EN_CUISINE_LIST[i]) || cuisine.equals(Constants.BG_CUISINE_LIST[i])){
                cuisine_id = Constants.CUISINE_ID_LIST[i];
                //Log.i("Debugging", "Selected cuisine is: " + Constants.EN_CUISINE_LIST[i] +
                //        ", id is: " + Constants.CUISINE_ID_LIST[i]);

                break;
            }
        }

        //determine the category type

        if (category.equals(Constants.EN_CATEGORY_LIST[0]) || category.equals(Constants.BG_CATEGORY_LIST[0])){
            category_id = Constants.CATEGORY_ID_LIST[0];
        }

        for (i=1; i<Constants.CATEGORY_ID_LIST.length; i++){
            if (category.equals(Constants.EN_CATEGORY_LIST[i]) || category.equals(Constants.BG_CATEGORY_LIST[i])){
                category_id = Constants.CATEGORY_ID_LIST[i];
                //Log.i("Debugging", "Selected category is: " + Constants.EN_CATEGORY_LIST[i] +
                //        ", id is: " + Constants.CATEGORY_ID_LIST[i]);
                break;
            }
        }

        //determine the price range
        for (i=0; i<Constants.EN_PRICE_LIST.length; i++){
            if (price.equals(Constants.EN_PRICE_LIST[i]) || price.equals(Constants.BG_PRICE_LIST[i])){
                price_max = i;
                //Log.i("Debugging", "Selected price is: " + Constants.EN_PRICE_LIST[i]);
                break;
            }
        }

        //determine the rating limit
        for (i=0; i<Constants.EN_RATING_LIST.length; i++){
            if (reviews.equals(Constants.EN_RATING_LIST[i]) || reviews.equals(Constants.BG_RATING_LIST[i])){
                rating_min = i;
                //Log.i("Debugging", "Selected rating is: " + Constants.EN_RATING_LIST[i]);
                break;
            }
        }

        //let the view know if the input is valid
        mainView.confirmData(inputValidity);
    }

    @Override
    public void fetchMarkerData() {

        interacter.getResultsUseCase(start_offset, lat, lon, cuisine_id, category_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Results>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.i("Debugging", "error");
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(Results results) {

                        Log.i("Debugging", "About to send the results");

                        getResults(results);

                    }
                });

    }

    @Override
    public void getResults(Results results) {

        restaurantList = results.getRestaurants();

        Log.i("Debugging", "Got the results");

        if (rating_min == 5) {
            rating_min = 4.5;
        }
        if (price_max == 0) {
            price_max = 6;
        }

        for (Restaurant restaurant : restaurantList){
            Restaurant_ temp_restaurant = restaurant.getRestaurant();
            UserRating temp_rating = temp_restaurant.getUserRating();
            if (temp_restaurant.getPriceRange() > price_max) {
                continue;
            }
            else if (temp_rating.getAggregateRating() < rating_min) {
                continue;
            }

            filteredRestaurants.add(temp_restaurant);

        }

        if (filteredRestaurants != null){
            prepareMarkerData(filteredRestaurants);
        }

        else{
            Log.i("Debugging", "No valid restaurants");
        }
    }

    @Override
    public void prepareMarkerData(List<Restaurant_> restaurants){

        Log.i("Debugging", "Got the restaurants");
        for (Restaurant_ restaurant : restaurants){
            String temp_id = restaurant.getId();
            Location temp_location = restaurant.getLocation();
            double temp_lat = Double.parseDouble(temp_location.getLatitude());
            double temp_lon = Double.parseDouble(temp_location.getLongitude());
            String temp_name = restaurant.getName();
            Integer temp_price = restaurant.getPriceRange();
            UserRating temp_userRating = restaurant.getUserRating();
            double temp_rating = temp_userRating.getAggregateRating();
            String temp_cuisines = restaurant.getCuisines();

            MarkerData temp_markerData = new MarkerData(temp_id, temp_lat, temp_lon, temp_name,
                    temp_price, temp_rating, temp_cuisines);

            markerDataList.add(temp_markerData);

        }

        Log.i("Debugging", "Inside presenter, first item in listi is " + markerDataList.get(0).restaurant_name);

        MarkerDataParcel markerDataParcel = new MarkerDataParcel(markerDataList);

        mainView.startMapActivity(markerDataParcel);
    }


}
