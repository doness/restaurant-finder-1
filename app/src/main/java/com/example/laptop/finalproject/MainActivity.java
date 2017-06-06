package com.example.laptop.finalproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.injection.MyApp;
import com.example.laptop.finalproject.models.MAStateParcel;
import com.example.laptop.finalproject.models.MarkerDataParcel;
import com.example.laptop.finalproject.presenters.MainPresenter;
import com.squareup.picasso.Picasso;

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
    boolean toolbar_hidden_check;
    String target_location;
    String target_cuisine;
    String target_category;
    String target_price;
    String target_rating;
    AlphaAnimation buttonClick;
    ProgressDialog progressDialog;
    MenuItem location_btn;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    //bind views
    @BindView(R.id.etPostcode) EditText etPostcode;
    @BindView(R.id.btnFindNearby) Button btnFindNearby;
    @BindView(R.id.toolbarMain) Toolbar toolbarMain;
    @BindView(R.id.ivCuisine) ImageView ivCuisine;
    @BindView(R.id.ivCategory) ImageView ivCategory;
    @BindView(R.id.ivPrice) ImageView ivPrice;
    @BindView(R.id.ivRating) ImageView ivRating;
    @BindView(R.id.tvCuisine) TextView tvCuisine;
    @BindView(R.id.tvCategory) TextView tvCategory;
    @BindView(R.id.tvPrice) TextView tvPrice;
    @BindView(R.id.tvRating) TextView tvRating;
    @BindView(R.id.svMainScrollView) ScrollView svMainScrollView;

    //Initialise the Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Butterknife to the view
        unbinder = ButterKnife.bind(this);
        //Inject the presenter in the view
        //binding to view happens in checkLocation()
        ((MyApp)getApplication()).getRestaurants_component().inject(this);
        //sets the default non-input values
        initDefaultValues();
        //sets default values to global variables related to user inputs
        initDefaultInputValues();
        //initialise the toolbar
        checkIntent();
        setupToolbar();
        //setup the image buttons
        setupImages();
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

        location_btn = menu.findItem(R.id.btnLocation);
        if (location_check){
            location_btn.setIcon(R.mipmap.crosshair_selected);
        }

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
                boolean i = location_check;
                initDefaultInputValues();
                location_check = i;


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
                boolean i = location_check;
                initDefaultInputValues();
                location_check = i;

                return true;
            }
        }
        else if (item.getItemId() == R.id.btnLocation) {
            if (location_check){
                location_check = false;
                item.setIcon(R.mipmap.crosshair);
                etPostcode.setText("");
            }
            else{
                location_check = true;
                item.setIcon(R.mipmap.crosshair_selected);
                if (!language_type){
                    etPostcode.setText(Constants.BG_USE_LOCATION);
                }
                else{
                    etPostcode.setText(Constants.EN_USE_LOCATION);
                }
            }

            return true;
        }
        else {

            return super.onOptionsItemSelected(item);
        }
    }

    private void setupViews() {

        //check which language has been selected and setup the views accordingly

        if (language_type) {

            etPostcode.setHint(Constants.EN_POSTCODE_HINT);
            btnFindNearby.setText(Constants.EN_BUTTON);
            tvCuisine.setText(Constants.EN_CUISINE_LIST[0]);
            tvCategory.setText(Constants.EN_CATEGORY_LIST[0]);
            tvPrice.setText(Constants.EN_PRICE_LIST[0]);
            tvRating.setText(Constants.EN_RATING_LIST[0]);
            progressDialog.setMessage(Constants.EN_PROGRESS_DIALOG);
            if (location_check) {
                etPostcode.setText(Constants.EN_USE_LOCATION);
            }
        }
        else {

            etPostcode.setHint(Constants.BG_POSTCODE_HINT);
            btnFindNearby.setText(Constants.BG_BUTTON);
            tvCuisine.setText(Constants.BG_CUISINE_LIST[0]);
            tvCategory.setText(Constants.BG_CATEGORY_LIST[0]);
            tvPrice.setText(Constants.BG_PRICE_LIST[0]);
            tvRating.setText(Constants.BG_RATING_LIST[0]);
            progressDialog.setMessage(Constants.BG_PROGRESS_DIALOG);
            if (location_check) {
                etPostcode.setText(Constants.BG_USE_LOCATION);
            }
        }
    }

    private void setupToolbar() {

        setSupportActionBar(toolbarMain);
        try {
            getSupportActionBar().setIcon(R.mipmap.rf_icon);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void setupButton() {

        //Set a click listener on the button
        //when pressed, collect all user inputs
        //and pass to presenter

        btnFindNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                checkLocation();

                if (input_validity) {

                    presenter.getUserInputs(getApplicationContext(), target_location, target_cuisine, target_category,
                            target_price, target_rating);
                }

                else {
                    progressDialog.dismiss();
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

            if (((etPostcode.getText().toString()).equals("")) ||
                    ((etPostcode.getText().toString()).equals(Constants.EN_USE_LOCATION)) ||
                    ((etPostcode.getText().toString()).equals(Constants.BG_USE_LOCATION))) {

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

        ivCuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                displayDialogueBox(0);
            }
        });

        ivCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                displayDialogueBox(1);
            }
        });

        ivPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                displayDialogueBox(2);
            }
        });

        ivRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                displayDialogueBox(3);
            }
        });

        //hiding the toolbar when scrolling
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            svMainScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (oldScrollY >= toolbarMain.getHeight() && scrollY < toolbarMain.getHeight()
                            && toolbar_hidden_check){
                        toolbarMain.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                        toolbar_hidden_check = false;
                    }
                    else if (scrollY > toolbarMain.getHeight() && !toolbar_hidden_check){
                        toolbarMain.animate().translationY(-toolbarMain.getHeight())
                                .setInterpolator(new AccelerateInterpolator(2));
                        toolbar_hidden_check = true;
                    }
                }
            });
        }
    }

    @Override
    public void confirmData(boolean dataState) {

        //confirm the user inputs are all valid
        //if not, inform the user
        //otherwise, start MapsActivity

        if (!dataState){

            progressDialog.dismiss();

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
    public void getError(String error_message) {

        progressDialog.dismiss();

        if (error_message.equals(Constants.LOCATION_ERROR)){

            requestLocationService();
        }

        else if (error_message.equals(Constants.LOCATION_SERVICE_ERROR)) {

            checkLocationServiceEnabled();
        }

        else{
            Toast.makeText(this, error_message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void startMapActivity(MarkerDataParcel markerDataParcel) {
        //pass the required data to the map activity and start it
        Integer temp_loc = 0;
        if (location_check) {
            temp_loc = 1;
        }
        Integer temp_lang = 1;
        if (!language_type){
            temp_lang = 0;
        }
        MAStateParcel maStateParcel = new MAStateParcel(temp_lang, temp_loc, (etPostcode.getText()).toString());
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        /**
         * TODO: Added flag and finish
         */
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("markerData", markerDataParcel);
        intent.putExtra("maState", maStateParcel);
        startActivity(intent);
        progressDialog.dismiss();
        finish();
    }

    private void displayDialogueBox(int dialogue_type) {
        //build an Alert Dialogue for filter selection

        String title_0;
        String title_1;
        String title_2;
        String title_3;

        CharSequence list_0 [];
        CharSequence list_1 [];
        CharSequence list_2 [];
        CharSequence list_3 [];

        String pretitle_0;
        String pretitle_1;
        String pretitle_2;
        String pretitle_3;


        if (!language_type) {
            title_0 = Constants.BG_CUISINE_LIST[0];
            title_1 = Constants.BG_CATEGORY_LIST[0];
            title_2 = Constants.BG_PRICE_LIST[0];
            title_3 = Constants.BG_RATING_LIST[0];

            list_0 = Constants.BG_AD_CUISINE_LIST;
            list_1 = Constants.BG_AD_CATEGORY_LIST;
            list_2 = Constants.BG_AD_PRICE_LIST;
            list_3 = Constants.BG_AD_RATING_LIST;

            pretitle_0 = Constants.BG_CUISINE;
            pretitle_1 = Constants.BG_CATEGORY;
            pretitle_2 = Constants.BG_PRICE;
            pretitle_3 = Constants.BG_RATING;
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

            pretitle_0 = Constants.EN_CUISINE;
            pretitle_1 = Constants.EN_CATEGORY;
            pretitle_2 = Constants.EN_PRICE;
            pretitle_3 = Constants.EN_RATING;
        }

        switch (dialogue_type) {
            case 0:
                AlertDialog.Builder builder0 = new AlertDialog.Builder(this);
                final CharSequence l0 [] = list_0;
                final String pt_0 = pretitle_0;
                builder0.setTitle(title_0);
                builder0.setItems(list_0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        target_cuisine = l0[which].toString();
                        tvCuisine.setText(pt_0 + target_cuisine);
                        dialog.dismiss();
                    }
                });
                builder0.show();

                break;
            case 1:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                final CharSequence l1 [] = list_1;
                final String pt_1 = pretitle_1;
                builder1.setTitle(title_1);
                builder1.setItems(list_1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        target_category = l1[which].toString();
                        tvCategory.setText(pt_1 + target_category);
                        dialog.dismiss();
                    }
                });
                builder1.show();

                break;
            case 2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                final CharSequence l2 [] = list_2;
                final String pt_2 = pretitle_2;
                builder2.setTitle(title_2);
                builder2.setItems(list_2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        target_price = l2[which].toString();
                        tvPrice.setText(pt_2 + target_price);
                        dialog.dismiss();
                    }
                });
                builder2.show();

                break;
            case 3:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                final CharSequence l3 [] = list_3;
                final String pt_3 = pretitle_3;
                builder3.setTitle(title_3);
                builder3.setItems(list_3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        target_rating = l3[which].toString();
                        tvRating.setText(pt_3 + target_rating);
                        dialog.dismiss();
                    }
                });
                builder3.show();

                break;
        }
    }

    private void setupImages(){

        Picasso.with(getApplicationContext())
                .load("http://fresh-abersoch.co.uk/wp-content/uploads/2014/07/restaurant-food-salat-2.jpg")
                .into(ivCuisine);
        ivCuisine.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);

        Picasso.with(getApplicationContext())
                .load("https://media.timeout.com/images/103720743/image.jpg")
                .into(ivCategory);
        ivCategory.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);

        Picasso.with(getApplicationContext())
                .load("http://libn.com/files/2014/05/Money-in-Wallet-620x330.jpg")
                .into(ivPrice);
        ivPrice.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);

        Picasso.with(getApplicationContext())
                .load("https://cdn.shutterstock.com/shutterstock/videos/17660632/thumb/1.jpg")
                .into(ivRating);
        ivRating.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void initDefaultInputValues() {

        //assign the default values to the global variables

        this.location_check = false;
        this.target_location = "";
        this.target_cuisine = "";
        this.target_category = "";
        this.target_price = "";
        this.target_rating = "";
        this.input_validity = false;
        this.toolbar_hidden_check = false;

    }

    private void initDefaultValues(){
        this.language_type = true;
        this.buttonClick = new AlphaAnimation(1F, 0.8F);
        progressDialog = new ProgressDialog(MainActivity.this);
    }

    private void requestLocationService(){

        String dialog_message;
        String dialog_yes;
        String dialog_no;

        if (language_type){
            dialog_message = Constants.EN_LOCATION_DIALOG_TEXT;
            dialog_yes = Constants.EN_LOCATION_DIALOG_YES;
            dialog_no = Constants.EN_LOCATION_DIALOG_NO;
        }

        else{
            dialog_message = Constants.BG_LOCATION_DIALOG_TEXT;
            dialog_yes = Constants.BG_LOCATION_DIALOG_YES;
            dialog_no = Constants.BG_LOCATION_DIALOG_NO;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(this)
                    .setMessage(dialog_message)
                    .setPositiveButton(dialog_yes, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION );
                        }
                    })
                    .setNegativeButton(dialog_no, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();


        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION );
        }
    }

    private void checkLocationServiceEnabled() {

        String dialog_message;
        String dialog_yes;
        String dialog_no;

        if (language_type){
            dialog_message = Constants.EN_LOCATION_SERVICE_DIALOG_TEXT;
            dialog_yes = Constants.EN_LOCATION_DIALOG_OK;
            dialog_no = Constants.EN_LOCATION_DIALOG_CANCEL;
        }

        else{
            dialog_message = Constants.BG_LOCATION_SERVICE_DIALOG_TEXT;
            dialog_yes = Constants.BG_LOCATION_DIALOG_OK;
            dialog_no = Constants.BG_LOCATION_DIALOG_CANCEL;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(dialog_message)
                .setCancelable(false)
                .setPositiveButton(dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();

        alert.show();
    }

    private void checkIntent(){
        MAStateParcel maStateParcel = getIntent().getParcelableExtra("maState");
        if (maStateParcel == null){
            return;
        }

        if (maStateParcel.language == 0){
            language_type = false;
        }

        if (maStateParcel.location_check == 1){
            location_check = true;
            return;
        }
        etPostcode.setText(maStateParcel.location_text);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
                        presenter.getUserInputs(getApplicationContext(), target_location,
                                target_cuisine, target_category, target_price, target_rating);
                    }

                }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}