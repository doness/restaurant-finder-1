package com.example.laptop.finalproject.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.contracts.FragmentsContract;
import com.example.laptop.finalproject.injection.MyApp;
import com.example.laptop.finalproject.models.DailyMenu;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.UserReviewWrapper;
import com.example.laptop.finalproject.presenters.FragmentPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3 extends Fragment implements FragmentsContract.ITabFragment {

    @Inject
    FragmentPresenter presenter;

    Unbinder unbinder;

    @BindView(R.id.srDailyMenu) SwipeRefreshLayout srDailyMenu;
    //@BindView(R.id.rvDailyMenu) RecyclerView rvDailyMenu;
    @BindView(R.id.wvMenu) WebView wvMenu;

    View view;
    Restaurant_ restaurant_data;
    List<DailyMenu> dailyMenu_data;

    public Tab3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true); // save state on change orientation
        ((MyApp)getActivity().getApplication()).getRestaurants_component().inject(this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //binding presenter/butterknife
        this.view = view;
        presenter.bind(this);
        unbinder = ButterKnife.bind(this, view);

        //initialise the recycler view
        initialiseRecyclerView(view.getContext());
        //tell the presenter to fetch the daily menus for the given restaurant
        //presenter.fetchDailyMenu(Integer.parseInt(restaurant_data.getId()));
        setupRecyclerView();
        //setup the swipe to refresh layout
        setupRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbind presenter/butterknife
        presenter.unbind();
        unbinder.unbind();
        view = null;
    }

    //method called in the presenter, sends the daily menu data to the fragment
    @Override
    public void receiveDailyMenu(List<DailyMenu> dailyMenus) {

        this.dailyMenu_data = dailyMenus;
        //setup the recycler view after retreiving the menu data
        setupRecyclerView();
    }

    private void initialiseRecyclerView(Context context) {

        //assign a layout manager to the recycler view
        //rvDailyMenu.setLayoutManager(new LinearLayoutManager(context));
    }

    //setup the swipe to refresh layout
    private void setupRefresh(){

        final Tab3 tab3 = this;

        srDailyMenu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (tab3.isAdded()) {
                            //presenter.fetchDailyMenu(Integer.parseInt(restaurant_data.getId()));
                            setupRecyclerView();
                            srDailyMenu.setRefreshing(false);
                        }

                    }
                }, 2500);
            }
        });


    }

    //pass the relevant data to the recycler view
    private void setupRecyclerView() {

        //rvDailyMenu.setAdapter(new DailyMenuAdapter(dailyMenu_data, R.layout.row_daily_menu,
        //        getActivity().getApplicationContext()));

        WebSettings webSettings = wvMenu.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        wvMenu.loadUrl(restaurant_data.getMenuUrl());
        wvMenu.setHorizontalScrollBarEnabled(true);
        wvMenu.setVerticalScrollBarEnabled(true);

        wvMenu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Uri uri = Uri.parse(restaurant_data.getMenuUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                return false;
            }
        });
    }

    //method called i nthe page adapter that passes the restaurant data from the MainFragment to the tab
    @Override
    public void receiveRestaurantId(Restaurant_ restaurant) {
        Log.i("Debugging", "Inside Tab 3, name is: " + restaurant.getName());
        this.restaurant_data = restaurant;

    }

    //unused method

    @Override
    public void receiveUserReviews(List<UserReviewWrapper> userReviews) {

    }

    @Override
    public void getError(String error_message) {

    }
}
