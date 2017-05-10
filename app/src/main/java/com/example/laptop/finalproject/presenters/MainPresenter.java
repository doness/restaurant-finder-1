package com.example.laptop.finalproject.presenters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.interacters.MainInteracter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;


public class MainPresenter implements MainContract.IMainPresenter{

    private MainContract.IMainView mainView;
    private MainInteracter interacter;
    private boolean maps_location;
    private double lat;
    private double lon;
    private String cuisine_id;
    private String category_id;
    private Integer price_max;
    private Integer rating_min;
    private boolean inputValidity;


    @Inject
    public MainPresenter (MainInteracter interacter) {

        this.interacter = interacter;
    }

    @Override
    public void bind(MainContract.IMainView view) {

        this.mainView = view;
    }

    @Override
    public void getUserInputs(Context context, String location, String cuisine, String category,
                              String price, String reviews) {

        //determine the location
        if (location.equals(Constants.USE_MY_LOCATION)) {

            maps_location = true;
            inputValidity = true;
            Log.i("Debugging", "Inside presenter: Use my location true");

        }

        else if (location.equals("")) {

            maps_location = false;
            inputValidity = false;
            Log.i("Debugging", "Inside presenter: Use my location false, empty postcode");

        }

        else {

            maps_location = false;
            Geocoder geoCoder = new Geocoder(context ,Locale.getDefault());
            List<Address> address = null;

            if (geoCoder != null) {
                boolean b = true;
                try {
                    address = geoCoder.getFromLocationName(location, 10);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    inputValidity = false;
                    b = false;
                    Log.i("Debugging", "Inside presenter: Error getting location from postcode");
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
                        Log.i("Debugging", "Inside presenter: Error parsing postcode");

                    }

                    Log.i("Debugging", "Inside presenter: Successfully parsed postcode");
                    Log.i("Debugging", "postcode is: " + location);
                    Log.i("Debugging", "location is: " + String.valueOf(lat) + ", " + String.valueOf(lon));



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
                Log.i("Debugging", "Selected cuisine is: " + Constants.EN_CUISINE_LIST[i] +
                        ", id is: " + Constants.CUISINE_ID_LIST[i]);

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
                Log.i("Debugging", "Selected category is: " + Constants.EN_CATEGORY_LIST[i] +
                        ", id is: " + Constants.CATEGORY_ID_LIST[i]);
                break;
            }
        }

        //determine the price range
        for (i=0; i<Constants.EN_PRICE_LIST.length; i++){
            if (price.equals(Constants.EN_PRICE_LIST[i]) || price.equals(Constants.BG_PRICE_LIST[i])){
                price_max = i;
                Log.i("Debugging", "Selected price is: " + Constants.EN_PRICE_LIST[i]);
                break;
            }
        }

        //determine the rating limit
        for (i=0; i<Constants.EN_RATING_LIST.length; i++){
            if (reviews.equals(Constants.EN_RATING_LIST[i]) || reviews.equals(Constants.BG_RATING_LIST[i])){
                rating_min = i;
                Log.i("Debugging", "Selected rating is: " + Constants.EN_RATING_LIST[i]);
                break;
            }
        }

        //let the view know if the input is valid
        mainView.confirmData(inputValidity);
    }

    @Override
    public void unbind() {
        this.mainView = null;
    }
}
