package com.example.laptop.finalproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.fragments.MainFragment;
import com.example.laptop.finalproject.fragments.RestaurantListView;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//This will be the main activity that controls and communicates with all the fragments
//It is called from the MainActivity after the user has defined what types of restaurants
//should be displayed

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        MainContract.IMapView, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private List<MarkerData> markerData;
    ProgressDialog progressDialog;
    private List<Marker> markerList;

    @Inject
    MapPresenter presenter;

    Unbinder unbinder;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SupportMapFragment mapFragment;
    MainFragment mainFragment;
    RestaurantListView restaurantListView;
    Restaurant_ restaurant_data;
    int click_counter;
    boolean map_view;
    boolean list_view;
    String id_counter;

    @BindView(R.id.toolbarMaps)
    Toolbar toolbarMaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //inject and bind presenter and butterknife
        ((MyApp) getApplication()).getRestaurants_component().inject(this);
        presenter.bind(this);
        unbinder = ButterKnife.bind(this);

        //counters to ensure double clicking a marker takes you to the MainFragment
        click_counter = 0;
        id_counter = "";
        map_view = true;
        list_view = false;
        markerList = new ArrayList<>();

        //setup toolbar
        setupToolbar();

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

        if ((markerData.get(0).location_check) == 1) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        //Calls a method which populates the map based on user input passed from MainActivity
        //and data retrieved from the API.
        populateMap();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //unbind butterknife and presenter from the view
        presenter.unbind();
        unbinder.unbind();
        mMap.clear();
    }

    private void populateMap() {
        //generates markers with onClickListeners attached to them
        //and displays them on the map
        //marker data comes from user inputs in MainActivity
        //mMap.setMinZoomPreference(17);
        for (int i = 0; i < markerData.size(); i++) {

            LatLng mapLatLng = new LatLng(markerData.get(i).restaurant_lat,
                    markerData.get(i).restaurant_lon);

            mMap.setOnInfoWindowClickListener(this);
            mMap.setOnMarkerClickListener(this);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(mapLatLng)
                    .title(String.valueOf(markerData.get(i).restaurant_name))
                    .snippet("Offers: " + markerData.get(i).restaurant_cuisines + "; Price: "  +
                            Constants.EN_PRICE_LIST[(markerData.get(i).restaurant_price)]
                            + ", Rating: " + String.valueOf(markerData.get(i).restaurant_rating) ));

            markerList.add(marker);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng, 17));

        }

        if (markerData.size() == 0) {
            LatLng default_LatLng = new LatLng(51.504167, -0.076271);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(default_LatLng, 17));

            Toast.makeText(this, "No Matching Restaurants Found", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //if user clicks on an InfoWindow,
        // get the details from the presenter on the selected restaurant
        //marker ids match restaurant positions in the markerData list
        //so we just fetch the restaurant id of the parcel at that position

        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage(Constants.EN_PROGRESS_DIALOG);
        progressDialog.show();

        Integer restaurant_id = Integer.parseInt(markerData.get(Integer.parseInt(marker.getId()
                .replaceAll("m", ""))).restaurant_id);

        presenter.fetchRestaurant(restaurant_id);
    }

    public void getDataFromList(Integer restaurant_id) {

        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage(Constants.EN_PROGRESS_DIALOG);
        progressDialog.show();

        presenter.fetchRestaurant(restaurant_id);

    }

    public void showMarkerFromList(int position){

        double lat = markerData.get(position).restaurant_lat;
        double lon = markerData.get(position).restaurant_lon;
        Marker marker = markerList.get(position);

        map_view = true;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out,
                R.anim.card_flip_right_in, R.anim.card_flip_right_out);
        fragmentTransaction.hide(restaurantListView);
        fragmentTransaction.show(mapFragment);
        fragmentTransaction.commit();
        LatLng mapLatLng = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng, 17));
        marker.showInfoWindow();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //if a marker is double-clicked, fetch info for that restaurant
        //used mostly for ui testing purposes
        if (click_counter >= 1 && id_counter.equals(marker.getId())){

            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage(Constants.EN_PROGRESS_DIALOG);
            progressDialog.show();

            click_counter = 0;
            id_counter = "";

            Integer restaurant_id = Integer.parseInt(markerData.get(Integer.parseInt(marker.getId()
                    .replaceAll("m", ""))).restaurant_id);

            presenter.fetchRestaurant(restaurant_id);
        }

        else {
            click_counter++;
            id_counter = marker.getId();
        }

        return false;
    }

    @Override
    public void getRestaurantData(Restaurant_ restaurant) {

        //when presenter.fetchRestaurant(res_id); is called, the presenter gets an object
        //containing restaurant details from the API and calls this method in the bound
        //activity so it can access the data
        //we pass the object to the MainFragment and display that fragment

        Log.i("Debugging", "Got data from presenter, name is: " + restaurant.getName());

        this.restaurant_data = restaurant;

        fragmentTransaction = fragmentManager.beginTransaction();

        this.mainFragment = new MainFragment();
        if (map_view) {
            fragmentTransaction.hide(mapFragment);
        }

        else {
            fragmentTransaction.hide(restaurantListView);
        }
        fragmentTransaction.add(R.id.fragment_container, mainFragment, "MAIN_FRAGMENT");
        fragmentTransaction.addToBackStack("MAP FRAGMENT");
        fragmentTransaction.commit();

        mainFragment.receiveRestaurantData(restaurant_data);
        getSupportActionBar().hide();
        progressDialog.dismiss();

    }

    @Override
    public void getError(String error_message) {

        Toast.makeText(this, error_message, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
    }

    private void setupToolbar(){

        setSupportActionBar(toolbarMaps);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("Restaurant Finder");
        toolbarMaps.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.maps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnRestaurantList){

            if (map_view) {

                map_view = false;
                Log.i("Debugging", "Show List selected");

                if (list_view) {

                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                            R.anim.card_flip_left_in, R.anim.card_flip_left_out);
                    fragmentTransaction.hide(mapFragment);
                    fragmentTransaction.show(restaurantListView);
                    fragmentTransaction.commit();

                    return true;
                }

                else {

                    fragmentTransaction = fragmentManager.beginTransaction();

                    restaurantListView = new RestaurantListView();
                    fragmentTransaction.setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                            R.anim.card_flip_left_in, R.anim.card_flip_left_out);
                    fragmentTransaction.hide(mapFragment);
                    fragmentTransaction.add(R.id.fragment_container, restaurantListView, "RESTAURANT_LIST");
                    fragmentTransaction.commit();

                    MapsActivity parentActivity = this;

                    restaurantListView.receiveListData(markerData, parentActivity);

                    return true;
                }
            }

            else {

                map_view = true;
                list_view = true;

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out,
                        R.anim.card_flip_right_in, R.anim.card_flip_right_out);
                fragmentTransaction.hide(restaurantListView);
                fragmentTransaction.show(mapFragment);
                fragmentTransaction.commit();


                return true;
            }
        }

        else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportActionBar().show();
    }
}
