package com.example.laptop.finalproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.models.MarkerData;
import com.example.laptop.finalproject.models.MarkerDataParcel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

//This will be the main activity that controls and communicates with all the fragments
//It is called from the MainActivity after the user has defined what types of restaurants
//should be displayed

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MainContract.IMapView {

    private GoogleMap mMap;
    List<MarkerData> markerDataList1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MarkerDataParcel markerDataParcel = getIntent().getParcelableExtra("markerData");
        markerDataList1 = markerDataParcel.markerDataList;

        if (markerDataParcel != null) {

            Log.i("Debugging", "Got the data in the Map Activity");
            //Log.i("Debugging", "List is of size" + markerDataList1.size());
            Log.i("Debugging", "1 and 2 name are: " + markerDataList1.get(0).restaurant_name + ", " +
                    markerDataList1.get(1).restaurant_name);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
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

    private void populateMap() {

        /**
         * TODO: Implement the method which populates he map with markers based on user input
         */
    }

    public void getMarkerData() {

    }
}
