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

        ((MyApp)getActivity().getApplication()).getRestaurants_component().inject(this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        presenter.bind(this);
        unbinder = ButterKnife.bind(this, view);

        initialiseRecyclerView(view.getContext());
        presenter.fetchDailyMenu(Integer.parseInt(restaurant_data.getId()));
        setupRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbind();
        unbinder.unbind();
    }

    @Override
    public void receiveDailyMenu(List<DailyMenu> dailyMenus) {

        this.dailyMenu_data = dailyMenus;
        setupRecyclerView();
    }

    private void initialiseRecyclerView(Context context) {

        rvDailyMenu.setLayoutManager(new LinearLayoutManager(context));
    }

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

    private void setupRecyclerView() {

        rvDailyMenu.setAdapter(new DailyMenuAdapter(dailyMenu_data, R.layout.row_daily_menu,
                getActivity().getApplicationContext()));
    }



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
