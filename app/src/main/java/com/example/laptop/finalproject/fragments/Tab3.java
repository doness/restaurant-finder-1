package com.example.laptop.finalproject.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.adapters.DailyMenuAdapter;
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
    @BindView(R.id.rvDailyMenu) RecyclerView rvDailyMenu;

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
        presenter.fetchDailyMenu(Integer.parseInt(restaurant_data.getId()));
        //setu the swipe to refresh layout
        setupRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbind presenter/butterknife
        presenter.unbind();
        unbinder.unbind();
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
        rvDailyMenu.setLayoutManager(new LinearLayoutManager(context));
    }

    //setup the swipe to refresh layout
    private void setupRefresh(){

        srDailyMenu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.fetchDailyMenu(Integer.parseInt(restaurant_data.getId()));
                        srDailyMenu.setRefreshing(false);
                    }
                }, 2500);
            }
        });
    }

    //pass the relevant data to the recycler view
    private void setupRecyclerView() {

        rvDailyMenu.setAdapter(new DailyMenuAdapter(dailyMenu_data, R.layout.row_daily_menu,
                getActivity().getApplicationContext()));
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
}
