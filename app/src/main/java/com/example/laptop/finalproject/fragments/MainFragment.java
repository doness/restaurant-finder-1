package com.example.laptop.finalproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.adapters.ViewPagerAdapter;
import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.contracts.FragmentsContract;
import com.example.laptop.finalproject.custom_views.SlidingTabLayout;
import com.example.laptop.finalproject.models.Restaurant_;


public class MainFragment extends Fragment implements FragmentsContract.IMainFragment {

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    Restaurant_ restaurant_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true); // save state on change orientation
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles for the Tabs, Number Of Tabs
        //and the data to be displayed
        adapter =  new ViewPagerAdapter(getChildFragmentManager(), Constants.TAB_TITLES,
                Constants.NUMBER_OF_TABS, restaurant_data);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorWhite);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    public void receiveRestaurantData(Restaurant_ restaurant) {
        Log.i("Debugging", "Inside main fragment, data received, name is: " + restaurant.getName());
        Log.i("Debugging", "Inside main fragment, data received, id is: " + restaurant.getId());

        this.restaurant_data = restaurant;
    }
}
