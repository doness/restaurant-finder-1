package com.example.laptop.finalproject.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laptop.finalproject.MapsActivity;
import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.adapters.RestaurantListAdapter;
import com.example.laptop.finalproject.models.MarkerData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RestaurantListView extends Fragment {

    List<MarkerData> listData;
    MapsActivity parentActivity;
    Unbinder unbinder;

    @BindView(R.id.rvRestaurantList) RecyclerView rvRestaurantList;

    public RestaurantListView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true); // save state on change orientation
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        initialiseRecyclerView(view.getContext());
        setupRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }

    private void initialiseRecyclerView(Context context){

        //assign a layout manager to the recycler view
        rvRestaurantList.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setupRecyclerView() {

        rvRestaurantList.setAdapter(new RestaurantListAdapter(parentActivity, listData, R.layout.row_restaurant_list,
                getActivity().getApplicationContext()));
    }

    public void receiveListData(List<MarkerData> listData, MapsActivity parentActivity) {

        this.listData = listData;
        this.parentActivity = parentActivity;
    }
}
