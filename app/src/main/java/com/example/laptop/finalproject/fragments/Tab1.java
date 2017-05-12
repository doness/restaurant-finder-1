package com.example.laptop.finalproject.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.contracts.FragmentsContract;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.UserRating;

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

    Restaurant_ restaurant_data;


    public Tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab1, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setupViews();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        tvRestaurantName.setText(restaurant_data.getName());
        UserRating userRating = restaurant_data.getUserRating();
        tvRestaurantScoreText.setText(userRating.getRatingText());
        tvRestaurantScore.setBackgroundColor(Color.parseColor("#" + (userRating.getRatingColor())));
        tvRestaurantScore.setText(" " + String.valueOf(userRating.getAggregateRating()) + " ");

    }
}
