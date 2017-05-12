package com.example.laptop.finalproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.fragments.MainFragment;
import com.example.laptop.finalproject.injection.MyApp;
import com.example.laptop.finalproject.models.MarkerData;
import com.example.laptop.finalproject.models.MarkerDataParcel;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.presenters.MapPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

//This will be the main activity that controls and communicates with all the fragments
//It is called from the MainActivity after the user has defined what types of restaurants
//should be displayed

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        MainContract.IMapView, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private List<MarkerData> markerData;

    @Inject MapPresenter presenter;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SupportMapFragment mapFragment;
    MainFragment mainFragment;
    Restaurant_ restaurant_data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //inject and bind presenter
        ((MyApp)getApplication()).getRestaurants_component().inject(this);
        presenter.bind(this);

        //fetch data to display the markers
        MarkerDataParcel markerDataParcel = getIntent().getParcelableExtra("markerData");
        markerData = markerDataParcel.markerDataList;

        //set up the fragment manager and transaction

        fragmentManager = getSupportFragmentManager();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Calls a method which populates the map based on user input passed from MainActivity
        //and data retrieved from the API.
        populateMap();

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        presenter.unbind();
        mMap.clear();
    }

    private void populateMap() {

        mMap.setMinZoomPreference(17);
        for (int i = 0; i < markerData.size(); i++) {

            LatLng mapLatLng = new LatLng(markerData.get(i).restaurant_lat,
                    markerData.get(i).restaurant_lon);

            mMap.setOnInfoWindowClickListener(this);
            mMap.addMarker(new MarkerOptions()
                    .position(mapLatLng)
                    .title(String.valueOf(markerData.get(i).restaurant_name))
                    .snippet("Offers: " + markerData.get(i).restaurant_cuisines + "; Price: "  +
                            Constants.EN_PRICE_LIST[(markerData.get(i).restaurant_price)]
                            + ", User Rating: " + String.valueOf(markerData.get(i).restaurant_rating) ));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(mapLatLng));

        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Integer restaurant_id = Integer.parseInt(markerData.get(Integer.parseInt(marker.getId()
                .replaceAll("m", ""))).restaurant_id);

        presenter.fetchRestaurant(restaurant_id);


        /*Log.i("Debugging", marker.getId());
        Log.i("Debuggin", "name is: " + markerData.get(Integer.parseInt(marker.getId()
                .replaceAll("m", ""))).restaurant_name);*/
    }

    @Override
    public void getRestaurantData(Restaurant_ restaurant) {

        Log.i("Debugging", "Got data from presenter, name is: " + restaurant.getName());

        this.restaurant_data = restaurant;

        fragmentTransaction = fragmentManager.beginTransaction();

        this.mainFragment = new MainFragment();
        fragmentTransaction.hide(mapFragment);
        fragmentTransaction.add(R.id.fragment_container, mainFragment, "MAIN_FRAGMENT");
        fragmentTransaction.addToBackStack("MAP FRAGMENT");
        fragmentTransaction.commit();

        mainFragment.receiveRestaurantData(restaurant_data);

    }


}
