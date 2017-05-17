package com.example.laptop.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
    @BindView(R.id.swUseMyLocation) Switch swUseMyLocation;
    @BindView(R.id.btnFindNearby) Button btnFindNearby;
    @BindView(R.id.toolbarMain) Toolbar toolbarMain;
    @BindView(R.id.btnCuisine) Button btnCuisine;
    @BindView(R.id.btnCategory) Button btnCategory;
    @BindView(R.id.btnPrice) Button btnPrice;
    @BindView(R.id.btnRating) Button btnRating;

    //Initialise the Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Butterknife to the view
        unbinder = ButterKnife.bind(this);
        //Inject the presenter to the view
        ((MyApp)getApplication()).getRestaurants_component().inject(this);

        //sets default values to global variables
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
            swUseMyLocation.setText(Constants.EN_USE_LOCATION);
            btnFindNearby.setText(Constants.EN_BUTTON);
            toolbarMain.setTitle(Constants.EN_MAIN_TOOLBAR_TITLE);
            btnCuisine.setText(Constants.EN_CUISINE_LIST[0]);
            btnCategory.setText(Constants.EN_CATEGORY_LIST[0]);
            btnPrice.setText(Constants.EN_PRICE_LIST[0]);
            btnRating.setText(Constants.EN_RATING_LIST[0]);
        }
        else {

            etPostcode.setHint(Constants.BG_POSTCODE_HINT);
            tvOr.setText(Constants.BG_OR);
            swUseMyLocation.setText(Constants.BG_USE_LOCATION);
            btnFindNearby.setText(Constants.BG_BUTTON);
            toolbarMain.setTitle(Constants.BG_MAIN_TOOLBAR_TITLE);
            btnCuisine.setText(Constants.BG_CUISINE_LIST[0]);
            btnCategory.setText(Constants.BG_CATEGORY_LIST[0]);
            btnPrice.setText(Constants.BG_PRICE_LIST[0]);
            btnRating.setText(Constants.BG_RATING_LIST[0]);
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
        //bind the presenter to the view
        if (input_validity) {
            presenter.bind(this);
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

        btnCuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogueBox(0);
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogueBox(1);
            }
        });

        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogueBox(2);

            }
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogueBox(3);

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
        presenter.unbind();
    }

    //build an Alert Dialogue for filter selection
    public void displayDialogueBox(int dialogue_type) {
        String title_0;
        String title_1;
        String title_2;
        String title_3;

        CharSequence list_0 [];
        CharSequence list_1 [];
        CharSequence list_2 [];
        CharSequence list_3 [];


        if (!language_type) {
            title_0 = Constants.BG_CUISINE_LIST[0];
            title_1 = Constants.BG_CATEGORY_LIST[0];
            title_2 = Constants.BG_PRICE_LIST[0];
            title_3 = Constants.BG_RATING_LIST[0];

            list_0 = Constants.BG_AD_CUISINE_LIST;
            list_1 = Constants.BG_AD_CATEGORY_LIST;
            list_2 = Constants.BG_AD_PRICE_LIST;
            list_3 = Constants.BG_AD_RATING_LIST;
        }

        else {
            title_0 = Constants.EN_CUISINE_LIST[0];
            title_1 = Constants.EN_CATEGORY_LIST[0];
            title_2 = Constants.EN_PRICE_LIST[0];
            title_3 = Constants.EN_RATING_LIST[0];

            list_0 = Constants.EN_AD_CUISINE_LIST;
            list_1 = Constants.EN_AD_CATEGORY_LIST;
            list_2 = Constants.EN_AD_PRICE_LIST;
            list_3 = Constants.EN_AD_RATING_LIST;
        }

        switch (dialogue_type) {
            case 0:
                AlertDialog.Builder builder0 = new AlertDialog.Builder(this);
                final CharSequence l0 [] = list_0;
                builder0.setTitle(title_0);
                builder0.setItems(list_0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        target_cuisine = l0[which].toString();
                        btnCuisine.setText(target_cuisine);
                        dialog.dismiss();
                    }
                });
                builder0.show();

                break;
            case 1:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                final CharSequence l1 [] = list_1;
                builder1.setTitle(title_1);
                builder1.setItems(list_1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        target_category = l1[which].toString();
                        btnCategory.setText(target_category);
                        dialog.dismiss();
                    }
                });
                builder1.show();

                break;
            case 2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                final CharSequence l2 [] = list_2;
                builder2.setTitle(title_2);
                builder2.setItems(list_2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        target_price = l2[which].toString();
                        btnPrice.setText(target_price);
                        dialog.dismiss();
                    }
                });
                builder2.show();

                break;
            case 3:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                final CharSequence l3 [] = list_3;
                builder3.setTitle(title_3);
                builder3.setItems(list_3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        target_rating = l3[which].toString();
                        btnRating.setText(target_rating);
                        dialog.dismiss();
                    }
                });
                builder3.show();

                break;
        }
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