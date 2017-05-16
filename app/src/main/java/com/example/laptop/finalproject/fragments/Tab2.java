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
import android.widget.TextView;

import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.adapters.ReviewsAdapter;
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
public class Tab2 extends Fragment implements FragmentsContract.ITabFragment {

    @Inject FragmentPresenter presenter;

    Unbinder unbinder;

    @BindView(R.id.srUserReviews) SwipeRefreshLayout srUserReviews;
    @BindView(R.id.rvUserReviews) RecyclerView rvUserReviews;
    @BindView(R.id.tvNoReviews) TextView tvNoReviews;

    Restaurant_ restaurant_data;
    List<UserReviewWrapper> userReviews_data;
    View view;

    public Tab2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true); // save state on change orientation
        ((MyApp)getActivity().getApplication()).getRestaurants_component().inject(this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        presenter.bind(this);
        unbinder = ButterKnife.bind(this, view);

        tvNoReviews.setText("No Reviews Available");
        initialiseRecyclerView(view.getContext());
        presenter.fetchUserReviews(Integer.parseInt(restaurant_data.getId()));
        setupRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
    }


    @Override
    public void receiveUserReviews(List<UserReviewWrapper> userReviews) {
        Log.i("User Reviews Received", "Size is: " + userReviews.size());
        this.userReviews_data = userReviews;
        if (userReviews.size() > 0) {
            tvNoReviews.setText("");
        }
        setupRecyclerView();

    }

    @Override
    public void receiveRestaurantId(Restaurant_ restaurant) {
        Log.i("Debugging", "Inside Tab 2, name is: " + restaurant.getName());
        this.restaurant_data = restaurant;

    }

    private void setupRecyclerView() {
        rvUserReviews.setAdapter(new ReviewsAdapter(userReviews_data, R.layout.row_user_reviews,
                getActivity().getApplicationContext()));
    }

    private void setupRefresh() {
        srUserReviews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.fetchUserReviews(Integer.parseInt(restaurant_data.getId()));
                        srUserReviews.setRefreshing(false);
                    }
                }, 2500);
            }
        });
    }

    private void initialiseRecyclerView(Context context){
        rvUserReviews.setLayoutManager(new LinearLayoutManager(context));
    }

    //unused method

    @Override
    public void receiveDailyMenu(List<DailyMenu> dailyMenus) {

    }
}
