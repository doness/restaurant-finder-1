package com.example.laptop.finalproject.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.contracts.FragmentsContract;
import com.example.laptop.finalproject.models.DailyMenu;
import com.example.laptop.finalproject.models.Location;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.UserRating;
import com.example.laptop.finalproject.models.UserReviewWrapper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1 extends Fragment implements FragmentsContract.ITabFragment{

    Unbinder unbinder;

    @BindView(R.id.tvRestaurantName) TextView tvRestaurantName;
    @BindView(R.id.tvRestaurantScore) TextView tvRestaurantScore;
    @BindView(R.id.tvRestaurantScoreText) TextView tvRestaurantScoreText;
    @BindView(R.id.ivRestaurantThumb) ImageView ivRestaurantThumb;
    @BindView(R.id.tvNumberOfVotes) TextView tvNumberOfVotes;
    @BindView(R.id.tvCuisineTypes) TextView tvCuisineTypes;
    @BindView(R.id.tvCostPerTwo) TextView tvCostPerTwo;
    @BindView(R.id.tvAddress) TextView tvAddress;

    Restaurant_ restaurant_data;


    public Tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true); // save state on change orientation
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab1, container, false);



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //bind butterknife
        unbinder = ButterKnife.bind(this, view);
        //setup the views
        setupViews();
    }

    @Override
    public void receiveRestaurantId(Restaurant_ restaurant) {
        Log.i("Debugging", "Inside Tab 1, name is: " + restaurant.getName());
        this.restaurant_data = restaurant;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    private void setupViews() {
        //uses the data passed from the MainFragment to display the relevant restaurant information

        //check if the restaurant has a Thumbnail image available in the database
        if (!(restaurant_data.getThumb()).equals("")) {
            Picasso.with(this.getContext())
                    .load(restaurant_data.getThumb())
                    .into(ivRestaurantThumb);
        }
        //if not, use a default background
        else {
            ivRestaurantThumb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        //setup the rest of the views
        ivRestaurantThumb.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        tvRestaurantName.setText(restaurant_data.getName());
        UserRating userRating = restaurant_data.getUserRating();
        Location location = restaurant_data.getLocation();
        tvRestaurantScoreText.setText(userRating.getRatingText());
        tvRestaurantScore.setBackgroundColor(Color.parseColor("#" + (userRating.getRatingColor())));
        tvRestaurantScore.setText(" " + String.valueOf(userRating.getAggregateRating()) + " ");
        tvNumberOfVotes.setText("Based on " + userRating.getVotes() + " vote(s)");
        tvCuisineTypes.setText(restaurant_data.getCuisines());
        tvCostPerTwo.setText("Average Cost for Two: " +  restaurant_data.getCurrency()
                + String.valueOf(restaurant_data.getAverageCostForTwo()));
        tvAddress.setText(location.getAddress());
    }

    //unused methods

    @Override
    public void receiveUserReviews(List<UserReviewWrapper> userReviews) {

    }

    @Override
    public void receiveDailyMenu(List<DailyMenu> dailyMenus) {

    }
}
