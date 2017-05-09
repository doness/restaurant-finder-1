package com.example.laptop.finalproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.laptop.finalproject.constants.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    Unbinder unbinder;
    boolean language_type;

    @BindView(R.id.etPostcode) EditText etPostcode;
    @BindView(R.id.tvOr) TextView tvOr;
    @BindView(R.id.swUseMyLocation) Switch swUseMyLocation;
    @BindView(R.id.tvPrice) TextView tvPrice;
    @BindView(R.id.tvRating) TextView tvRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Butterknife to the view
        unbinder = ButterKnife.bind(this);
        language_type = true;

        if (language_type) {

            etPostcode.setHint(Constants.EN_POSTCODE_HINT);
            tvOr.setText(Constants.EN_OR);
            swUseMyLocation.setText(Constants.EN_USE_LOCATION);
            tvPrice.setText(Constants.EN_PRICE);
            tvRating.setText(Constants.EN_RATING);
        }
        else {

            etPostcode.setHint(Constants.BG_POSTCODE_HINT);
            tvOr.setText(Constants.BG_OR);
            swUseMyLocation.setText(Constants.BG_USE_LOCATION);
            tvPrice.setText(Constants.BG_PRICE);
            tvRating.setText(Constants.BG_RATING);
        }


        /**
         * TODO: Create and implement views that let the user provide input for the API and a way
         * to get to the MapsActivity
         */

        /*Testing the map activity
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(intent);
        */
    }
}