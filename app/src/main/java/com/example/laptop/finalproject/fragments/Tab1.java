package com.example.laptop.finalproject.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.contracts.FragmentsContract;
import com.example.laptop.finalproject.models.Restaurant_;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1 extends Fragment implements FragmentsContract.ITabFragment{


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
    }

    @Override
    public void receiveRestaurantId(Restaurant_ restaurant) {
        Log.i("Debugging", "Inside Tab 1, name is: " + restaurant.getName());
    }
}
