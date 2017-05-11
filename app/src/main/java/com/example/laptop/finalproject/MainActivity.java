package com.example.laptop.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.injection.MyApp;
import com.example.laptop.finalproject.models.MarkerDataParcel;
import com.example.laptop.finalproject.presenters.MainPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements MainContract.IMainView {
    //initialise the variables
    @Inject MainPresenter presenter;

    Unbinder unbinder;
    boolean language_type;
    boolean location_check;
    boolean input_validity;
    String target_location;
    String target_cuisine;
    String target_category;
    String target_price;
    String target_rating;

    @BindView(R.id.etPostcode) EditText etPostcode;
    @BindView(R.id.tvOr) TextView tvOr;
    @BindView(R.id.tvFilters) TextView tvFilters;
    @BindView(R.id.swUseMyLocation) Switch swUseMyLocation;
    @BindView(R.id.tvCuisine) TextView tvCuisine;
    @BindView(R.id.tvCategory) TextView tvCategory;
    @BindView(R.id.tvPrice) TextView tvPrice;
    @BindView(R.id.tvRating) TextView tvRating;
    @BindView(R.id.btnFindNearby) Button btnFindNearby;
    @BindView(R.id.toolbarMain) Toolbar toolbarMain;
    @BindView(R.id.spCuisine) Spinner spCuisine;
    @BindView(R.id.spCategory) Spinner spCategory;
    @BindView(R.id.spPrice) Spinner spPrice;
    @BindView(R.id.spRating) Spinner spRating;

    //Initialise the Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Butterknife to the view
        unbinder = ButterKnife.bind(this);
        //Inject and bind the presenter to the view
        ((MyApp)getApplication()).getRestaurants_component().inject(this);
        presenter.bind(this);

        //sets default values to gloval variables
        initDefaultValues();
        //initialise the toolbar
        setupToolbar();
        //assign the views with the correct language option (EN by default)
        setupViews();
        //assign listeners
        setupListeners();
        //assign the button
        setupButton();
    }

    //Clean up after the Activity ends
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
        presenter.unbind();
    }

    //View logic below

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.btnEN){
            if (language_type) {

                return true;
            }
            else {
                this.language_type = true;
                setupViews();

                return true;
            }
        }
        else if (item.getItemId() == R.id.btnBG) {
            if (!language_type){
                return true;
            }

            else {
                this.language_type = false;
                setupViews();

                return true;
            }
        }
        else {

            return super.onOptionsItemSelected(item);
        }
    }

    private void setupViews() {

        //check which language has been selected and setup the views accordingly

        if (language_type) {

            etPostcode.setHint(Constants.EN_POSTCODE_HINT);
            tvOr.setText(Constants.EN_OR);
            tvFilters.setText(Constants.EN_FILTERS);
            swUseMyLocation.setText(Constants.EN_USE_LOCATION);
            tvCuisine.setText(Constants.EN_CUISINE);
            tvCategory.setText(Constants.EN_CATEGORY);
            tvPrice.setText(Constants.EN_PRICE);
            tvRating.setText(Constants.EN_RATING);
            btnFindNearby.setText(Constants.EN_BUTTON);
            toolbarMain.setTitle(Constants.EN_MAIN_TOOLBAR_TITLE);
            ArrayAdapter<String> cuisine_adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, Constants.EN_CUISINE_LIST);
            spCuisine.setAdapter(cuisine_adapter);
            ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, Constants.EN_CATEGORY_LIST);
            spCategory.setAdapter(category_adapter);
            ArrayAdapter<String> price_adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, Constants.EN_PRICE_LIST);
            spPrice.setAdapter(price_adapter);

            ArrayAdapter<String> rating_adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, Constants.EN_RATING_LIST);
            spRating.setAdapter(rating_adapter);
        }
        else {

            etPostcode.setHint(Constants.BG_POSTCODE_HINT);
            tvOr.setText(Constants.BG_OR);
            tvFilters.setText(Constants.BG_FILTERS);
            swUseMyLocation.setText(Constants.BG_USE_LOCATION);
            tvCuisine.setText(Constants.BG_CUISINE);
            tvCategory.setText(Constants.BG_CATEGORY);
            tvPrice.setText(Constants.BG_PRICE);
            tvRating.setText(Constants.BG_RATING);
            btnFindNearby.setText(Constants.BG_BUTTON);
            toolbarMain.setTitle(Constants.BG_MAIN_TOOLBAR_TITLE);
            ArrayAdapter<String> cuisine_adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, Constants.BG_CUISINE_LIST);
            spCuisine.setAdapter(cuisine_adapter);
            ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, Constants.BG_CATEGORY_LIST);
            spCategory.setAdapter(category_adapter);
            ArrayAdapter<String> price_adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, Constants.BG_PRICE_LIST);
            spPrice.setAdapter(price_adapter);

            ArrayAdapter<String> rating_adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, Constants.BG_RATING_LIST);
            spRating.setAdapter(rating_adapter);
        }
    }

    private void setupToolbar() {

        setSupportActionBar(toolbarMain);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    private void setupButton() {

        //Set a click listener on the button
        //when pressed, collect all user inputs
        //and pass to presenter

        btnFindNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLocation();

                if (input_validity) {

                    presenter.getUserInputs(getApplicationContext(), target_location, target_cuisine, target_category,
                            target_price, target_rating);
                }

                else {
                    if (language_type) {

                        Toast.makeText(getApplicationContext(), Constants.EN_TOAST_ONLY_ONE_INPUT,
                                Toast.LENGTH_LONG).show();
                    }
                    else {

                        Toast.makeText(getApplicationContext(), Constants.BG_TOAST_ONLY_ONE_INPUT,
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void checkLocation() {
        //check if the user wants to use GPS location or postcode

        if (location_check) {

            if ((etPostcode.getText().toString()).equals("")) {

                this.input_validity = true;
                this.target_location = Constants.USE_MY_LOCATION;
            }
            else {

                this.input_validity = false;
            }
        }
        else {

            this.target_location = etPostcode.getText().toString();
            this.input_validity = true;
        }
    }

    private void setupListeners(){
        //assign listeners to all the views that require user input
        //store the selected data so it can be passed to the presenter

        swUseMyLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                location_check = isChecked;
            }
        });

        spCuisine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                target_cuisine = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                target_category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                target_price = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                target_rating = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void confirmData(boolean dataState) {

        //confirm the user inputs are all valid
        //if not, inform the user
        //otherwise, start MapsActivity

        if (!dataState){
            if (language_type) {

                Toast.makeText(this, Constants.EN_TOAST_INVALID_POSTCODE, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, Constants.BG_TOAST_INVALID_POSTCODE, Toast.LENGTH_LONG).show();
            }
        }

        else {
            //Prepare the data so it can be sent to the map activity
            presenter.fetchMarkerData();
        }
    }

    @Override
    public void startMapActivity(MarkerDataParcel markerDataParcel) {
        //pass the required data to the map activity and start it
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        intent.putExtra("markerData", markerDataParcel);
        startActivity(intent);
    }

    public void initDefaultValues() {

        //assign the default values to the global variables

        this.location_check = false;
        this.language_type = true;
        this.target_location = "";
        this.target_cuisine = "";
        this.target_category = "";
        this.target_price = "";
        this.target_rating = "";
        this.input_validity = false;
    }
}